package yt.bam.bamradio;

import yt.bam.bamradio.radiomanager.worldguard.WGRegionEventsListener;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import yt.bam.bamradio.commands.*;
import yt.bam.bamradio.radiomanager.RadioManager;
import yt.bam.library.BAMLibrary;
import yt.bam.library.ICommand;


/**
 * @author fr34kyn01535
 */

public class BAMradio extends JavaPlugin {
    
    //////////
    
    public static final Logger logger = Bukkit.getLogger();
    public static BAMradio Instance;
    public static BAMLibrary Library;
    
    @Override
    public void onEnable() {
        Instance = this;
        Map<String,String> defaultTranslation = new HashMap<String, String>();
        defaultTranslation.put("MIDI_MANAGER_EXCEPTION_MIDI_UNAVAILABLE" , "Could not obtain sequencer device - Falling back to software sequencer.");
        defaultTranslation.put("MIDI_MANAGER_NOW_PLAYING" , "Now playing:");
        defaultTranslation.put("MIDI_MANAGER_INVALID_MIDI" , "Invalid midi file:");
        defaultTranslation.put("MIDI_MANAGER_CORRUPT_MIDI" , "Can't read file:");
        defaultTranslation.put("COMMAND_MANAGER_UNKNOWN_COMMAND" , "Unknown command. Type \"/br help\" for help." );
        defaultTranslation.put("COMMAND_MANAGER_INVALID_PARAMETER" , "Invalid parameter. Type \"/br help\" for help." );
        defaultTranslation.put("COMMAND_MANAGER_NO_PERMISSION" , "Missing permission:" );
        defaultTranslation.put("COMMAND_MANAGER_ONLY_CHAT" , "This command is only available ingame" );
        defaultTranslation.put("COMMAND_ABOUT_BY" , "by" );
        defaultTranslation.put("COMMAND_ABOUT_HELP" , "Credits");
        defaultTranslation.put("COMMAND_HELP_HELP" , "Shows all commands" );
        defaultTranslation.put("COMMAND_LIST_TITLE" , "List of tracks:");
        defaultTranslation.put("COMMAND_LIST_HELP" , "List all tracks");
        defaultTranslation.put("COMMAND_MUTE_MESSAGE" , "Muted BAMradio.");
        defaultTranslation.put("COMMAND_MUTE_HELP" , "Mute BAMradio");
        defaultTranslation.put("COMMAND_UNMUTE_HELP" , "Unmute BAMradio");
        defaultTranslation.put("COMMAND_NEXT_HELP" , "Skip to next track");
        defaultTranslation.put("COMMAND_STOP_MESSAGE" , "Stopped playing...");
        defaultTranslation.put("COMMAND_STOP_HELP" , "Stop a track");
        defaultTranslation.put("COMMAND_PLAY_HELP" , "Play a track");
        defaultTranslation.put("COMMAND_PLAY_EXCEPTION_NOT_FOUND" , "Can not find track:");
        defaultTranslation.put("COMMAND_PLAY_EXTENDED_HELP" , "/br play League_of_Legends_-_Season_1.mid or /br play 42");
        defaultTranslation.put("COMMAND_GET_NOT_FOUND" , "track not found in Webservice");
        defaultTranslation.put("COMMAND_GET_HELP" , "Get a track from the BAMradio Webservice");
        defaultTranslation.put("COMMAND_SEARCH_TITLE" , "List of available tracks");
        defaultTranslation.put("COMMAND_SEARCH_HELP" , "Search the BAMradio Webservice");
        defaultTranslation.put("COMMAND_SEARCH_EXTENDED_HELP" , "/br search league");
        defaultTranslation.put("COMMAND_RANDOM_HELP" , "Play a random track");
        
        ArrayList<ICommand> commands= new ArrayList<ICommand>();
        commands.add(new CmdGet());
        commands.add(new CmdList());
        commands.add(new CmdMute());
        commands.add(new CmdNext());
        commands.add(new CmdPlay());
        commands.add(new CmdRandom());
        commands.add(new CmdSearch());
        commands.add(new CmdStop());
        commands.add(new CmdUnmute());
        
        String[] rootCommands = new String[]{"bamradio","br"};
       
        Library = new BAMLibrary(this, defaultTranslation, commands, rootCommands, true, true);
        customOnEnable();
    }
    
    @Override
    public void onDisable() {
        Library.onDisable();
        RadioManager.onDisable();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command root, String commandLabel, String[] args) {
        return Library.onCommand(sender, root,commandLabel,args);
    }
      
    //////////
    
    public WorldGuardPlugin WorldGuardInstance;
    public RadioManager RadioManager = null;
    public boolean NoteBlockAPI;
   
    private WorldGuardPlugin getWGPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin))
        {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
    
    public void customOnEnable(){
        if (getServer().getPluginManager().getPlugin("NoteBlockAPI") != null) {
            getLogger().info("Detected NoteBlockAPI!");
            NoteBlockAPI = true;
        }else{
            NoteBlockAPI = false;
        }
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        WorldGuardInstance = getWGPlugin();
        if (WorldGuardInstance != null)
        {
            getLogger().info("Detected WorldGuard!");
            getServer().getPluginManager().registerEvents(new WGRegionEventsListener(),this);
        }
    }
    
    
}
