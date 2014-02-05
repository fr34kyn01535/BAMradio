package yt.bam.bamradio.commands;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.json.*;
import yt.bam.bamradio.BAMradio;
import yt.bam.library.Helpers;
import yt.bam.library.ICommand;

/**
 * @author fr34kyn01535
 */

public class CmdGet implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            try{
                if(args.length>=2 && isInteger(args[1])){
                    int id = Integer.parseInt(args[1]); 
                    JSONArray json = readJsonFromUrl("http://radio.bam.yt/?f=json&id="+id);
                    
                    if(json.length()==1){
                        JSONObject row = json.getJSONObject(0);
                        String filename = row.getString("filename");
                        
                        String surl = "http://radio.bam.yt/?f=download&name=" +filename;
                
                        File file = new File(BAMradio.Instance.getDataFolder()+File.separator+filename);
                        
                        if(!file.exists() && (surl.endsWith(".mid") || surl.endsWith(".midi") || surl.endsWith(".nbs"))){
                            URL url = new URL(surl);
                            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                            FileOutputStream fos = new FileOutputStream(BAMradio.Instance.getDataFolder()+File.separator+filename);
                            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        }else{
                            Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_MANAGER_INVALID_PARAMETER"));
                        }
                        
                        if(!BAMradio.Instance.RadioManager.playSong(filename)){
                            Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_PLAY_EXCEPTION_NOT_FOUND")+" \""+args[1]+"\"");
                        } 
                    }else{
                       Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_GET_NOT_FOUND"));                 
                    }
                }else{
                    Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_MANAGER_INVALID_PARAMETER"));                 
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
        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
              sb.append((char) cp);
            }
            return sb.toString();
        }

        public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
          try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd).trim();
            JSONArray json = new JSONArray(jsonText);
            return json;
          } finally {
            is.close();
          }
        }
       
	@Override
	public String getHelp() {
		return BAMradio.Library.Translation.getTranslation("COMMAND_GET_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br get <id>";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.get");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"get"};
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
