package yt.bam.bamradio.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.library.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdUnmute implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            BAMradio.Instance.RadioManager.tuneOut((Player) sender);
            BAMradio.Instance.RadioManager.tuneIn((Player) sender);
        }

	@Override
	public String getHelp() {
		return BAMradio.Library.Translation.getTranslation("COMMAND_UNMUTE_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br unmute";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.mute");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"unmute"};
	}
        @Override
        public String getExtendedHelp() {
            return null;
        }
        @Override
        public boolean allowedInConsole() {
            return false;
        }
}
