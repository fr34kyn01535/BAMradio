package yt.bam.bamradio.radiomanager.listener;

import yt.bam.bamradio.radiomanager.listener.worldguard.RegionEnterEvent;
import yt.bam.bamradio.radiomanager.listener.worldguard.RegionLeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import yt.bam.bamradio.BAMradio;

/**
 *
 * @author mewin
 */
public class RegionListener implements Listener {
    
    @EventHandler
    public void onRegionEnter(RegionEnterEvent e)
    {
        if(e.getRegion().getId()!=null && !e.getRegion().getId().isEmpty() && e.getRegion().getId().toLowerCase().equals(BAMradio.Library.Configuration.getString("region", "").toLowerCase())){
            BAMradio.Instance.RadioManager.tuneOut(e.getPlayer());
            BAMradio.Instance.RadioManager.tuneIn(e.getPlayer());
        }
    }
    
    @EventHandler
    public void onRegionLeave(RegionLeaveEvent e)
    {
        if(e.getRegion().getId()!=null && !e.getRegion().getId().isEmpty() && e.getRegion().getId().toLowerCase().equals(BAMradio.Library.Configuration.getString("region", "").toLowerCase())){
            BAMradio.Instance.RadioManager.tuneOut(e.getPlayer());
        }
    }
}