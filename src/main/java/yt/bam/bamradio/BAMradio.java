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

/**
 * @author fr34kyn01535,t7seven7t
 */

public class BAMradio extends JavaPlugin {
    public static BAMradio Instance;
    public static MidiPlayer MidiPlayer;
    private String[] fileList;	
    
    public static void sendMessage(Player player,String message){
        player.sendMessage(ChatColor.GRAY+"[BAMradio] "+ChatColor.BLUE + message);
    }
    public static void sendMessage(CommandSender player,String message){
        player.sendMessage(ChatColor.GRAY+"[BAMradio] "+ChatColor.BLUE + message);
    }
        
    @Override
    public void onEnable() {
        this.Instance = this;
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        initMidiPlayer();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        Player[] onlinePlayerList = getServer().getOnlinePlayers();
        for(Player player : onlinePlayerList){
        if (MidiPlayer != null)
            MidiPlayer.tuneIn(player);
        }

        String[] midis = listMidiFiles();
        if (midis.length > 0)
            MidiPlayer.playSong(midis[0]);
    }
	
    @Override
    public void onDisable() {
        MidiPlayer.stopPlaying();
    }

    public void onReload(){
    }
        
        
    public void initMidiPlayer() {
        try {
            MidiPlayer = new SequencerMidiPlayer();
            getLogger().info("Sequencer device obtained!");
        } catch (MidiUnavailableException ex) {
            getLogger().severe("Could not obtain sequencer device - Falling back.");
            MidiPlayer = new OldMidiPlayer();
        }
    }
	

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if ((command.getName().toLowerCase().equals("br") || command.getName().toLowerCase().equals("bamradio"))) {
                if (args.length == 0 ) {
                    sender.sendMessage(ChatColor.BOLD+""+ChatColor.GREEN+"##############   BAMradio by FR34KYN01535   ############");
                    sender.sendMessage(ChatColor.BOLD+"/bamradio - Shows this help");
                    sender.sendMessage(ChatColor.BOLD+ "/br - Alias to /bamradio");
                    if(sender.hasPermission("bamradio.list"))sender.sendMessage(ChatColor.BOLD+ "/br list - List all midis");
                    if(sender.hasPermission("bamradio.play"))sender.sendMessage(ChatColor.BOLD+ "/br play <name|index> - Play a midi");
                    if(sender.hasPermission("bamradio.play"))sender.sendMessage(ChatColor.GRAY+""+ ChatColor.ITALIC+"/br play League_of_Legends_-_Season_1.mid "+ChatColor.RESET+""+ChatColor.GRAY+"or "+ChatColor.ITALIC+"/br play 42");
                    if(sender.hasPermission("bamradio.next"))sender.sendMessage(ChatColor.BOLD+ "/br next - Skip to next midi");
                    if(sender.hasPermission("bamradio.stop"))sender.sendMessage(ChatColor.BOLD+"/br stop - Stop a midi");
                    if(sender.hasPermission("bamradio.mute"))sender.sendMessage(ChatColor.BOLD+"/br mute - Mute BAMradio");
                    if(sender.hasPermission("bamradio.mute"))sender.sendMessage(ChatColor.BOLD+"/br unmute - Unmute BAMradio");
                    sender.sendMessage(ChatColor.BOLD+""+ChatColor.GREEN+ "#####################################################");
                    return true;
                }
                if (args.length == 1) {
                    if(args[0].toLowerCase().equals("list")||args[0].toLowerCase().equals("play")){
                        if(hasPermission(sender,"bamradio.list")){
                            sender.sendMessage(ChatColor.GREEN + "List of midi files:");
                            fileList = listMidiFiles();
                            int i = 0;
                            for (String name : fileList) {
                                sender.sendMessage(ChatColor.GREEN + "["+ChatColor.BOLD+i+ChatColor.RESET+""+ChatColor.GREEN+"] "+ChatColor.RESET+ name);
                                i++;
                            }
                        }
                        return true;
                    }
                    if(args[0].toLowerCase().equals("next")){
                        if(hasPermission(sender,"bamradio.next")){
                        if (MidiPlayer.isNowPlaying()) {
                            MidiPlayer.stopPlaying();
                        }
                        MidiPlayer.playNextSong();
                        return true;
                        }
                    }
                    if(args[0].toLowerCase().equals("stop")){
                        if(hasPermission(sender,"bamradio.stop")){
                        if (MidiPlayer.isNowPlaying()) {
                            MidiPlayer.stopPlaying();
                            sendMessage(sender,"Stopped playing...");
                        }
                        return true;
                        }
                    }

                    if(args[0].toLowerCase().equals("mute")){
                        if(hasPermission(sender,"bamradio.mute")){
                            MidiPlayer.tuneOut((Player) sender);
                            sendMessage(sender,"Muted BAMradio.");
                            return true;
                        }
                    }

                    if(args[0].toLowerCase().equals("unmute")){
                        if(hasPermission(sender,"bamradio.mute")){
                            MidiPlayer.tuneIn((Player) sender);
                            sendMessage(sender,"Unmuted BAMradio.");
                            return true;
                        }
                    }

                    if(args[0].toLowerCase().equals("about")||args[0].toLowerCase().equals("info")){
                        sendMessage(sender, ChatColor.GREEN + "BAMradio by FR34KYN01535@bam.yt");
                        sendMessage(sender, ChatColor.GREEN + "Coded for BAMcraft (bam.yt)");
                        return true;
                    }
                    return true;
                }
                if (args.length == 2) {

                    if(args[0].toLowerCase().equals("play")){
                        if(sender.hasPermission("bamradio.play")){
                            if (MidiPlayer.isNowPlaying()) {
                                MidiPlayer.stopPlaying();
                            }
                             if(isInteger(args[1])&&fileList!=null){
                                int index = Integer.parseInt(args[1]); 
                                if(index<fileList.length){
                                    MidiPlayer.playSong(fileList[index]);
                                }
                            }else{ 
                                if(!MidiPlayer.playSong(args[1])){
                                    sendMessage(sender, ChatColor.RED + "Can not find midi \""+args[1]+"\"");
                                }
                            }
                            return true;
                        }
                    }
                    return true;
                }
                return true;	
            } 
            return false;
    }
        
    public boolean hasPermission(CommandSender player,String permission){
        if(player.hasPermission(permission)){
            return true;
        }else{    
            sendMessage(player, ChatColor.RED + "You dont have the permission: ("+permission+")");                 
            return false;
        }
    }
    public boolean isInteger( String input )  
    {  
       try  
       {  
          Integer.parseInt( input );  
          return true;  
       }  
       catch(Exception ex)  
       {  
          return false;  
       }  
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
