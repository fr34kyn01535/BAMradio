package yt.bam.bamradio;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import yt.bam.bamradio.managers.configurationmanager.ConfigurationManager;
import yt.bam.bamradio.managers.commandmanager.CommandManager;
import yt.bam.bamradio.managers.midimanager.MidiManager;
import yt.bam.bamradio.managers.translationmanager.TranslationManager;

/**
 * @author fr34kyn01535
 */

public class BAMradio extends JavaPlugin {
    public static final Logger logger = Bukkit.getLogger();
    private ArrayList<IManager> managers;
    public static BAMradio Instance;
    
    public ConfigurationManager ConfigurationManager;
    public CommandManager CommandManager;
    public TranslationManager TranslationManager;
    public MidiManager MidiManager;
   
    @Override
    public void onEnable() {
        BAMradio.Instance = this;
        managers = new ArrayList<IManager>();
        ConfigurationManager = (ConfigurationManager) registerManager(new ConfigurationManager(this));
        TranslationManager = (TranslationManager) registerManager(new TranslationManager(this,ConfigurationManager.Language));
        MidiManager = (MidiManager) registerManager(new MidiManager(this,TranslationManager,ConfigurationManager.AutoPlay,ConfigurationManager.AutoPlayNext,ConfigurationManager.ForceSoftwareSequencer));
        CommandManager = (CommandManager) registerManager(new CommandManager(this,TranslationManager,new String[]{"bamradio","br"},"yt/bam/bamradio/managers/commandmanager/commands"));
        registerListener();
    }
	
    @Override
    public void onDisable() {
        CommandManager.onDisable();
        MidiManager.onDisable();
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
