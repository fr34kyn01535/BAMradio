package yt.bam.bamradio;

import managers.IManager;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import yt.bam.bamradio.managers.configurationmanager.ConfigurationManager;
import yt.bam.bamradio.managers.commandmanager.CommandManager;
import yt.bam.bamradio.managers.midimanager.MidiManager;

/**
 * @author fr34kyn01535
 */

public class BAMradio extends JavaPlugin {
    public static final Logger logger = Bukkit.getLogger();
    private ArrayList<IManager> managers = new ArrayList<IManager>();
    public static BAMradio Instance;
    
    public ConfigurationManager ConfigurationManager;
    public CommandManager CommandManager;
    public MidiManager MidiManager;
    
    @Override
    public void onEnable() {
        BAMradio.Instance = this;
        registerListener();
        
        ConfigurationManager = (ConfigurationManager) registerManager(new ConfigurationManager(this));
        CommandManager = (CommandManager) registerManager(new CommandManager(this));
        MidiManager = (MidiManager) registerManager(new MidiManager(this));
        
        for(IManager manager : managers){
            manager.onEnable();
        }
    }
	
    @Override
    public void onDisable() {
        for(IManager manager : managers){
            manager.onDisable();
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command root, String commandLabel, String[] args) {
        return CommandManager.onCommand(sender, root,commandLabel,args);
    }
    
    private IManager registerManager(IManager manager){
        managers.add(manager);
        return manager;
    }
    
    private void registerListener(){
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }
    
}
