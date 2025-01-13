package xyz.iamthedefender.cosmetics.api.cosmetics;

import org.bukkit.ChatColor;

public enum RarityType {
    LEGENDARY(ChatColor.GOLD),
    EPIC(ChatColor.DARK_PURPLE),
    RARE(ChatColor.AQUA),
    COMMON(ChatColor.GREEN),
    RANDOM(ChatColor.GREEN),
    NONE(ChatColor.GREEN);

    private final ChatColor color;
    RarityType(ChatColor color) {
        this.color = color;
    }

    public ChatColor getChatColor() {
        return this.color;
    }
}
