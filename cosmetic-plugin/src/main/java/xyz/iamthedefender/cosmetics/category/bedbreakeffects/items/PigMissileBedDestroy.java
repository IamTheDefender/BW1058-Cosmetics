package xyz.iamthedefender.cosmetics.category.bedbreakeffects.items;


import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.particle.type.ParticleType;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.handler.ITeamHandler;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;

import java.util.Arrays;
import java.util.List;

/**
 * Bed destroy effect.
 * Spawns a flying pig that you can ride!
 */
public class PigMissileBedDestroy extends BedDestroy {
    /** {@inheritDoc} */
    @Override
    public ItemStack getItem() {
        return XMaterial.COOKED_BEEF.parseItem();
    }
    /** {@inheritDoc} */
    @Override
    public String base64() {
        return null;
    }
    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return "pig-missile";
    }
    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
        return "Pig Missile";
    }
    /** {@inheritDoc} */
    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns a flying pig that", "&7you can ride!");
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
        final Pig pig = (Pig)player.getWorld().spawnEntity(bedLocation, EntityType.PIG);
        final ArmorStand stand = (ArmorStand)player.getWorld().spawnEntity(bedLocation, EntityType.ARMOR_STAND);
        pig.setSaddle(true);
        stand.setGravity(false);
        stand.setPassenger(pig);
        stand.setVisible(false);
        new BukkitRunnable() {
            int i1 = 0;

            public void run() {
                ++this.i1;
                stand.eject();
                stand.teleport(stand.getLocation().add(0.0, 0.5, 0.0));
                stand.setPassenger(pig);

                ParticleWrapper particleWrapper;

                try{
                    particleWrapper = new ParticleWrapper(WrappedParticle.create(Particle.FLAME, null));
                }catch (Exception exception){
                    particleWrapper = new ParticleWrapper(EnumWrappers.Particle.FLAME);
                }

                Cosmetics.getInstance().getVersionSupport()
                        .displayParticle(player, stand.getLocation(), particleWrapper, 1, 0.0f);

                XSound.ENTITY_CHICKEN_EGG.play(stand.getLocation(), 1.0f, 1.0f);
                if (this.i1 == 13) {
                    final Firework fw = stand.getWorld().spawn(stand.getLocation(), Firework.class);
                    final FireworkMeta fm = fw.getFireworkMeta();
                    fm.addEffect(FireworkEffect.builder().flicker(true).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.BLACK).withFade(Color.BLACK).build());
                    fw.setFireworkMeta(fm);
                }

                if (this.i1 == 25) {
                    stand.remove();
                    pig.remove();
                    this.i1 = 0;
                    this.cancel();
                }
            }
        }.runTaskTimer(Cosmetics.getInstance(), 4L, 1L);
    }

}
