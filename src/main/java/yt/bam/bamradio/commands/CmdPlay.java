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

public class CmdPlay implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            try{
                if(args.length==1){
                    CmdList list = new CmdList();
                    list.execute(sender, commandLabel, args);
                }else{
                    if (BAMradio.Instance.RadioManager.isNowPlaying()) {
                        BAMradio.Instance.RadioManager.stopPlaying();
                    }
                    if(isInteger(args[1])){
                        int index = Integer.parseInt(args[1]); 
                        String [] fileList = BAMradio.Instance.RadioManager.listRadioFiles();
                        if(index<fileList.length){
                            BAMradio.Instance.RadioManager.playSong(fileList[index]);
                        }else{    
                            Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_PLAY_EXCEPTION_NOT_FOUND")+" \""+args[1]+"\"");  
                        }
                    }else{ 
                        if(!BAMradio.Instance.RadioManager.playSong(args[1])){
                            Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_PLAY_EXCEPTION_NOT_FOUND")+" \""+args[1]+"\"");
                        }
                    }
                }
            }catch (Exception e){
                Helpers.sendMessage(sender, ChatColor.RED + e.getMessage());
            }
        }
        
        public boolean isInteger( String input )  
        {  
           try  
           {  
              Integer.parseInt( input );  
              return true;  
           }  
           catch(Exception ex)  
           {  
              return false;  
           }  
        }  
       
	@Override
	public String getHelp() {
		return BAMradio.Library.Translation.getTranslation("COMMAND_PLAY_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br play <name|index>";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.play");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"play"};
	}
        @Override
        public String getExtendedHelp() {
            return BAMradio.Library.Translation.getTranslation("COMMAND_PLAY_EXTENDED_HELP");
        }
        @Override
        public boolean allowedInConsole() {
            return true;
        }
}
