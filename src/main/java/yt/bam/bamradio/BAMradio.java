package yt.bam.bamradio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import yt.bam.bamradio.managers.configurationmanager.ConfigurationManager;
import yt.bam.bamradio.managers.commandmanager.CommandManager;
import yt.bam.bamradio.managers.radiomanager.RadioManager;
import yt.bam.bamradio.managers.translationmanager.TranslationManager;
import org.mcstats.MetricsLite;


/**
 * @author fr34kyn01535
 */

public class BAMradio extends JavaPlugin {
    public static final Logger logger = Bukkit.getLogger();
    private ArrayList<IManager> managers = null;
    public static BAMradio Instance;
    
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
        RadioManager = (RadioManager) registerManager(new RadioManager(this,TranslationManager,ConfigurationManager.AutoPlay,ConfigurationManager.AutoPlayNext,ConfigurationManager.ForceSoftwareSequencer));
        CommandManager = (CommandManager) registerManager(new CommandManager(this,TranslationManager,new String[]{"bamradio","br"},"yt/bam/bamradio/managers/commandmanager/commands"));
        registerListener();
        
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
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
