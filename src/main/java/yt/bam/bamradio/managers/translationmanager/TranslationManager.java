package yt.bam.bamradio.managers.translationmanager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import yt.bam.bamradio.managers.IManager;

/**
 * TranslationManager
 * @author FR34KYN01535
 * @version 1.1
 */

public class TranslationManager implements IManager {
    private static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;
    public String Language;
    private Map<String,String> translation;
    private Map<String,String> defaultTranslation;
    private FileConfiguration loadedlanguage;
    
    public TranslationManager(Plugin plugin,String language){
        Plugin = plugin;
        Language = language;
    }
    
    public void reloadTranslation(){
        if(Language == null || Language.equals("en")){
            loadDefaults();
            return;
        }else{
            File f = new File(Plugin.getDataFolder()+File.separator+ Language+".yml");
            if(f.exists()) {
                File languageFile = new File(Plugin.getDataFolder()+File.separator+Language+".yml");
                loadedlanguage = YamlConfiguration.loadConfiguration(languageFile);
                loadTranslation();
            }else{
                logger.warning("Languagefile "+Language+".yml not found, falling back to english language!");
                loadDefaults();
            }
        }
    }
    
    public String getTranslation(String key){
        if(translation!=null){
            String value = translation.get("translation."+key);
            if(value==null || value.isEmpty()){
                return "Translation missing: "+key;
            }else{
                return value;
            }
        }
        else{
            return defaultTranslation.get(key);
        }
    }
    
    private void loadTranslation(){
        translation = new HashMap<String, String>();
        for(Map.Entry<String,Object> entry : loadedlanguage.getValues(true).entrySet()){
            translation.put(entry.getKey(),entry.getValue().toString());
        }
    }
    
    private void loadDefaults(){
        defaultTranslation = new HashMap<String, String>();
        defaultTranslation.put("MIDI_MANAGER_EXCEPTION_MIDI_UNAVAILABLE" , "Could not obtain sequencer device - Falling back to software sequencer.");
        defaultTranslation.put("MIDI_MANAGER_NOW_PLAYING" , "Now playing:");
        defaultTranslation.put("MIDI_MANAGER_INVALID_MIDI" , "Invalid midi file:");
        defaultTranslation.put("MIDI_MANAGER_CORRUPT_MIDI" , "Can't read file:");
        defaultTranslation.put("COMMAND_MANAGER_UNKNOWN_COMMAND" , "Unknown command. Type \"/br help\" for help." );
        defaultTranslation.put("COMMAND_MANAGER_INVALID_PARAMETER" , "Invalid parameter. Type \"/br help\" for help." );
        defaultTranslation.put("COMMAND_MANAGER_NO_PERMISSION" , "Missing permission:" );
        defaultTranslation.put("COMMAND_MANAGER_ONLY_CHAT" , "This command is only available ingame" );
        defaultTranslation.put("COMMAND_ABOUT_BY" , "by" );
        defaultTranslation.put("COMMAND_ABOUT_FOR" , "for");
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
    }

    public void onEnable() {
        reloadTranslation();
    }

    public void onDisable() {
     //
    }
    
}