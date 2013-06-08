package yt.bam.bamradio.managers.midimanager;

import org.bukkit.entity.Player;

/**
 * @author fr34kyn01535
 */

public interface MidiPlayer {
	public  void tuneIn(Player player);
	
	public void tuneOut(Player player);
	
	public void stopPlaying();
	
	public boolean isNowPlaying();
	
	public void playNextSong();
	
	public boolean playSong(String midiName);
}
