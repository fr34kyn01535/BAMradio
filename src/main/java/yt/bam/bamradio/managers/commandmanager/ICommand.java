package yt.bam.bamradio.managers.commandmanager;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

/**
 * @author fr34kyn01535
 * @version 1.1
 */

public interface ICommand {
    public  void execute(CommandSender sender, String commandLabel, String[] args);

    /**
     * @return short description
     */
    public String getHelp();
    
    /**
     * @return additional description (examples) or null
     */
    public String getExtendedHelp(); 

    /**
     * @return syntax with <required> and [possible] parameters
     */
    public String getSyntax();

    /**
     * @return required permission or null
     */
    public Permission getPermissions(); 
    
    /**
     * @return name of the command (/RootCommand <name>)
     */
    public String[] getName(); 
}
