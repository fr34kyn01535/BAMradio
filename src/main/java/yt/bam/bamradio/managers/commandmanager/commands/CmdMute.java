package yt.bam.bamradio.managers.commandmanager.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.Helpers;
import yt.bam.bamradio.managers.commandmanager.ICommand;
import yt.bam.bamradio.managers.commandmanager.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdMute implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            BAMradio.Instance.MidiManager.MidiPlayer.tuneOut((Player) sender);
            Helpers.sendMessage(sender,"Muted BAMradio.");
        }

	@Override
	public String getHelp() {
		return "Mute BAMradio";
	}

	@Override
	public String getSyntax() {
		return "/br mute";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.mute");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"mute"};
	}
        @Override
        public String getExtendedHelp() {
            return null;
        }
}
