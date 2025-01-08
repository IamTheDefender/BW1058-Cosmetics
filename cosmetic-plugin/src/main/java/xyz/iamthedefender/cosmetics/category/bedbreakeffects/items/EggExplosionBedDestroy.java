package xyz.iamthedefender.cosmetics.category.bedbreakeffects.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.handler.ITeamHandler;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.util.MathUtil;

import java.util.List;

public class EggExplosionBedDestroy extends BedDestroy {
    @Override
    public ItemStack getItem() {
        return XMaterial.EGG.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "egg-explosion";
    }

    @Override
    public String getDisplayName() {
        return "Egg Explosion";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Explodes the ground with an", "&7explosion of chickens!");
    }

    @Override
    public int getPrice() {
        return 10000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.COMMON;
    }

    @Override
    public void execute(Player player, Location bedLocation, ITeamHandler victimTeam) {
        // Define the center point where the eggs should be spawned
        Location center = bedLocation;
        for (int i = 0; i < MathUtil.getRandom(5, 6); i++) {
            // Choose a random direction vector
            Vector direction = new Vector(MathUtil.getRandom(-1, 1), 0, MathUtil.getRandom(-1, 1)).normalize();

            // Spawn an egg entity in the chosen direction
            Egg egg = player.getWorld().spawn(center, Egg.class);
            egg.setVelocity(direction.multiply(1.5)); // Make the egg fly faster in the chosen direction

            // Spawn particle effects at the egg's location
            for (int j = 0; j < 10; j++) {
                ParticleWrapper.getParticle("SPELL_WITCH").ifPresent(particle ->
                        particle.support().displayParticle(null, egg.getLocation(), particle, 1, 0, center.toVector()));
                ParticleWrapper.getParticle("SPELL_INSTANT").ifPresent(particle ->
                        particle.support().displayParticle(null, egg.getLocation(), particle, 1, 0, center.toVector()));
            }

            Run.sync(() -> {
                egg.getWorld().spawnEntity(egg.getLocation(), EntityType.CHICKEN);
                egg.remove();
            });
        }
    }

}