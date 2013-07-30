package yt.bam.bamradio.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.library.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdRandom implements ICommand{
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            if (BAMradio.Instance.RadioManager.isNowPlaying()) {
                BAMradio.Instance.RadioManager.stopPlaying();
            }
            BAMradio.Instance.RadioManager.playRandomSong();
        }

	@Override
	public String getHelp() {
		return BAMradio.Library.Translation.getTranslation("COMMAND_RANDOM_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br random";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.play");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"random"};
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
