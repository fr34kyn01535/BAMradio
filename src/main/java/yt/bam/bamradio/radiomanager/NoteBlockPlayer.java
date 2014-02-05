package yt.bam.bamradio.radiomanager;

import com.xxmicloxx.NoteBlockAPI.*;
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import yt.bam.bamradio.BAMradio;
import yt.bam.library.Helpers;
/**
 *
 * @author FR34KYN01535
 */
public class NoteBlockPlayer implements MidiPlayer, Listener{
    private RadioManager manager;
    public static Song CurrentSong = null;
    
    public NoteBlockPlayer(RadioManager manager){
        this.manager=manager;
    }
    
    public void tuneIn(Player player) {
        if(CurrentSong!=null){
            CurrentSong.addPlayer(player);
        }
    }

    public void tuneOut(Player player) {
        if(CurrentSong!=null){
            CurrentSong.removePlayer(player);
        }
    }
    
    public void stopPlaying(){
        if(CurrentSong!=null){
            CurrentSong.setPlaying(false);
        }
        manager.nowPlaying = false;
    }
    
    public boolean playSong(String fileName){
        manager.nowPlayingFile=fileName;
        manager.nowPlaying = true;
        try{
            File nbs = manager.getNoteBlockFile(fileName);
            CurrentSong = NBSDecoder.parse(nbs);
            CurrentSong.setAutoDestroy(false);
            System.out.println(CurrentSong.getVolume());
            for (Player player : manager.tunedIn) {
                CurrentSong.addPlayer(player);
                manager.NowPlaying(player, false);
             }
            CurrentSong.setPlaying(true);
            return true;
        }
        catch(Exception e){
                System.err.println(BAMradio.Library.Translation.getTranslation("RADIO_MANAGER_CORRUPT_NBS")+" " + fileName+" ("+e.getMessage()+")");
        }
            return false;
    }
    

}
