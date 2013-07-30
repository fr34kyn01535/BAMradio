package yt.bam.bamradio.managers.commandmanager;

import yt.bam.bamradio.managers.commandmanager.commands.CmdHelp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import yt.bam.bamradio.Helpers;
import yt.bam.bamradio.managers.IManager;
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
    public static ArrayList<String> RootCommands;
    public String CommandPath;
    
    public static ArrayList<ICommand> AllCommands;
    Set<Class<? extends ICommand>> commandClasses = new HashSet<Class<? extends ICommand>>();

    public CommandManager(Plugin plugin,TranslationManager translationManager,String[] rootCommands,String commandPath){
        Plugin = plugin;
        TranslationManager = translationManager;
        RootCommands = new ArrayList<String>();
        CommandPath = commandPath;
        
        for(String rootCommand :rootCommands) {
            RootCommands.add(rootCommand.toLowerCase());
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
                    if(!command.allowedInConsole() && !(sender instanceof Player)){
                        Helpers.sendMessage(sender, ChatColor.RED + TranslationManager.getTranslation("COMMAND_MANAGER_ONLY_CHAT"));
                    }else{
                        command.execute(sender, commandLabel, args); // Execute
                    }
                    return true;
                }
            } catch (Exception e) {
                sender.sendMessage(commandLabel);
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
        AllCommands = new ArrayList<ICommand>();
        AllCommands.add(new CmdAbout());
        AllCommands.add(new CmdGet());
        AllCommands.add(new CmdHelp());
        AllCommands.add(new CmdList());
        AllCommands.add(new CmdMute());
        AllCommands.add(new CmdNext());
        AllCommands.add(new CmdPlay());
        AllCommands.add(new CmdRandom());
        AllCommands.add(new CmdSearch());
        AllCommands.add(new CmdStop());
        AllCommands.add(new CmdUnmute());
        
        //Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner()).setUrls(ClasspathHelper.forPackage(CommandPath)));
        //commandClasses = reflections.getSubTypesOf(ICommand.class);
        //logger.info("["+Plugin.getDescription().getName()+"] CommandManager: "+commandClasses.size()+" commands found!");
        //for (Class<? extends ICommand> c :commandClasses){
        //    try {
        //        AllCommands.add((ICommand)c.newInstance());
        //    } catch (InstantiationException ex) {
        //        logger.log(java.util.logging.Level.SEVERE, null, ex);
        //    } catch (IllegalAccessException ex) {
        //        logger.log(java.util.logging.Level.SEVERE, null, ex);
        //    }
        //}
    }

    public void onDisable() {
    //
    }
}
