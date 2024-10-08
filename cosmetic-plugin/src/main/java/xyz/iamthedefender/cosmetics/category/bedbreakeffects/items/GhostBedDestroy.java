package xyz.iamthedefender.cosmetics.category.bedbreakeffects.items;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.handler.ITeamHandler;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Bed destroy effect.
 * Spawns five Ghosts that will haunt other players!
 */
public class GhostBedDestroy extends BedDestroy {
    /** {@inheritDoc} */
    @Override
    public ItemStack getItem() {
        return XMaterial.ENDER_PEARL.parseItem();
    }
    /** {@inheritDoc} */
    @Override
    public String base64() {
        return null;
    }
    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return "ghost";
    }
    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
        return "Ghost";
    }
    /** {@inheritDoc} */
    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns five Ghosts that will", "&7haunt other players!");
    }
    /** {@inheritDoc} */
    @Override
    public int getPrice() {
        return 10000;
    }
    /** {@inheritDoc} */
    @Override
    public RarityType getRarity() {
        return RarityType.RARE;
    }
    /** {@inheritDoc} */
    @Override
    public void execute(Player player, Location bedLocation, ITeamHandler victimTeam) {
        List<Entity> standsAndBats = new ArrayList<>();
        HCore.syncScheduler().every(10L).limit(5).run(() -> {
            Location loc = UsefulUtilsVD.getRandomLocation(bedLocation, 2);
            Bat bat = (Bat) player.getWorld().spawnEntity(loc, EntityType.BAT);
            bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 19999980, 1));
            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            stand.setVisible(false);
            stand.setGravity(false);
            bat.setPassenger(stand);
            stand.setHelmet(UsefulUtilsVD.gethead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTZlOTM0NjdhM2EwNzkyMjdmNGE3ZDNlYmE3NjE3NTM2ZGE0OTFiYzJmYzZkNzNlZTM5NjhkM2NmMWE2YTUifX19"));
            standsAndBats.addAll(Arrays.asList(stand, bat));
        });
        HCore.syncScheduler().after(8, TimeUnit.SECONDS).run(() -> {
            for(Entity entity : standsAndBats){
                if (entity instanceof Bat) {
                    Location location = entity.getLocation();
                    HCore.playParticle(location, new Particle(ParticleType.EXPLOSION_LARGE, 0.0f, new Vector(0.0f, 0.0f, 0.0f)));
                    XSound.ENTITY_GENERIC_EXPLODE.play(location);
                    entity.getPassenger().remove();
                    entity.remove();
                }
            }
        });
    }
}