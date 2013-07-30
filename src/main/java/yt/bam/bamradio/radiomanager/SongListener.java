package yt.bam.bamradio.radiomanager;

import com.xxmicloxx.NoteBlockAPI.SongEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import yt.bam.bamradio.BAMradio;

/**
 * @author FR34KYN01535
 */
public class SongListener implements Listener {
    @EventHandler
    public static void onSongEnd(SongEndEvent e)
    {  
          if(BAMradio.Instance.RadioManager!=null){
            BAMradio.Instance.RadioManager.nowPlaying=false;
            if(BAMradio.Instance.RadioManager.AutoPlayNext){
                  BAMradio.Instance.RadioManager.playNextSong();
             }
          }
    }
}
