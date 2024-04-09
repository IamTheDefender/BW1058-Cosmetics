package xyz.iamthedefender.cosmetics.category.finalkilleffects.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import com.hakan.core.particle.Particle;
import com.hakan.core.particle.type.ParticleType;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class BurningShoesEffect extends FinalKillEffect {
    @Override
    public ItemStack getItem() {
        return XMaterial.IRON_BOOTS.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "burning-shoes";
    }

    @Override
    public String getDisplayName() {
        return "Burning Shoes";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Makes your shoes burn!");
    }

    @Override
    public int getPrice() {
        return 5000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.COMMON;
    }

    @Override
    public void execute(Player killer, Player victim, Location location, boolean onlyVictim) {
        (new BukkitRunnable() {
            double t = 0.0D;

            public void run() {
                this.t += 0.3D;
                for (double phi = 0.0D; phi <= 6.0D; phi += 1.5D) {
                    double x = 0.11D * (12.5D - this.t) * Math.cos(this.t + phi);
                    double y = 0.23D * this.t;
                    double z = 0.11D * (12.5D - this.t) * Math.sin(this.t + phi);
                    location.add(x, y, z);
                    if (onlyVictim) {
                        HCore.playParticle(victim, location, new Particle(ParticleType.FLAME, 100, 0.01, new Vector(0.0f, 0.0f, 0.0f)));
                    } else {
                        HCore.playParticle(location, new Particle(ParticleType.FLAME, 100, 0.01, new Vector(0.0f, 0.0f, 0.0f)));
                    }
                    location.subtract(x, y, z);
                    if (this.t >= 12.5D) {
                        location.add(x, y, z);
                        if (phi > Math.PI)
                            cancel();
                    }
                }
            }
        }).runTaskTimer(Cosmetics.getInstance(), 1L, 1L);
    }
}
