package yt.bam.bamradio.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.library.Helpers;
import yt.bam.library.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdInfo implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            if(BAMradio.Instance.RadioManager.isNowPlaying()) {
                BAMradio.Instance.RadioManager.NowPlaying(sender,true);
            }
        }

	@Override
	public String getHelp() {
		return BAMradio.Library.Translation.getTranslation("COMMAND_INFO_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br info";
	}

	@Override
	public Permission getPermissions() {
		return null;
	}
        
        @Override
	public String[] getName() {
		return new String[] {"info"};
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
