package yt.bam.bamradio.radiomanager;

import com.xxmicloxx.NoteBlockAPI.*;
import java.io.File;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import yt.bam.bamradio.BAMradio;
/**
 *
 * @author FR34KYN01535
 */
public class NoteBlockPlayer implements MidiPlayer, Listener{
    private RadioManager manager;
    public SongPlayer Player;
    
    public NoteBlockPlayer(RadioManager manager){
        this.manager=manager;
    }
    
    public void tuneIn(Player player) {
        if(Player!=null){
            Player.addPlayer(player);
        }
    }

    public void tuneOut(Player player) {
        if(Player!=null){
            Player.removePlayer(player);
        }
    }
    
    public void stopPlaying(){
        if(Player!=null){
            Player.setPlaying(false);
        }
        manager.nowPlaying = false;
    }
    
    public boolean playSong(String fileName){
        manager.nowPlayingFile=fileName;
        manager.nowPlaying = true;
        try{
            Song s = NBSDecoder.parse(new File(BAMradio.Instance.getDataFolder(),fileName+".nbs"));
            Player = new RadioSongPlayer(s);
            Player.setAutoDestroy(false);
            for (Player player : manager.tunedIn) {
                Player.addPlayer(player);
                manager.NowPlaying(player, false);
             }
            Player.setPlaying(true);
            return true;
        }
        catch(Exception e){
                System.err.println(BAMradio.Library.Translation.getTranslation("RADIO_MANAGER_CORRUPT_NBS")+" " + fileName+" ("+e.getMessage()+")");
        }
            return false;
    }
    

}
