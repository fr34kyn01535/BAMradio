package yt.bam.bamradio.managers.commandmanager.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.managers.commandmanager.ICommand;
import yt.bam.bamradio.managers.commandmanager.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdNext implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            if (BAMradio.Instance.MidiManager.MidiPlayer.isNowPlaying()) {
                BAMradio.Instance.MidiManager.MidiPlayer.stopPlaying();
            }
            BAMradio.Instance.MidiManager.MidiPlayer.playNextSong();
        }

	@Override
	public String getHelp() {
		return "Skip to next midi";
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
}
