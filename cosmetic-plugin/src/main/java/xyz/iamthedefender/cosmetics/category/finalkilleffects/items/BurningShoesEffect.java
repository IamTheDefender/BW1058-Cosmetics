package xyz.iamthedefender.cosmetics.category.finalkilleffects.items;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;

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

                    ParticleWrapper particleWrapper;
                    try{
                        particleWrapper = new ParticleWrapper(WrappedParticle.create(org.bukkit.Particle.FLAME, null));
                    }catch (Exception exception){
                        particleWrapper = new ParticleWrapper(EnumWrappers.Particle.FLAME);
                    }

                    if (onlyVictim) {
                        Cosmetics.getInstance().getVersionSupport()
                                .displayParticle(victim, location, particleWrapper, 1, 0.0f);
                    } else {
                        Cosmetics.getInstance().getVersionSupport()
                                .displayParticle(null, location, particleWrapper, 1, 0.0f);
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
