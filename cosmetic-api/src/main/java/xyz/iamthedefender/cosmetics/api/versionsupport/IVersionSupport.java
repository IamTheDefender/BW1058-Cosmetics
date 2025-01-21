package xyz.iamthedefender.cosmetics.api.versionsupport;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;

public interface IVersionSupport {

    @NotNull String getVersion();

    ItemStack getSkull(String base64);

    @NotNull
    ItemStack applyRenderer(MapRenderer mapRenderer, MapView mapView);

    /**
        Checks if a particle is valid depending on the version
     **/
    boolean isValidParticle(String name);

    default void displayRedstoneParticle(Player player, Location location, Color color) {
        displayParticle(player, location, ParticleWrapper.getParticle("REDSTONE").orElseGet(() -> (ParticleWrapper.getParticle("DUST").orElseThrow())), color);
    }

    void displayParticle(Player player, Location location, ParticleWrapper particleWrapper, Color color);

    void displayParticle(Player player, Location location, ParticleWrapper particle);

    void displayParticle(Player player, Location location, ParticleWrapper particle, int count);

    void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed);

    void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed, Vector offset);

    default void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed, float offsetX, float offsetY, float offsetZ) {
        displayParticle(player, location, particle, count, speed, new Vector(offsetX, offsetY, offsetZ));
    }
}
