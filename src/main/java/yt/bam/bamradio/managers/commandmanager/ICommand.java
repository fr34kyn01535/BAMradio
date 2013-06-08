package yt.bam.bamradio.managers.commandmanager;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

/**
 * @author fr34kyn01535
 */

public interface ICommand {
    public  void execute(CommandSender sender, String commandLabel, String[] args);

    public String getHelp(); //Short description
    
    public String getExtendedHelp(); //Additional help (Examples)

    public String getSyntax(); //Syntax with <required> parameters

    public Permission getPermissions(); //Required permission or null

    public String[] getName(); // name of permission 
}
