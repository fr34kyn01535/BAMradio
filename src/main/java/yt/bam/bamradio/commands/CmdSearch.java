package yt.bam.bamradio.commands;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
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

public class CmdSearch implements ICommand{
        public static final Logger logger = Bukkit.getLogger();
	@Override
	public void execute(CommandSender sender, String commandLabel, String[] args) {
            try{
                String url = "http://radio.bam.yt/?f=json";
                if(args.length>=2){
                        if(!BAMradio.Instance.NoteBlockAPI){
                            url+="&type=mid";
                        }else{
                            url+="&type=any";
                        }
                        url+="&q=";
                        for(int i = 1;i<args.length;i++){
                            url+=args[i]+"%20";
                        }
                        url=url.substring(0,url.length()-3);

                       JSONArray json = readJsonFromUrl(url);
                    if(json!=null){
                        Helpers.sendMessage(sender,ChatColor.GREEN + BAMradio.Library.Translation.getTranslation("COMMAND_SEARCH_TITLE"));

                        for (int i = 0; i < json.length(); i++) {
                            JSONObject row = json.getJSONObject(i);

                            String suffix="";
                            if(row.getString("type").toLowerCase().equals("mid")){
                                suffix = ChatColor.DARK_BLUE+row.getString("type").toUpperCase()+" ";
                            }
                            if(row.getString("type").toLowerCase().equals("nbs")){
                                suffix = ChatColor.DARK_GREEN+row.getString("type").toUpperCase()+" ";
                            }

                            sender.sendMessage(ChatColor.GREEN + "["+ChatColor.BOLD+row.getInt("id")+ChatColor.RESET+""+ChatColor.GREEN+"] "+suffix+ChatColor.RESET+ row.getString("artist") +" - "+row.getString("title"));
                        }
                    }
                }else{   
                    Helpers.sendMessage(sender, ChatColor.RED + BAMradio.Library.Translation.getTranslation("COMMAND_MANAGER_INVALID_PARAMETER"));
                }
            }catch (Exception e){
                Helpers.sendMessage(sender, ChatColor.RED + e.getMessage());
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
          }
            catch (Exception e){
                logger.log(Level.SEVERE,e.getMessage());
                return null;
            }
           finally {
            is.close();
          }
        }
       
	@Override
	public String getHelp() {
		return BAMradio.Library.Translation.getTranslation("COMMAND_SEARCH_HELP");
	}

	@Override
	public String getSyntax() {
		return "/br search <name>";
	}

	@Override
	public Permission getPermissions() {
		return new Permission("bamradio.search");
	}
        
        @Override
	public String[] getName() {
		return new String[] {"search"};
	}
        @Override
        public String getExtendedHelp() {
            return BAMradio.Library.Translation.getTranslation("COMMAND_SEARCH_EXTENDED_HELP");
        }
        @Override
        public boolean allowedInConsole() {
            return true;
        }
}
