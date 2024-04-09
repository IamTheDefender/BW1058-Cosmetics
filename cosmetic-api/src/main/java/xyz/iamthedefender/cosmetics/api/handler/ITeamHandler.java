package xyz.iamthedefender.cosmetics.api.handler;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface ITeamHandler {
    Location getBed();
    List<Player> getPlayers();
    String getName();
    Location getTeamUpgrades();
    Location getShop();
    Location getSpawn();
    int getSize();
   default boolean isBed(Location location){
        return getBed().getBlockX() == location.getBlockX() && getBed().getBlockY() == location.getBlockY() && getBed().getBlockZ() == location.getBlockZ();
    }
    default boolean isMember(Player player){
        return getPlayers().contains(player);
    }
}
