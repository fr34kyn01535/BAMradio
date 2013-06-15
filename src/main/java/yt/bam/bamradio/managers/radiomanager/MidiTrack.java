package yt.bam.bamradio.managers.radiomanager;

import java.util.logging.Logger;
import javax.sound.midi.Track;
import org.bukkit.Bukkit;

/**
 * @author fr34kyn01535
 */

public class MidiTrack {    
    public static final Logger logger = Bukkit.getLogger();
    private final MinecraftMidiPlayer player;
    private final Track track;
    private int event = 0;

    public MidiTrack(MinecraftMidiPlayer player, Track track) {
        this.player = player;
        this.track = track;
    }

    public void nextTick(float currentTick) {	
        for (; (event < (track.size() - 1)) && (track.get(event).getTick() <= currentTick); event++) {
                player.onMidiMessage(track.get(event).getMessage());
        }
    }
}
