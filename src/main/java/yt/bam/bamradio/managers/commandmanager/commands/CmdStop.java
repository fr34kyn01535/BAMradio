package yt.bam.bamradio.managers.commandmanager.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.Helpers;
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
                Helpers.sendMessage(sender,BAMradio.Instance.TranslationManager.getTranslation("COMMAND_STOP_MESSAGE"));
            }
            
        }

	@Override
	public String getHelp() {
		return BAMradio.Instance.TranslationManager.getTranslation("COMMAND_STOP_HELP");
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
