package yt.bam.bamradio.managers.configurationmanager;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import yt.bam.bamradio.managers.IManager;

/**
 * ConfigurationManager
 * @author FR34KYN01535
 * @version 1.1
 */

public class ConfigurationManager implements IManager {
    private static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;

    public boolean AutoPlayNext;
    public boolean AutoPlay;
    public boolean ForceSoftwareSequencer;
    public String Language;
    public String Region;
    
    public boolean Update = false;
    
    public ConfigurationManager(Plugin plugin){
        Plugin=plugin;
    }
    
    public void readConfiguration(){
        if (Plugin.getConfig().get("auto-play") == null){
            Plugin.getConfig().set("auto-play",true);
            AutoPlay=true;
            Update=true;
        }else{
            AutoPlay = Plugin.getConfig().getBoolean("auto-play");
        }
        
        if (Plugin.getConfig().get("auto-play-next") == null){
            Plugin.getConfig().set("auto-play-next",true);
            AutoPlayNext=true;
            Update=true;
        }else{
            AutoPlayNext = Plugin.getConfig().getBoolean("auto-play-next");
        }
        
        if (Plugin.getConfig().get("force-software-sequencer") == null){
            Plugin.getConfig().set("force-software-sequencer",false);
            ForceSoftwareSequencer=false;
            Update=true;
        }else{
            ForceSoftwareSequencer = Plugin.getConfig().getBoolean("force-software-sequencer");
        }
       
        if (Plugin.getConfig().get("language") == null){
            Plugin.getConfig().set("language",true);
            Language="en";
            Update=true;
        }else{
            Language = Plugin.getConfig().getString("language");
        }
        if (Plugin.getConfig().get("region") == null){
            Plugin.getConfig().set("region","");
            Region="";
            Update=true;
        }else{
            Region = Plugin.getConfig().getString("region");
        }
        if(Update){
            Plugin.saveConfig();
        }
    }
    
    public void onEnable() {
        Plugin.saveDefaultConfig();
        Plugin.reloadConfig();
        readConfiguration();
    }

    public void onDisable() {
    //
    }
}
