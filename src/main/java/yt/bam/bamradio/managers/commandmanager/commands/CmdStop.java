package yt.bam.bamradio.managers.commandmanager.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.Helpers;
import yt.bam.bamradio.managers.commandmanager.ICommand;
import yt.bam.bamradio.managers.commandmanager.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdStop implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            if(BAMradio.Instance.MidiManager.MidiPlayer.isNowPlaying()) {
                BAMradio.Instance.MidiManager.MidiPlayer.stopPlaying();
                Helpers.sendMessage(sender,"Stopped playing...");
            }
            
        }

	@Override
	public String getHelp() {
		return "Stop a midi";
	}

	@Override
	public String getSyntax() {
		return "/br stop";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.stop");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"stop"};
	}
        @Override
        public String getExtendedHelp() {
            return null;
        }
}
