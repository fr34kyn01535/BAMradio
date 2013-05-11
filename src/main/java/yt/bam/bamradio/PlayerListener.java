/**
 * Copyright (C) 2012 t7seven7t
 */
package yt.bam.bamradio;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author t7seven7t
 */
public class PlayerListener implements Listener {
	private final BAMradio plugin;
	
	public PlayerListener(final BAMradio plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if (plugin.getMidiPlayer() != null)
			plugin.getMidiPlayer().tuneOut(event.getPlayer());
		
	}
        
        @EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		if (plugin.getMidiPlayer() != null)
			plugin.getMidiPlayer().tuneIn(event.getPlayer());
		
	}
        
        
	
}
