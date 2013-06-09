package yt.bam.bamradio.managers.commandmanager.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.managers.commandmanager.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdHelp implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            sender.sendMessage(ChatColor.BOLD+""+ChatColor.GREEN+"############   BAMradio "+BAMradio.Instance.getDescription().getVersion()+" by FR34KYN01535   ##########");
            for(ICommand cmd : BAMradio.Instance.CommandManager.AllCommands){
                if(cmd.getPermissions() == null || sender.hasPermission(cmd.getPermissions())){
                    sender.sendMessage(ChatColor.BOLD +cmd.getSyntax()+" - "+cmd.getHelp());
                }
                if(cmd.getExtendedHelp()!=null){
                    sender.sendMessage(ChatColor.GRAY+""+ ChatColor.ITALIC+cmd.getExtendedHelp());
                }
            }
            sender.sendMessage(ChatColor.BOLD+""+ChatColor.GREEN+ "#####################################################"); 
            
        }

	@Override
	public String getHelp() {
		return BAMradio.Instance.TranslationManager.getTranslation("COMMAND_HELP_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br help";
	}

	@Override
	public Permission getPermissions() {
		return null;
	}
        
        @Override
	public String[] getName() {
		return new String[] {"help"};
	}
        @Override
        public String getExtendedHelp() {
            return null;
        }
}
