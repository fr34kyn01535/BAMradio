package yt.bam.bamradio;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author fr34kyn01535,t7seven7t
 */
public class PlayerListener implements Listener {
	@EventHandler
	public static void onPlayerQuit(PlayerQuitEvent event) {
		if (BAMradio.Instance.MidiPlayer != null)
                    BAMradio.Instance.MidiPlayer.tuneOut(event.getPlayer());
	}
        @EventHandler
	public static void onPlayerJoin(PlayerJoinEvent event) {
		if (BAMradio.Instance.MidiPlayer != null)
                    BAMradio.Instance.MidiPlayer.tuneIn(event.getPlayer());
	}
}
