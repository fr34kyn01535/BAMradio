/**
 * Copyright (C) 2012 t7seven7t
 */
package yt.bam.bamradio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiUnavailableException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import yt.bam.bamradio.MidiPlayer;
import yt.bam.bamradio.OldMidiPlayer;
import yt.bam.bamradio.PlayerListener;
import yt.bam.bamradio.SequencerMidiPlayer;

/**
 * @author t7seven7t
 */
public class BAMradio extends JavaPlugin {

	private MidiPlayer midiPlayer;
		
	public void onEnable() {
		
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		
		//saveDefaultConfig();
		//reloadConfig();
						
		initMidiPlayer();
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
		String[] midis = listMidiFiles();
		if (midis.length > 0)
			midiPlayer.playSong(midis[0]);
		
	}
	
	public void onDisable() {
		
		midiPlayer.stopPlaying();
		
	}
	
	public void initMidiPlayer() {
                try {
                        midiPlayer = new SequencerMidiPlayer(this);
                        getLogger().info("Sequencer device obtained!");
                } catch (MidiUnavailableException ex) {
                        getLogger().severe("Could not obtain sequencer device. Defaulting to old player.");
                }
		
		if (midiPlayer == null) {
			midiPlayer = new OldMidiPlayer(this);
		}
		
	}
	
	public MidiPlayer getMidiPlayer() {
		return midiPlayer;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equals("play") && sender.hasPermission("bamradio.play")) {
			
			if (args.length == 1) {
				
				if (midiPlayer.isNowPlaying()) {
					
					midiPlayer.stopPlaying();
					
				}
				
				midiPlayer.playSong(args[0]);
				
				return true;
				
			} else if (args.length == 0) {
				
				StringBuilder msg = new StringBuilder();
				msg.append(ChatColor.YELLOW);
				for (String name : listMidiFiles()) {
					
					msg.append(name + ", ");
					
				}
				
				msg.deleteCharAt(msg.lastIndexOf(","));
				sender.sendMessage(ChatColor.AQUA + "List of midi files:");
				sender.sendMessage(msg.toString());
				return true;
				
			}
			
		} 
		
		//if (command.getName().equals("tune") && sender instanceof Player) {
		//	
		//	if (args.length == 1) {
		//		
		//		Player player = (Player) sender;
		//		
		//		if (args[0].equalsIgnoreCase("in")) {
		//			
		//			midiPlayer.tuneIn(player);
		//			return true;
		//			
		//		}
		//		
		//		if (args[0].equalsIgnoreCase("out")) {
		//			
		//			midiPlayer.tuneOut(player);
		//			return true;
		//			
		//		}
		//				
		//	}
		//	
		//}
		
		return false;
		
	}
	
	public File getMidiFile(String fileName) {
		
		File midiFile = new File(getDataFolder(), fileName + ".mid");
		if (!midiFile.exists())
			return null;
		return midiFile;
		
	}
	
	public String[] listMidiFiles() {
		
		File[] files = getDataFolder().listFiles();
		List<String> midiFiles = new ArrayList<String>();
		
		for (File file : files) {
			
			if (file.getName().endsWith(".mid")) {
				
				midiFiles.add(file.getName().substring(0, file.getName().lastIndexOf(".mid")));
				
			}
			
		}
		
		return midiFiles.toArray(new String[0]);
		
	}
	
}
