package yt.bam.bamradio.radiomanager;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

/**
 * @author fr34kyn01535
 */

public class Instrument {    
    public static final Logger logger = Bukkit.getLogger();
    public static Sound getInstrument(int patch, int channel) {

		if (channel == 10) {
                    return null;
		}

                if(channel==9){
                    return null;
                }
                
                if ((patch >= 0 && patch <= 7)||(patch >= 80 && patch <= 103)||(patch >= 64 && patch <= 71)) { 
			return Sound.NOTE_PIANO;
		}
                
                if ((patch >= 8 && patch <= 15)) { 
			return Sound.NOTE_PLING;
		}
                
		if ((patch >= 16 && patch <= 23)||(patch >= 44 && patch <= 46)) { // Guitars & bass
			return Sound.NOTE_BASS_GUITAR;
		}
                if((patch >= 28 && patch <= 40)||(patch >= 56 && patch <= 63)){
                        return Sound.NOTE_BASS;
                }

		if (patch >= 113 && patch <= 119) { // Percussive
			return Sound.NOTE_BASS_DRUM;
		}

		if (patch >= 120 && patch <= 127) { // Misc.
			return Sound.NOTE_SNARE_DRUM;
		}
                
                if (patch >= 120 && patch <= 127) { // Misc.
			return Sound.NOTE_SNARE_DRUM;
		}
                
		return Sound.NOTE_PLING;
	}
}
