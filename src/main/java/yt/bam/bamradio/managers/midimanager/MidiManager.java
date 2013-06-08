package yt.bam.bamradio.managers.midimanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import managers.IManager;
import yt.bam.bamradio.BAMradio;

/**
 * @author fr34kyn01535
 */

public class MidiManager implements IManager {
    public static final Logger logger = Bukkit.getLogger();
    public Plugin Plugin;
    public MidiPlayer MidiPlayer;
    
    public MidiManager(Plugin plugin) {
        this.Plugin=plugin;
        try {
            MidiPlayer = new SequencerMidiPlayer(this);
            logger.info("Sequencer device obtained!");
        } catch (MidiUnavailableException ex) {
            logger.severe("Could not obtain sequencer device - Falling back.");
            MidiPlayer = new MinecraftMidiPlayer(this);
        }
        Player[] onlinePlayerList = plugin.getServer().getOnlinePlayers();
        for(Player player : onlinePlayerList){
        if (MidiPlayer != null)
            MidiPlayer.tuneIn(player);
        }
        if(BAMradio.Instance.ConfigurationManager.AutoPlay){
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
	
    public String[] listMidiFiles() {
            File[] files = Plugin.getDataFolder().listFiles();
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
