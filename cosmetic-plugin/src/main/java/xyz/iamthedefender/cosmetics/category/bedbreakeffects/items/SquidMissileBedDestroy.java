package xyz.iamthedefender.cosmetics.category.bedbreakeffects.items;


import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.handler.ITeamHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Bed destroy effect.
 * Spawns a squid that will go flying!
 */
public class SquidMissileBedDestroy extends BedDestroy {
    /** {@inheritDoc} */
    @Override
    public ItemStack getItem() {
        return XMaterial.SQUID_SPAWN_EGG.parseItem();
    }
    /** {@inheritDoc} */
    @Override
    public String base64() {
        return null;
    }
    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return "squid-missile";
    }
    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
        return "Squid Missile";
    }
    /** {@inheritDoc} */
    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns a squid that will", "&7go flying!");
    }
    /** {@inheritDoc} */
    @Override
    public int getPrice() {
        return 5000;
    }
    /** {@inheritDoc} */
    @Override
    public RarityType getRarity() {
        return RarityType.COMMON;
    }
    /** {@inheritDoc} */
    @Override
    public void execute(Player player, Location bedLocation, ITeamHandler victimTeam) {
         Squid squid = (Squid) player.getWorld().spawnEntity(bedLocation, EntityType.SQUID);
         ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(bedLocation, EntityType.ARMOR_STAND);
        stand.setGravity(false);
        stand.setPassenger(squid);
        stand.setVisible(false);
        new BukkitRunnable() {
            int i1 = 0;
            public void run() {
                ++this.i1;
                squid.getLocation().setYaw(180.0f);
                stand.eject();
                stand.teleport(stand.getLocation().add(0.0, 0.5, 0.0));
                stand.setPassenger(squid);
                Particle flame = new Particle(ParticleType.FLAME, 1, 0.0f, new Vector(0.0f, 0.0f, 0.0f));
                HCore.playParticle(player, stand.getLocation(), flame);
                player.playSound(player.getLocation(), XSound.ENTITY_CHICKEN_EGG.parseSound(), 1.0f, 1.0f);
                if (this.i1 == 13) {
                    final Firework fw = stand.getWorld().spawn(stand.getLocation(), Firework.class);
                    final FireworkMeta fm = fw.getFireworkMeta();
                    fm.addEffect(FireworkEffect.builder().flicker(true).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.BLACK).withFade(Color.BLACK).build());
                    fw.setFireworkMeta(fm);
                }
                if (this.i1 == 25) {
                    stand.remove();
                    squid.remove();
                    this.i1 = 0;
                    this.cancel();
                }
            }
        }.runTaskTimer(Cosmetics.getInstance(), 4L, 1L);
    }


}