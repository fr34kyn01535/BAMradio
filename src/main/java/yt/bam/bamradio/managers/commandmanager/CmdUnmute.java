package yt.bam.bamradio.managers.commandmanager;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;

/**
 * @author fr34kyn01535
 */

public class CmdUnmute implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            BAMradio.Instance.MidiManager.MidiPlayer.tuneIn((Player) sender);
        }

	@Override
	public String getHelp() {
		return "Unmute BAMradio";
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
}
