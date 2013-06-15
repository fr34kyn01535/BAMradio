package yt.bam.bamradio.managers.radiomanager;

import com.xxmicloxx.NoteBlockAPI.*;
import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.Helpers;
import yt.bam.bamradio.PlayerListener;
/**
 *
 * @author FR34KYN01535
 */
public class NoteBlockPlayer implements MidiPlayer, Listener{
    private RadioManager manager;
    private Song s = null;
    
    public NoteBlockPlayer(RadioManager manager){
        this.manager=manager;
    }
    
    public void tuneIn(Player player) {
        if(s!=null){
            s.addPlayer(player);
        }
    }

    public void tuneOut(Player player) {
        if(s!=null){
            s.removePlayer(player);
        }
    }
    
    public void stopPlaying(){
        if(s!=null){
            s.setPlaying(false);
        }
        manager.nowPlaying = false;
    }
    
    public boolean playSong(String fileName){
        manager.nowPlayingFile=fileName;
        manager.nowPlaying = true;

        
        
        try{
            File nbs = manager.getNoteBlockFile(fileName);
            s = NBSDecoder.parse(nbs);
            s.setAutoDestroy(false);
            for (Player player : manager.tunedIn) {
                s.addPlayer(player);
                Helpers.sendMessage(player,manager.TranslationManager.getTranslation("MIDI_MANAGER_NOW_PLAYING")+" " + ChatColor.YELLOW + fileName.replace("_", " ").replace(".nbs",""));
            }
            s.setPlaying(true);
            return true;
        }
        catch(Exception e){
                System.err.println(manager.TranslationManager.getTranslation("RADIO_MANAGER_CORRUPT_NBS")+" " + fileName+" ("+e.getMessage()+")");
        }
            return false;
    }
    

}
