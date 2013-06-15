package yt.bam.bamradio.managers.radiomanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.Helpers;
import yt.bam.bamradio.IManager;
import yt.bam.bamradio.managers.translationmanager.TranslationManager;

/**
 * @author fr34kyn01535
 */

public class RadioManager implements IManager {
    public static final Logger logger = Bukkit.getLogger();
    public static Plugin Plugin;
    public TranslationManager TranslationManager;
    public boolean AutoPlay;
    public boolean AutoPlayNext;
    
    public List<Player> tunedIn;
    public boolean nowPlaying = false;
    public String nowPlayingFile = "";
    public int nowPlayingIndex = 0;
    
    public boolean ForceSoftwareSequencer;
    private MidiPlayer MidiPlayer;
    private NoteBlockPlayer NoteBlockPlayer;
    
    public RadioManager(Plugin plugin,TranslationManager translationManager,boolean autoPlay,boolean autoPlayNext,boolean forceSoftwareSequencer) {
        Plugin = plugin;
        AutoPlay = autoPlay;
        AutoPlayNext = autoPlayNext;
        TranslationManager = translationManager;
        ForceSoftwareSequencer = forceSoftwareSequencer;
        tunedIn = new ArrayList<Player>();
        
        if(BAMradio.Instance.NoteBlockAPI){
            NoteBlockPlayer = new NoteBlockPlayer(this);
            BAMradio.Instance.getServer().getPluginManager().registerEvents(new SongListener(), BAMradio.Instance);
        }else{
            NoteBlockPlayer=null;
        }       
    
    }

    public void tuneIn(Player player) {
        tunedIn.add(player);
        if(NoteBlockPlayer!=null){
            NoteBlockPlayer.tuneIn(player);
        }
        if(nowPlaying){
            Helpers.sendMessage(player,TranslationManager.getTranslation("MIDI_MANAGER_NOW_PLAYING")+" " + ChatColor.YELLOW + nowPlayingFile.replace("_", " "));
        }
    }

    public void tuneOut(Player player) {
        tunedIn.remove(player);
        if(NoteBlockPlayer!=null){
            NoteBlockPlayer.tuneOut(player);
        }
    }
    
    
    public boolean isNowPlaying() {
        return nowPlaying;
    }
    
    
    public void playNextSong() {
        nowPlayingIndex++;
        String[] midiFileNames = listRadioFiles();
        if(nowPlayingIndex >= midiFileNames.length)
            nowPlayingIndex = 0;
        playSong(midiFileNames[nowPlayingIndex]);
    }
    
    public boolean playSong(String fileName) {
        if(nowPlaying){
            stopPlaying();
        }
        if(MidiPlayer!=null){
            File file = getMidiFile(fileName);
            if(file!=null){
                return MidiPlayer.playSong(fileName);
            }
            if(NoteBlockPlayer!=null){
                file = getNoteBlockFile(fileName);
                if(file!=null){
                    return NoteBlockPlayer.playSong(fileName);
                }
            }
        }
        return false;
    }   
    public void stopPlaying(){
        MidiPlayer.stopPlaying();
        if(NoteBlockPlayer!=null)NoteBlockPlayer.stopPlaying();
    }
    
    public File getMidiFile(String fileName) {
        File midiFile = new File(Plugin.getDataFolder(), fileName.replace(".mid", "") + ".mid");
        if (!midiFile.exists())
                return null;
        return midiFile;
    }
    public File getNoteBlockFile(String fileName) {
        File noteBlockFile = new File(Plugin.getDataFolder(), fileName.replace(".nbs", "") + ".nbs");
        if (!noteBlockFile.exists())
                return null;
        return noteBlockFile;
    }
    public static String[] listRadioFiles() {
        File[] files = Plugin.getDataFolder().listFiles();
        List<String> radioFiles = new ArrayList<String>();
        for (File file : files) {
                if (file.getName().endsWith(".mid")) {
                    radioFiles.add(file.getName().substring(0, file.getName().lastIndexOf(".mid")));
                }
                if (BAMradio.Instance.NoteBlockAPI&&file.getName().endsWith(".nbs")) {
                    radioFiles.add(file.getName().substring(0, file.getName().lastIndexOf(".nbs")));
                }
        }
        return radioFiles.toArray(new String[0]);
    }	
    public static String[] listRadioFilesWithExtensions() {
        File[] files = Plugin.getDataFolder().listFiles();
        List<String> radioFiles = new ArrayList<String>();
        for (File file : files) {
                if (file.getName().endsWith(".mid")) {
                    radioFiles.add(file.getName());
                }
                if (BAMradio.Instance.NoteBlockAPI&&file.getName().endsWith(".nbs")) {
                    radioFiles.add(file.getName());
                }
        }
        return radioFiles.toArray(new String[0]);
    }	
    
    public void onEnable() {
        if(ForceSoftwareSequencer){
            MidiPlayer = new MinecraftMidiPlayer(this);
        }else{
            try {
                MidiPlayer = new SequencerMidiPlayer(this);
            } catch (MidiUnavailableException ex) {
                logger.severe(TranslationManager.getTranslation("MIDI_MANAGER_EXCEPTION_MIDI_UNAVAILABLE"));
                MidiPlayer = new MinecraftMidiPlayer(this);
            }
        }
        Player[] onlinePlayerList = Plugin.getServer().getOnlinePlayers();
        for(Player player : onlinePlayerList){
            tuneIn(player);
        }
        if(AutoPlay){
            String[] midis = listRadioFiles();
            if (midis.length > 0)
                playSong(midis[0]);
        }
    }

    public void onDisable() {
        stopPlaying();
    }
}
