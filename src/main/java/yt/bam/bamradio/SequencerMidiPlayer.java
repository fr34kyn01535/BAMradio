package yt.bam.bamradio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author fr34kyn01535,t7seven7t
 */
public class SequencerMidiPlayer implements MidiPlayer, Receiver {
	private final Sequencer sequencer;
	private final List<Player> tunedIn = new ArrayList<Player>();
	private final Map<Integer, Byte> channelPatches = new HashMap<Integer, Byte>(); 
	
	private boolean nowPlaying = false;
	private int currentSong = 0;
	private String midiName;
	
	public SequencerMidiPlayer() throws MidiUnavailableException {
		sequencer = MidiSystem.getSequencer();
		sequencer.open();
                sequencer.getTransmitter().setReceiver(this);
	}
	
	public void tuneIn(Player player) {
            tunedIn.add(player);
            if(midiName!=null){
                BAMradio.Instance.sendMessage(player,"Now playing: " + ChatColor.YELLOW + midiName.replace("_", " ").replace(".mid",""));
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
            BAMradio.Instance.getServer().getScheduler().cancelTasks(BAMradio.Instance);
	}
	
	public void playNextSong() {
		currentSong++;
		
		String[] midiFileNames = BAMradio.Instance.listMidiFiles();
		
		if (currentSong >= midiFileNames.length)
			currentSong = 0;
		 
		playSong(midiFileNames[currentSong]);
	}
	
	public boolean playSong(String midiName) {
		try{    
                    this.midiName = midiName;

                    File midiFile = BAMradio.Instance.getMidiFile(midiName);
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
                            BAMradio.Instance.sendMessage(player,"Now playing: " + ChatColor.YELLOW + midiName.replace("_", " ").replace(".mid",""));
                    }

                    new BukkitRunnable() {
                            @Override
                            public void run() {
                                    if (!nowPlaying)
                                            this.cancel();

                                    if (!sequencer.isRunning() || sequencer.getMicrosecondPosition() > sequencer.getMicrosecondLength()) {
                                            stopPlaying();
                                            playNextSong();
                                    }
                            }

                    }.runTaskTimer(BAMradio.Instance, 20L, 20L);
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
