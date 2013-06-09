package yt.bam.bamradio.managers.midimanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import yt.bam.bamradio.Helpers;

/**
 * @author fr34kyn01535
 */

public class MinecraftMidiPlayer implements MidiPlayer {
    public static final Logger logger = Bukkit.getLogger();
    public static final long MILLIS_PER_TICK = 2;

    private final List<Player> tunedIn = new ArrayList<Player>();
    private final List<MidiTrack> midiTracks = new ArrayList<MidiTrack>();
    private final Map<Integer, Integer> channelPatches = new HashMap<Integer, Integer>();

    private boolean nowPlaying = false;
    private float tempo;
    private int resolution;
    private long timeLeft;
    private float currentTick = 0;

    private int currentSong = 0;
    private String midiName;

    private Timer timer;
    
    private MidiManager manager;

    public MinecraftMidiPlayer(MidiManager manager) {
        this.manager = manager;
        timer = new Timer();
    }

    public boolean isNowPlaying() {
        return nowPlaying;
    }

    public void tuneIn(Player player) {
        tunedIn.add(player);
        Helpers.sendMessage(player,manager.TranslationManager.getTranslation("MIDI_MANAGER_NOW_PLAYING")+" " + ChatColor.YELLOW + midiName.replace("_", " "));
    }

    public void tuneOut(Player player) {
        tunedIn.remove(player);
    }

    public void stopPlaying() {
        synchronized (midiTracks) {
            nowPlaying = false;
            midiTracks.clear();
            timer.cancel();
            timer = new Timer();
        }
    }

    public void playNextSong() {
        currentSong++;
        String[] midiFileNames = manager.listMidiFiles();
        if(currentSong >= midiFileNames.length)
            currentSong = 0;
        playSong(midiFileNames[currentSong]);
    }

    public boolean playSong(final String midiName) {
        this.midiName = midiName;
        File midiFile = manager.getMidiFile(midiName);
        if (midiFile == null)
            return false;
        try {
            Sequence midi = MidiSystem.getSequence(midiFile);
            int microsPerQuarterNote = 0;
            Track firstTrack = midi.getTracks()[0];
            for (int i = 0; i < firstTrack.size(); i++) {
                if (firstTrack.get(i).getMessage().getStatus() == MetaMessage.META && firstTrack.get(i).getMessage().getMessage()[1] == 81) {
                    MetaMessage message = (MetaMessage) firstTrack.get(i).getMessage();
                    byte[] data = message.getData();
                    for (byte b : data) {
                        microsPerQuarterNote <<= 8;
                        microsPerQuarterNote += b;
                    }
                    break;
                }
            }
            tempo = (500000.0f / microsPerQuarterNote) * 0.8f * (MILLIS_PER_TICK / 20f);
            timeLeft = midi.getMicrosecondLength() / 1000;
            resolution = (int) Math.floor(midi.getResolution() / 24);
            for (int i = 0; i < midi.getTracks().length; i++) {
                MidiTrack midiTrack = new MidiTrack(this, midi.getTracks()[i]);
                midiTracks.add(midiTrack);
            }
        } catch (InvalidMidiDataException ex) {
                System.err.println(manager.TranslationManager.getTranslation("MIDI_MANAGER_INVALID_MIDI")+" " + midiName);
        } catch (IOException ex) {
                System.err.println(manager.TranslationManager.getTranslation("MIDI_MANAGER_CORRUPT_MIDI")+" " + midiName);
        }
        for (Player player : tunedIn) {
            Helpers.sendMessage(player,manager.TranslationManager.getTranslation("MIDI_MANAGER_NOW_PLAYING")+" " + ChatColor.YELLOW + midiName.replace("_", " "));
        }
        timer.scheduleAtFixedRate(new TickTask(), MILLIS_PER_TICK, MILLIS_PER_TICK);
        return true;
    }

    public void onMidiMessage(MidiMessage event) {
            if (event instanceof ShortMessage) {
                ShortMessage message = (ShortMessage) event;
                if (message.getCommand() == ShortMessage.NOTE_ON) {
                    int midiNote = message.getData1();
                    float volume = message.getData2();
                    if (volume == 0)
                        volume =1;
                    int note = Integer.valueOf((midiNote - 6) % 24);
                    int channel = message.getChannel();
                    int patch = 1;
                    if (channelPatches.containsKey(channel))
                        patch = channelPatches.get(channel);
                    Sound instrument = Instrument.getInstrument(patch, channel);
                    float notePitch = NotePitch.getPitch(note);
                    if(instrument!=null){
                        for (Player player : tunedIn) {
                                player.playSound(player.getLocation(), instrument, volume, notePitch);
                        }
                    }
                } else if (message.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    channelPatches.put(message.getChannel(), message.getData1());
                } else if (message.getCommand() == ShortMessage.STOP) {
                    stopPlaying();
                    playNextSong();
                }
            } else if (event instanceof MetaMessage) {
                MetaMessage message = (MetaMessage) event;
                if (message.getType() == 0x51) {
                    int microsPerQuarterNote = 0;
                    byte[] data = message.getData();
                    for (byte b : data) {
                        microsPerQuarterNote <<= 8;
                        microsPerQuarterNote += b;
                    }
                    tempo = (500000.0f / microsPerQuarterNote) * 0.8f * (MILLIS_PER_TICK / 20f);
                }
            }
    }

    public class TickTask extends TimerTask {

            public TickTask() {
                    super();
                    nowPlaying = true;
                    currentTick = 0;
            }

            @Override
            public void run() {
                if (nowPlaying) {
                    currentTick += tempo * resolution;
                    synchronized (midiTracks) {
                        for (MidiTrack track : midiTracks) {
                                track.nextTick(currentTick);
                        }
                    }
                    timeLeft -= MILLIS_PER_TICK;

                    if (timeLeft <= 0) {
                        stopPlaying();
                        if(manager.ConfigurationManager.AutoPlayNext){
                            new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                            playNextSong();
                                        }
                            }.runTask(manager.Plugin);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
    }
}
