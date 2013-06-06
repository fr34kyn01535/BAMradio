package yt.bam.bamradio;

import org.bukkit.entity.Player;

/**
 * @author fr34kyn01535,t7seven7t
 */
public interface MidiPlayer {

	public void tuneIn(Player player);
	
	public void tuneOut(Player player);
	
	public void stopPlaying();
	
	public boolean isNowPlaying();
	
	public void playNextSong();
	
	public boolean playSong(String midiName);
}
