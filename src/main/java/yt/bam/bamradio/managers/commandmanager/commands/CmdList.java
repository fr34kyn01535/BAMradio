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

public class CmdList implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            Helpers.sendMessage(sender,ChatColor.GREEN + BAMradio.Instance.TranslationManager.getTranslation("COMMAND_LIST_TITLE"));
            String[] fileList = BAMradio.Instance.MidiManager.listMidiFiles();
            int i = 0;
            for (String name : fileList) {
                sender.sendMessage(ChatColor.GREEN + "["+ChatColor.BOLD+i+ChatColor.RESET+""+ChatColor.GREEN+"] "+ChatColor.RESET+ name);
                i++;
            }
        }

	@Override
	public String getHelp() {
		return BAMradio.Instance.TranslationManager.getTranslation("COMMAND_LIST_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br list";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.list");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"list"};
	}
        @Override
        public String getExtendedHelp() {
            return null;
        }
}
