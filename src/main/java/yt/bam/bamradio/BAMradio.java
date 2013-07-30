package yt.bam.bamradio;

import com.mewin.WGRegionEvents.WGRegionEventsListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import yt.bam.bamradio.managers.configurationmanager.ConfigurationManager;
import yt.bam.bamradio.managers.commandmanager.CommandManager;
import yt.bam.bamradio.managers.radiomanager.RadioManager;
import yt.bam.bamradio.managers.translationmanager.TranslationManager;
import org.mcstats.MetricsLite;
import yt.bam.bamradio.managers.IManager;


/**
 * @author fr34kyn01535
 */

public class BAMradio extends JavaPlugin {
    public static final Logger logger = Bukkit.getLogger();
    private ArrayList<IManager> managers = null;
    public static BAMradio Instance;
    
    private WGRegionEventsListener listener;
    private WorldGuardPlugin WGInstance;
    
    private ConfigurationManager ConfigurationManager = null;
    public ConfigurationManager getConfigurationManager(){
        return ConfigurationManager;
    }
    private CommandManager CommandManager = null;
    public CommandManager getCommandManager(){
        return CommandManager;
    }
    private TranslationManager TranslationManager = null;
    public TranslationManager getTranslationManager(){
        return TranslationManager;
    }
    private RadioManager RadioManager = null;
    public RadioManager getRadioManager(){
        return RadioManager;
    }
    
    public boolean NoteBlockAPI;
   
    @Override
    public void onEnable() {
        Instance = this;
       
        if (getServer().getPluginManager().getPlugin("NoteBlockAPI") != null) {
            getLogger().info("Detected NoteBlockAPI!");
            NoteBlockAPI = true;
        }else{
            NoteBlockAPI = false;
        }
        
        managers = new ArrayList<IManager>();
        ConfigurationManager = (ConfigurationManager) registerManager(new ConfigurationManager(this));
        TranslationManager = (TranslationManager) registerManager(new TranslationManager(this,ConfigurationManager.Language));
        RadioManager = (RadioManager) registerManager(new RadioManager(this,TranslationManager,ConfigurationManager.AutoPlay,ConfigurationManager.AutoPlayNext,ConfigurationManager.ForceSoftwareSequencer,ConfigurationManager.Region));
        CommandManager = (CommandManager) registerManager(new CommandManager(this,TranslationManager,new String[]{"bamradio","br"},"yt/bam/bamradio/managers/commandmanager/commands"));
        registerListener();
        
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
        //try{
        //    String latestVersion = getVersion();
        //    int lVersion = Integer.parseInt(latestVersion.replace(".", "").replace("v", ""));
        //    int cVersion = Integer.parseInt(this.getDescription().getVersion().replace(".", "").replace("v", ""));
        //
        //    if(lVersion>cVersion){
        //        getLogger().info("A new version "+latestVersion+" is available!");
        //        getLogger().info("Get it on http://dev.bukkit.org/bukkit-mods/bamradio/");
        //    }
        //}catch (Exception e){
        //    //Ok... well then not..
        //}
        
        WGInstance = getWGPlugin();
        if (WGInstance != null)
        {
            getLogger().info("Detected WorldGuard!");
            listener = new WGRegionEventsListener();
            getServer().getPluginManager().registerEvents(listener,this);
        }
    }
        
    private WorldGuardPlugin getWGPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (plugin == null || !(plugin instanceof WorldGuardPlugin))
        {
            return null;
        }
        
        return (WorldGuardPlugin) plugin;
    }
    public WorldGuardPlugin getWGInstance()
    {
        return WGInstance;
    }
    private String getVersion(){
        try{
            Scanner sc = new Scanner(new URL("http://radio.bam.yt/version.php").openStream(), "UTF-8");
            String out = sc.useDelimiter("\\A").next();
            if(out != null && !out.isEmpty()){
                return out;
            }
        }catch (Exception e){
            //Ok... well then not..
        }
        return "0";
    }
	
    @Override
    public void onDisable() {
        CommandManager.onDisable();
        RadioManager.onDisable();
        TranslationManager.onDisable();
        ConfigurationManager.onDisable();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command root, String commandLabel, String[] args) {
        return CommandManager.onCommand(sender, root,commandLabel,args);
    }
    
    private IManager registerManager(IManager manager){
        managers.add(manager);
        manager.onEnable();
        return manager;
    }
    
    private void registerListener(){
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
    
}
