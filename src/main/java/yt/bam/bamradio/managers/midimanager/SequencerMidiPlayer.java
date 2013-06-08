package yt.bam.bamradio.managers.midimanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import yt.bam.bamradio.Helpers;

/**
 * @author fr34kyn01535
 */

public class SequencerMidiPlayer implements MidiPlayer, Receiver {
    public static final Logger logger = Bukkit.getLogger();
    private final Sequencer sequencer;
    private final List<Player> tunedIn = new ArrayList<Player>();
    private final Map<Integer, Byte> channelPatches = new HashMap<Integer, Byte>(); 

    private boolean nowPlaying = false;
    private int currentSong = 0;
    private String midiName;
    private MidiManager manager;
    public SequencerMidiPlayer(MidiManager manager) throws MidiUnavailableException {
        this.manager = manager;
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.getTransmitter().setReceiver(this);
    }

    public void tuneIn(Player player) {
        tunedIn.add(player);
        if(midiName!=null){
            Helpers.sendMessage(player,"Now playing: " + ChatColor.YELLOW + midiName.replace("_", " ").replace(".mid",""));
        }
    }

    public void tuneOut(Player player) {
        tunedIn.remove(player);
    }

    public boolean isNowPlaying() {
        return nowPlaying;
    }

    public void stopPlaying() {
        sequencer.stop();
        manager.Plugin.getServer().getScheduler().cancelTasks(manager.Plugin);
    }

    public void playNextSong() {
            currentSong++;

            String[] midiFileNames = manager.listMidiFiles();

            if (currentSong >= midiFileNames.length)
                    currentSong = 0;

            playSong(midiFileNames[currentSong]);
    }

    public boolean playSong(String midiName) {
            try{    
                this.midiName = midiName;

                File midiFile = manager.getMidiFile(midiName);
                if (midiFile == null){
                 return false;
                }

                try {
                        Sequence midi = MidiSystem.getSequence(midiFile);
                        sequencer.setSequence(midi);
                        sequencer.start();
                        nowPlaying = true;
                } catch (InvalidMidiDataException ex) {
                        System.err.println("Invalid midi file: " + midiName);
                } catch (IOException e) {
                        System.err.println("Can't read file: " + midiName);
                }

                for (Player player : tunedIn) {
                        Helpers.sendMessage(player,"Now playing: " + ChatColor.YELLOW + midiName.replace("_", " ").replace(".mid",""));
                }

                new BukkitRunnable() {
                        @Override
                        public void run() {
                                if (!nowPlaying)
                                        this.cancel();

                                if (!sequencer.isRunning() || sequencer.getMicrosecondPosition() > sequencer.getMicrosecondLength()) {
                                        stopPlaying();
                                        if(manager.ConfigurationManager.AutoPlayNext){
                                            playNextSong();
                                        }
                                }
                        }

                }.runTaskTimer(manager.Plugin, 20L, 20L);
            }catch(Exception e){
                    System.err.println("Can't read file: " + midiName+" ("+e.getMessage()+")");
            }
                    return true;
    }

    @Override
    protected void finalize() {
            sequencer.close();
    }

    @Override
    public void close() {
            // We don't really need this in this case, thanks anyway oracle <3
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {

            if (!(message instanceof ShortMessage))
                    return; // Not interested in meta events

            ShortMessage event = (ShortMessage) message;

            if (event.getCommand() == ShortMessage.NOTE_ON) {

                    int midiNote = event.getData1();
                    float volume = event.getData2() / 127;

                    if (volume == 0)
                            volume = 3;

                    int note = Integer.valueOf((midiNote - 6) % 24);

                    int channel = event.getChannel();
                    byte patch = 1;
                    if (channelPatches.containsKey(channel))
                            patch = channelPatches.get(channel);

                    for (Player player : tunedIn) {
                            Sound sound = Instrument.getInstrument(patch, channel);
                            if(sound!=null){
                                if(sound==Sound.NOTE_PLING){
                                     player.playSound(player.getLocation().add(0, 20, 0), sound, 5, NotePitch.getPitch(note));
                                }else{
                                    player.playSound(player.getLocation(), sound, volume, NotePitch.getPitch(note));
                                } 
                            }
                    }

            } else if (event.getCommand() == ShortMessage.PROGRAM_CHANGE) {

                    channelPatches.put(event.getChannel(), (byte) event.getData1());

            } else if (event.getCommand() == ShortMessage.STOP) {
                    stopPlaying();
                    playNextSong();
            }
    }


}
