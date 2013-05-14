/**
 * Copyright (C) 2013 fr34kyn01535
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
import yt.bam.bamradio.PlayerListener;
import yt.bam.bamradio.SequencerMidiPlayer;

/**
 * @author fr34kyn01535,t7seven7t
 */
public class BAMradio extends JavaPlugin {

	private MidiPlayer midiPlayer;
		
        public static void sendMessage(Player player,String message){
            player.sendMessage(ChatColor.GRAY+"[BAMradio] "+ChatColor.BLUE + message);
        }
        public static void sendMessage(CommandSender player,String message){
            player.sendMessage(ChatColor.GRAY+"[BAMradio] "+ChatColor.BLUE + message);
        }
        
	public void onEnable() {
		
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		
		//saveDefaultConfig();
		//reloadConfig();
						
		initMidiPlayer();
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		
                Player[] onlinePlayerList = getServer().getOnlinePlayers();
                for(Player player : onlinePlayerList){
		if (getMidiPlayer() != null)
			getMidiPlayer().tuneIn(player);
                }
                
		String[] midis = listMidiFiles();
		if (midis.length > 0)
			midiPlayer.playSong(midis[0]);
		
	}
	
	public void onDisable() {
		midiPlayer.stopPlaying();
	}
	
        public void onReload(){
        
        }
        
        
	public void initMidiPlayer() {
                try {
                        midiPlayer = new SequencerMidiPlayer(this);
                        getLogger().info("Sequencer device obtained!");
                } catch (MidiUnavailableException ex) {
                        getLogger().severe("Could not obtain sequencer device - Falling back.");
                        midiPlayer = new OldMidiPlayer(this);
                }
                
	}
	
	public MidiPlayer getMidiPlayer() {
		return midiPlayer;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if ((command.getName().toLowerCase().equals("br") || command.getName().toLowerCase().equals("bamradio"))) {
                    if (args.length == 0) {
                            StringBuilder msg = new StringBuilder();
                            msg.append(ChatColor.YELLOW);
                            for (String name : listMidiFiles()) {
                                    msg.append(name + ", ");
                            }
                            if(msg.toString().contains(",")){
                                msg.deleteCharAt(msg.lastIndexOf(","));
                            }
                            sendMessage(sender, ChatColor.GREEN + "BAMradio by FR34KYN01535@bam.yt");
                            if(sender.hasPermission("bamradio.list")){
                                sender.sendMessage(ChatColor.AQUA + "List of midi files:");
                                sender.sendMessage(msg.toString());
                            }
                            return true;
                    }
                    if (args.length == 1) {
                        if(args[0].toLowerCase().equals("next")){
                            if (midiPlayer.isNowPlaying()) {
                                midiPlayer.stopPlaying();
                            }
                            midiPlayer.playNextSong();
                            return true;
                        }
                        if(args[0].toLowerCase().equals("stop")){
                            if (midiPlayer.isNowPlaying()) {
                                midiPlayer.stopPlaying();
                                sendMessage(sender,"Stopped playing...");
                            }
                            return true;
                        }
                        return true;
                    }
                    if (args.length == 2) {
                        if(args[0].toLowerCase().equals("play")){
                            if (midiPlayer.isNowPlaying()) {
                                midiPlayer.stopPlaying();
                            }
                            if(!midiPlayer.playSong(args[1])){
                                sendMessage(sender,"Can not find midi "+args[1]);
                            }
                            return true;
                        }
                        return true;
                    }
                    return true;	
		} 
		return false;
	}
        
        
        
	
	public File getMidiFile(String fileName) {
		
		File midiFile = new File(getDataFolder(), fileName.replace(".mid", "") + ".mid");
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
