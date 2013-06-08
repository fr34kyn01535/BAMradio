package yt.bam.bamradio.managers.translationmanager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.IManager;
import yt.bam.bamradio.managers.configurationmanager.ConfigurationManager;

/**
 * TranslationManager
 * @author FR34KYN01535
 * @version 1.1
 */

public class TranslationManager implements IManager {
    private static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;
    public ConfigurationManager ConfigurationManager;
    private Map<String,String> translation;
    private Map<String,String> defaultTranslation;
    private String selectedLanguage;
    private FileConfiguration loadedlanguage;
    
    public TranslationManager(Plugin plugin,ConfigurationManager configurationManager){
        Plugin = plugin;
        ConfigurationManager = configurationManager;
        if(ConfigurationManager.Language == null){
            selectedLanguage = "en";
        }else{
            File f = new File(Plugin.getDataFolder()+File.separator+ BAMradio.Instance.ConfigurationManager+".yml");
            if(f.exists()) {
                this.selectedLanguage=ConfigurationManager.Language;
            }else{
                selectedLanguage = "en";
                logger.warning("Languagefile "+ConfigurationManager.Language+".yml not found, falling back to english language!");
            }
        }
        reloadTranslation();
    }
    public void reloadTranslation(){
        if(selectedLanguage.equals("en")){
            loadDefaults();
        }else{
            File languageFile = new File(Plugin.getDataFolder()+File.separator+selectedLanguage+".yml");
            loadedlanguage = YamlConfiguration.loadConfiguration(languageFile);
            loadTranslation();
        }
    }
    public String getTranslation(String key){
        if(translation!=null){
            String value = translation.get("translation."+key);
            if(value==null || value.isEmpty()){
                return "Translation missing: "+key;
            }else{
                return value;
            }
        }
        else{
            return defaultTranslation.get(key);
        }
    }
    private void loadTranslation(){
        translation = new HashMap<String, String>();
        for(Map.Entry<String,Object> entry : loadedlanguage.getValues(true).entrySet()){
            translation.put(entry.getKey(),entry.getValue().toString());
        }
    }
    private void loadDefaults(){
        defaultTranslation = new HashMap<String, String>();
        defaultTranslation.put("BAN_LIST_READ_ERR","Error occurred while attempting to read banned-ips.txt!");
    }

    public void onEnable() {
    //
    }

    public void onDisable() {
     //
    }
    
}
