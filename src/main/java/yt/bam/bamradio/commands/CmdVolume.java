package yt.bam.bamradio.commands;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import yt.bam.bamradio.BAMradio;
import yt.bam.library.Helpers;
import yt.bam.library.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdVolume implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            if(args.length==2){
                try{
                    BAMradio.Instance.RadioManager.Volume = Integer.parseInt(args[1].toString());   
                    Helpers.sendMessage(sender,ChatColor.GREEN + BAMradio.Library.Translation.getTranslation("COMMAND_VOLUME_CHANGED")+args[1].toString());
                }catch(Exception e){
                    Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_MANAGER_INVALID_PARAMETER")+" \""+args[1]+"\"");
                }
            }
         }

	@Override
	public String getHelp() {
		return BAMradio.Instance.Library.Translation.getTranslation("COMMAND_VOLUME_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br volume <volume>";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.volume");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"volume"};
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
