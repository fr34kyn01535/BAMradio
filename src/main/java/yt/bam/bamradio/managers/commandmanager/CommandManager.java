package yt.bam.bamradio.managers.commandmanager;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import yt.bam.bamradio.Helpers;
import managers.IManager;

/**
 * @author fr34kyn01535
 */

public class CommandManager implements IManager {
    public static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;
    public static ArrayList<ICommand> AllCommands;

    public CommandManager(Plugin plugin){
        Plugin = plugin;
        //Commands
        AllCommands = new ArrayList<ICommand>();
        AllCommands.add(new CmdList());
        AllCommands.add(new CmdPlay());
        AllCommands.add(new CmdStop());
        AllCommands.add(new CmdNext());
        AllCommands.add(new CmdUnmute());
        AllCommands.add(new CmdMute());
        AllCommands.add(new CmdHelp());
        AllCommands.add(new CmdAbout());
    }
    
    public static boolean onCommand(CommandSender sender, org.bukkit.command.Command root, String commandLabel, String[] args) {
      if (root.getName().equalsIgnoreCase("bamradio") || root.getName().equalsIgnoreCase("br")) {

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
                Helpers.sendMessage(sender, ChatColor.RED + "Unknown command. Type \"/br help\" for help.");
                return true;
            }

            try {
                Permission permission = command.getPermissions();
                if(permission == null || hasPermission(sender,permission)){
                    command.execute(sender, commandLabel, args); // Execute
                    return true;
                }
            } catch (Exception e) {
                
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
        Helpers.sendMessage(player, ChatColor.RED + "You dont have the permission: ("+permission.toString()+")");                 
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
