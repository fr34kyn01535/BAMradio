package yt.bam.bamradio;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author fr34kyn01535
 */

public class PlayerListener implements Listener {
        public static Logger logger = Bukkit.getLogger();
	
        @EventHandler
	public static void onPlayerQuit(PlayerQuitEvent event) {
		if (BAMradio.Instance != null && BAMradio.Instance.RadioManager != null)
                    BAMradio.Instance.RadioManager.tuneOut(event.getPlayer());
	}
        @EventHandler
	public static void onPlayerJoin(PlayerJoinEvent event) {
		if (BAMradio.Instance != null && BAMradio.Instance.RadioManager != null)
                    BAMradio.Instance.RadioManager.tuneIn(event.getPlayer());
	}
}
