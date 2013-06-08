package yt.bam.bamradio.managers.commandmanager;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.Helpers;

/**
 * @author fr34kyn01535
 */

public class CmdAbout implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            Helpers.sendMessage(sender, ChatColor.GREEN + "BAMradio by FR34KYN01535@bam.yt");
            Helpers.sendMessage(sender, ChatColor.GREEN + "Coded for BAMcraft (bam.yt)");
        }

	@Override
	public String getHelp() {
		return "Credits";
	}

	@Override
	public String getSyntax() {
		return "/br about";
	}

	@Override
	public Permission getPermissions() {
		return null;
	}
        
        @Override
	public String[] getName() {
		return new String[] {"about"};
	}
        
        @Override
        public String getExtendedHelp() {
            return null;
        }
}
