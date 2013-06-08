package yt.bam.bamradio.managers.configurationmanager;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import managers.IManager;

/**
 * @author fr34kyn01535
 */

public class ConfigurationManager implements IManager {
    private static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;

    public boolean AutoPlayNext = false;
    public boolean AutoPlay = false;
    public boolean ForceSoftwareSequencer = false;
    
    public ConfigurationManager(Plugin plugin){
        Plugin=plugin;
        Plugin.saveDefaultConfig();
        Plugin.reloadConfig();
        readConfiguration();
    }
    
    public void readConfiguration(){
        AutoPlay = Plugin.getConfig().getBoolean("auto-play");
        AutoPlayNext = Plugin.getConfig().getBoolean("auto-play-next");
        ForceSoftwareSequencer = Plugin.getConfig().getBoolean("force-software-sequencer");
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
