package yt.bam.bamradio.radiomanager.listener.worldguard;

import yt.bam.bamradio.radiomanager.listener.MovementWay;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class RegionEnteredEvent extends RegionEvent {
    public RegionEnteredEvent(ProtectedRegion region, Player player, MovementWay movement)
    {
        super(region, player, movement);
    }
}
