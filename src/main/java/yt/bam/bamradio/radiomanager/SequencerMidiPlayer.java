package yt.bam.bamradio.radiomanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import yt.bam.bamradio.BAMradio;
import yt.bam.library.Helpers;

/**
 * @author fr34kyn01535
 */

public class SequencerMidiPlayer implements MidiPlayer, Receiver {
    public static final Logger logger = Bukkit.getLogger();
    
    private final Sequencer sequencer;
    private final Map<Integer, Byte> channelPatches; 
    
    private RadioManager manager;
    
    public SequencerMidiPlayer(RadioManager manager) throws MidiUnavailableException {
        this.manager = manager;
        manager.tunedIn = new ArrayList<Player>();
        channelPatches = new HashMap<Integer, Byte>(); 
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.getTransmitter().setReceiver(this);
    }

    public void stopPlaying() {
        sequencer.stop();
        manager.Plugin.getServer().getScheduler().cancelTasks(manager.Plugin);
    }

    public boolean playSong(String midiName) {
            try{    
                manager.nowPlayingFile = midiName;

                File midiFile = manager.getMidiFile(midiName);
                if (midiFile == null){
                 return false;
                }

                try {
                        Sequence midi = MidiSystem.getSequence(midiFile);
                        sequencer.setSequence(midi);
                        sequencer.start();
                        manager.nowPlaying = true;
                } catch (InvalidMidiDataException ex) {
                        System.err.println(BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_INVALID_MIDI")+" " + midiName);
                } catch (IOException e) {
                        System.err.println(BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_CORRUPT_MIDI")+" " + midiName);
                }

                for (Player player : manager.tunedIn) {
                        Helpers.sendMessage(player,BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_NOW_PLAYING")+" " + ChatColor.YELLOW + midiName.replace("_", " ").replace(".mid",""));
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!manager.nowPlaying)
                                this.cancel();

                        if (!sequencer.isRunning() || sequencer.getMicrosecondPosition() > sequencer.getMicrosecondLength()) {
                                stopPlaying();
                                if(manager.AutoPlayNext){
                                    manager.playNextSong();
                                }
                        }
                    }
                }.runTaskTimer(manager.Plugin, 20L, 20L);
            }catch(Exception e){
                    System.err.println(BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_CORRUPT_MIDI")+" " + midiName+" ("+e.getMessage()+")");
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

                    for (Player player : manager.tunedIn) {
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
                    manager.playNextSong();
            }
    }


}
