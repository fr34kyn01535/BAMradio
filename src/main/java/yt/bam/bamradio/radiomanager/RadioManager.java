package yt.bam.bamradio.radiomanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiUnavailableException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yt.bam.bamradio.BAMradio;
import yt.bam.library.Helpers;

/**
 * @author fr34kyn01535
 */

public class RadioManager {
    public static final Logger logger = Bukkit.getLogger();
    public boolean AutoPlay;
    public boolean AutoPlayNext;
    public String RegionName = "";
    public int Volume = 0;
    
    public List<Player> tunedIn;
    public boolean nowPlaying = false;
    public String nowPlayingFile = "";
    public int nowPlayingIndex = 0;
    
    public boolean ForceSoftwareSequencer;
    private MidiPlayer MidiPlayer;
    private NoteBlockPlayer NoteBlockPlayer;
    
    public RadioManager(boolean autoPlay,boolean autoPlayNext,boolean forceSoftwareSequencer,String regionName,int volume) {
        AutoPlay = autoPlay;
        AutoPlayNext = autoPlayNext;
        ForceSoftwareSequencer = forceSoftwareSequencer;
        RegionName = regionName;
        Volume = volume;
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
        NowPlaying(player,false);
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
    
    public void playRandomSong() {
        String[] midiFileNames = listRadioFiles();
        nowPlayingIndex = (int)(Math.random() * (midiFileNames.length + 1));
        if(nowPlayingIndex >= midiFileNames.length)
            nowPlayingIndex = 0;
        playSong(midiFileNames[nowPlayingIndex]);
    }    
             
    public boolean playSong(String fileName){
        if(nowPlaying){
            stopPlaying();
        }
            File file = getMidiFile(fileName);
            if(file!=null){
                if(MidiPlayer!=null){
                    return MidiPlayer.playSong(fileName);
                }
            }
            file = getNoteBlockFile(fileName);
            if(file!=null){
                if(NoteBlockPlayer!=null){
                    return NoteBlockPlayer.playSong(fileName);
                }else{
                    BAMradio.Instance.getLogger().log(Level.WARNING ,"NoteBlockAPI not found, can not play NBS file!");
                }
            }
        return false;
    }   
    
    public void stopPlaying(){
        MidiPlayer.stopPlaying();
        if(NoteBlockPlayer!=null)NoteBlockPlayer.stopPlaying();
    }
    
    public File getMidiFile(String fileName) {
        File midiFile = new File(BAMradio.Instance.getDataFolder(), fileName.replace(".mid", "") + ".mid");
        if (!midiFile.exists())
                return null;
        return midiFile;
    }
    
    public File getNoteBlockFile(String fileName) {
        File noteBlockFile = new File(BAMradio.Instance.getDataFolder(), fileName.replace(".nbs", "") + ".nbs");
        if (!noteBlockFile.exists())
                return null;
        return noteBlockFile;
    }
    
    public static String[] listRadioFiles() {
        File[] files = BAMradio.Instance.getDataFolder().listFiles();
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
        File[] files = BAMradio.Instance.getDataFolder().listFiles();
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
                logger.severe(BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_EXCEPTION_MIDI_UNAVAILABLE"));
                MidiPlayer = new MinecraftMidiPlayer(this);
            }
        }
        Player[] onlinePlayerList = BAMradio.Instance.getServer().getOnlinePlayers();
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

    public void NowPlaying(CommandSender player,boolean force) {
        if(!force){
            if(!BAMradio.Library.Configuration.getBoolean("show-current-track", true)){
                return;
            }
        }
        if(nowPlaying){
            Helpers.sendMessage(player,BAMradio.Library.Translation.getTranslation("MIDI_MANAGER_NOW_PLAYING")+" " + ChatColor.YELLOW + nowPlayingFile.replace("_", " "));
        }
    }
}
