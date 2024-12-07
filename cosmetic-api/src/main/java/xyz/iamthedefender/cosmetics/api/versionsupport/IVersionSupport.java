package xyz.iamthedefender.cosmetics.api.versionsupport;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;

public interface IVersionSupport {

    ItemStack getSkull(String base64);

    @NotNull
    ItemStack applyRenderer(MapRenderer mapRenderer, MapView mapView);

    void displayRedstoneParticle(Player player, Location location, Color color);

    void displayParticle(Player player, Location location, ParticleWrapper particle);

    void displayParticle(Player player, Location location, ParticleWrapper particle, int count);

    void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed);
}
