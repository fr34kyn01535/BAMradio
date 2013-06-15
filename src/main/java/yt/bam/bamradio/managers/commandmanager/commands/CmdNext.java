package yt.bam.bamradio.managers.commandmanager.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.managers.commandmanager.ICommand;
import yt.bam.bamradio.managers.radiomanager.MidiPlayer;

/**
 * @author fr34kyn01535
 */

public class CmdNext implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            if (BAMradio.Instance.getRadioManager().isNowPlaying()) {
                BAMradio.Instance.getRadioManager().stopPlaying();
            }
            BAMradio.Instance.getRadioManager().playNextSong();
        }

	@Override
	public String getHelp() {
		return BAMradio.Instance.getTranslationManager().getTranslation("COMMAND_NEXT_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br next";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.next");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"next"};
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
