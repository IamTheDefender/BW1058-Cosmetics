package xyz.iamthedefender.cosmetics.api.handler;

import org.bukkit.entity.Player;

public interface IScoreboardUtil {
    void giveScoreboard(Player player, boolean b);
    void removePlayerScoreboard(Player player);
}
