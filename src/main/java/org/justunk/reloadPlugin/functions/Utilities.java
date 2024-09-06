package org.justunk.reloadPlugin.functions;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

public class Utilities {

    public static void ActionBar(Player player, String message) {

        Player plr = Bukkit.getPlayer(player.getName());

        if (plr != null) {
            plr.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message));
            plr.playSound(plr, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        } else {
            Bukkit.getServer().getLogger().severe("[ReloadPlugin] Player was not found!");
        }

    }


    public static void Title(Player player, String upper, String downer, Integer timespan, Integer fadeIn, Integer fadeOut) {
        Player plr = Bukkit.getPlayer(player.getName());

        int seconds = timespan * 20;
        int in = fadeIn * 20;
        int out = fadeOut * 20;

        String title = ChatColor.translateAlternateColorCodes('&', upper);
        String subtitle = ChatColor.translateAlternateColorCodes('&', downer);


        if (plr != null) {
            plr.sendTitle(title, subtitle, seconds, in, out);
            plr.playSound(plr, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        } else {
            Bukkit.getServer().getLogger().severe("[ReloadPlugin] Player was not found!");
        }
    }


    public static void Message(Player player, String message) {

        Player plr = Bukkit.getPlayer(player.getName());

        String msg = ChatColor.translateAlternateColorCodes('&', message);

        if (plr != null) {
            plr.sendMessage(msg);
            plr.playSound(plr, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        } else {
            Bukkit.getServer().getLogger().severe("[ReloadPlugin] Player was not found!");
        }
    }
}
