package xyz.iamthedefender.cosmetics.api.cosmetics;

import org.bukkit.ChatColor;

public enum RarityType {
    NONE(ChatColor.GREEN),
    RANDOM(ChatColor.GREEN),
    COMMON(ChatColor.GREEN),
    RARE(ChatColor.AQUA),
    EPIC(ChatColor.DARK_PURPLE),
    LEGENDARY(ChatColor.GOLD);

    private final ChatColor color;
    RarityType(ChatColor color) {
        this.color = color;
    }

    public ChatColor getChatColor() {
        return this.color;
    }
}
