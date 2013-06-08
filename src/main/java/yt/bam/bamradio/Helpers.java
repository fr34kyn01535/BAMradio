package yt.bam.bamradio;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author FR34KYN01535
 */

public class Helpers {
    public static void sendMessage(Player player,String message){
        player.sendMessage(ChatColor.GRAY+"[BAMradio] "+ChatColor.BLUE + message);
    }
    public static void sendMessage(CommandSender player,String message){
        player.sendMessage(ChatColor.GRAY+"[BAMradio] "+ChatColor.BLUE + message);
    }
}
