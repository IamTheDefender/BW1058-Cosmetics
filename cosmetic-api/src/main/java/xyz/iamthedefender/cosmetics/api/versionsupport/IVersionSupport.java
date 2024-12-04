package xyz.iamthedefender.cosmetics.api.versionsupport;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public interface IVersionSupport {

    ItemStack getSkull(String base64);

    @NotNull
    ItemStack applyRenderer(MapRenderer mapRenderer, MapView mapView);

    void displayRedstoneParticle(Player player, Location location, Color color);
}
