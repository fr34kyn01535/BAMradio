package yt.bam.bamradio.managers.commandmanager.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.Helpers;
import yt.bam.bamradio.managers.commandmanager.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdAbout implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            Helpers.sendMessage(sender, ChatColor.GREEN + "BAMradio "+ChatColor.WHITE +BAMradio.Instance.TranslationManager.getTranslation("COMMAND_ABOUT_BY")+ChatColor.GREEN +" FR34KYN01535@bam.yt");
            Helpers.sendMessage(sender, ChatColor.GREEN + "Coded "+ChatColor.WHITE +BAMradio.Instance.TranslationManager.getTranslation("COMMAND_ABOUT_FOR")+ChatColor.GREEN +" BAMcraft (bam.yt)");
        }

	@Override
	public String getHelp() {
		return BAMradio.Instance.TranslationManager.getTranslation("COMMAND_ABOUT_HELP");
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
        
        @Override
        public boolean allowedInConsole() {
            return true;
        }
}
