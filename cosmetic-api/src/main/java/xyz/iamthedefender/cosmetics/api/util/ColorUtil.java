package xyz.iamthedefender.cosmetics.api.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ColorUtil {

    public static String translate(@NotNull String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
