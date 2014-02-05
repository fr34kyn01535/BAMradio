package yt.bam.bamradio.radiomanager;

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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import yt.bam.bamradio.BAMradio;

/**
 * @author fr34kyn01535
 */

public class MinecraftMidiPlayer implements MidiPlayer {
    public static final Logger logger = Bukkit.getLogger();
    public static final long MILLIS_PER_TICK = 3;

    private final List<MidiTrack> midiTracks;
    private final Map<Integer, Integer> channelPatches;

    private float tempo;
    private int resolution;
    private long timeLeft;
    private float currentTick = 0;

    private Timer timer;
    
    private RadioManager manager;

    public MinecraftMidiPlayer(RadioManager manager) {
        this.manager = manager;
        timer = new Timer();
        midiTracks = new ArrayList<MidiTrack>();
        channelPatches = new HashMap<Integer, Integer>();
    }

    public void stopPlaying() {
        synchronized (midiTracks) {
            manager.nowPlaying = false;
            midiTracks.clear();
            timer.cancel();
            timer = new Timer();
        }
    }

    public boolean playSong(final String midiName) {
        manager.nowPlayingFile = midiName;
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
                System.err.println(BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_INVALID_MIDI")+" " + midiName);
        } catch (IOException ex) {
                System.err.println(BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_CORRUPT_MIDI")+" " + midiName);
        }
        for (Player player : manager.tunedIn) {
            manager.NowPlaying(player, false);
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
                    volume = volume + BAMradio.Instance.RadioManager.Volume;
                    int note = Integer.valueOf((midiNote - 6) % 24);
                    int channel = message.getChannel();
                    int patch = 1;
                    if (channelPatches.containsKey(channel))
                        patch = channelPatches.get(channel);
                    Sound instrument = Instrument.getInstrument(patch, channel);
                    float notePitch = NotePitch.getPitch(note);
                    if(instrument!=null){
                        for (Player player : manager.tunedIn) {
                                player.playSound(player.getLocation(), instrument, volume, notePitch);
                        }
                    }
                } else if (message.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    channelPatches.put(message.getChannel(), message.getData1());
                } else if (message.getCommand() == ShortMessage.STOP) {
                    stopPlaying();
                    manager.playNextSong();
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
                    manager.nowPlaying = true;
                    currentTick = 0;
            }

            @Override
            public void run() {
                if (manager.nowPlaying) {
                    currentTick += tempo * resolution;
                    synchronized (midiTracks) {
                        for (MidiTrack track : midiTracks) {
                                track.nextTick(currentTick);
                        }
                    }
                    timeLeft -= MILLIS_PER_TICK;

                    if (timeLeft <= 0) {
                        stopPlaying();
                        if(manager.AutoPlayNext){
                            new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                            manager.playNextSong();
                                        }
                            }.runTask(BAMradio.Instance);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
    }
}
