package xyz.iamthedefender.cosmetics.category.finalkilleffects.items;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;

import java.util.Arrays;
import java.util.List;

public class SquidMissleEffect extends FinalKillEffect {
    @Override
    public ItemStack getItem() {
        return XMaterial.SQUID_SPAWN_EGG.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "squid-missile";
    }

    @Override
    public String getDisplayName() {
        return "Squid Missile";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawn a flying squid at", "&7location of victim's death!");
    }

    @Override
    public int getPrice() {
        return 5000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.COMMON;
    }

    private int fireworkID;

    @Override
    public void execute(Player killer, Player victim, Location location, boolean onlyVictim) {
        ArmorStand stand = (ArmorStand) victim.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setGravity(false);
        stand.setVisible(false);

        if (onlyVictim) {
            Squid squid = (Squid) location.getWorld().spawnEntity(location, EntityType.SQUID);
            stand.setPassenger(squid);

            new BukkitRunnable() {
                int i1 = 0;

                public void run() {
                    ++this.i1;
                     squid.getLocation().setYaw(180.0f);
                    stand.eject();
                    stand.teleport(stand.getLocation().add(0.0, 0.5, 0.0));
                    stand.setPassenger(squid);

                    ParticleWrapper particleWrapper;
                    try{
                        particleWrapper = new ParticleWrapper(WrappedParticle.create(org.bukkit.Particle.FLAME, null));
                    }catch (Exception exception){
                        particleWrapper = new ParticleWrapper(EnumWrappers.Particle.FLAME);
                    }

                    Cosmetics.getInstance().getVersionSupport()
                            .displayParticle(null, stand.getLocation(), particleWrapper, 1, 0.0f);

                    victim.playSound(victim.getLocation(), XSound.ENTITY_CHICKEN_EGG.parseSound(), 1.0f, 1.0f);
                    if (this.i1 == 25) {
                        ItemStack stackFirework = new ItemStack(Material.FIREWORK);
                        FireworkMeta fireworkMeta = (FireworkMeta) stackFirework.getItemMeta();
                        fireworkMeta.addEffect(FireworkEffect.builder().flicker(true).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.BLACK).withFade(Color.BLACK).build());
                        fireworkMeta.setPower(1);
                        stackFirework.setItemMeta(fireworkMeta);

                        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
                        fw.setFireworkMeta(fireworkMeta);
                        fw.detonate();
                        fireworkID = fw.getEntityId();

                        stand.eject();
                        stand.remove();
                        squid.remove();
                        this.i1 = 0;
                        this.cancel();
                    }
                }
            }.runTaskTimer(Cosmetics.getInstance(), 4L, 1L);
        } else {
            Squid squid = (Squid) victim.getWorld().spawnEntity(location, EntityType.SQUID);
            stand.setPassenger(squid);
            new BukkitRunnable() {
                int i1 = 0;

                public void run() {
                    ++this.i1;
                    squid.getLocation().setYaw(180.0f);
                    stand.eject();
                    stand.teleport(stand.getLocation().add(0.0, 0.5, 0.0));
                    stand.setPassenger(squid);

                    ParticleWrapper.getParticle("FLAME")
                            .ifPresent(
                                    particleWrapper -> particleWrapper.support().displayParticle(null, stand.getLocation(), particleWrapper, 1, 0.0f)
                            );

                    victim.playSound(victim.getLocation(), XSound.ENTITY_CHICKEN_EGG.parseSound(), 1.0f, 1.0f);
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
}