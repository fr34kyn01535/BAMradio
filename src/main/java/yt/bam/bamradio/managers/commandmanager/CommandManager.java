package yt.bam.bamradio.managers.commandmanager;

import yt.bam.bamradio.managers.commandmanager.commands.CmdHelp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.libs.jline.internal.Log.Level;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import yt.bam.bamradio.Helpers;
import yt.bam.bamradio.IManager;
import yt.bam.bamradio.managers.commandmanager.commands.*;
import yt.bam.bamradio.managers.translationmanager.TranslationManager;

/**
 * CommandManager
 * @author FR34KYN01535
 * @version 1.1
 */

public class CommandManager implements IManager {
    public static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;
    public static TranslationManager TranslationManager;
    
    public static ArrayList<ICommand> AllCommands;
    public static ArrayList<String> RootCommands = new ArrayList<String>();
    
    Set<Class<? extends ICommand>> commandClasses = new HashSet<Class<? extends ICommand>>();

    public CommandManager(Plugin plugin,TranslationManager translationManager,String[] rootCommands,String commandPath){
        Plugin = plugin;
        TranslationManager = translationManager;
        for(String rootCommand :rootCommands) {
            RootCommands.add(rootCommand.toLowerCase());
        }
        //Commands
        AllCommands = new ArrayList<ICommand>();
        
         Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner()).setUrls(ClasspathHelper.forPackage(commandPath)));
        commandClasses = reflections.getSubTypesOf(ICommand.class);
 
        for (Class<? extends ICommand> c :commandClasses){
            try {
                AllCommands.add((ICommand)c.newInstance());
            } catch (InstantiationException ex) {
                Logger.getLogger(CommandManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CommandManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    public static boolean onCommand(CommandSender sender, org.bukkit.command.Command root, String commandLabel, String[] args) {
      if (RootCommands.contains(root.getName().toLowerCase())) {

            ICommand command=null;
            if(args.length==0){
                command = new CmdHelp();
            }else{
                 for(ICommand cmd : AllCommands){
                    String[] cmdargs = cmd.getName();
                    if(args.length >= cmdargs.length){
                        int index = 0;
                        boolean found = true; 
                        for(String cmdarg : cmdargs){
                            if(!cmdarg.equals(args[index])){
                                found =false;
                            }
                        }
                        if(found){
                            command=cmd;
                            break;
                        }
                    }
                }
            }
            if (command == null) {
                Helpers.sendMessage(sender, ChatColor.RED + TranslationManager.getTranslation("COMMAND_MANAGER_UNKNOWN_COMMAND"));
                return true;
            }

            try {
                Permission permission = command.getPermissions();
                if(permission == null || hasPermission(sender,permission)){
                    command.execute(sender, commandLabel, args); // Execute
                    return true;
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            } finally {
                return true;
            }
        }

        return false;
    }
    
    public static boolean hasPermission(CommandSender player, Permission permission){
    if(player.hasPermission(permission)){
        return true;
    }else{    
        Helpers.sendMessage(player, ChatColor.RED + TranslationManager.getTranslation("COMMAND_MANAGER_NO_PERMISSION")+ " ("+permission.getName()+")");                 
        return false;
        }
    }
    
    public void onEnable() {
    //
    }

    public void onDisable() {
    //
    }
}
