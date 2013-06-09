package yt.bam.bamradio.managers.midimanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.IManager;
import yt.bam.bamradio.managers.configurationmanager.ConfigurationManager;
import yt.bam.bamradio.managers.translationmanager.TranslationManager;

/**
 * @author fr34kyn01535
 */

public class MidiManager implements IManager {
    public static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;
    public TranslationManager TranslationManager;
    public ConfigurationManager ConfigurationManager;
    public MidiPlayer MidiPlayer;
    
    public MidiManager(Plugin plugin,TranslationManager translationManager,ConfigurationManager configurationManager) {
        this.Plugin = plugin;
        ConfigurationManager = configurationManager;
        TranslationManager = translationManager;
        try {
            MidiPlayer = new SequencerMidiPlayer(this);
        } catch (MidiUnavailableException ex) {
            logger.severe(TranslationManager.getTranslation("MIDI_MANAGER_EXCEPTION_MIDI_UNAVAILABLE"));
            MidiPlayer = new MinecraftMidiPlayer(this);
        }
        Player[] onlinePlayerList = plugin.getServer().getOnlinePlayers();
        for(Player player : onlinePlayerList){
        if (MidiPlayer != null)
            MidiPlayer.tuneIn(player);
        }
        if(ConfigurationManager.AutoPlay){
            String[] midis = listMidiFiles();
            if (midis.length > 0)
                MidiPlayer.playSong(midis[0]);
        }
    }

    public File getMidiFile(String fileName) {
        File midiFile = new File(Plugin.getDataFolder(), fileName.replace(".mid", "") + ".mid");
        if (!midiFile.exists())
                return null;
        return midiFile;
    }
	
    public static String[] listMidiFiles() {
        File[] files = BAMradio.Instance.getDataFolder().listFiles();
        List<String> midiFiles = new ArrayList<String>();
        for (File file : files) {
                if (file.getName().endsWith(".mid")) {
                    midiFiles.add(file.getName().substring(0, file.getName().lastIndexOf(".mid")));
                }
        }
        return midiFiles.toArray(new String[0]);
    }	

    public void onEnable() {
    //
    }

    public void onDisable() {
        MidiPlayer.stopPlaying();
    }
}
