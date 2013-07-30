package yt.bam.bamradio.radiomanager.worldguard.events;

import yt.bam.bamradio.radiomanager.worldguard.MovementWay;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class RegionLeftEvent extends RegionEvent {
    public RegionLeftEvent(ProtectedRegion region, Player player, MovementWay movement)
    {
        super(region, player, movement);
    }
}
