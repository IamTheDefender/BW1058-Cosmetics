package xyz.iamthedefender.cosmetics.category.finalkilleffects.items;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.utils.ColorUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.util.EntityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatCruxEffect extends FinalKillEffect {
    @Override
    public ItemStack getItem() {
        return XMaterial.BAT_SPAWN_EGG.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "batcrux";
    }

    @Override
    public String getDisplayName() {
        return "Batcrux";
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns many bats at", "&7location of victim!");
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

        List<Bat> bats = new ArrayList<>();

        for (int i = 0; i < 4; i++){
            Bat bat = (Bat) location.getWorld().spawnEntity(location, EntityType.BAT);
            bat.setVelocity(bat.getLocation().getDirection().multiply(0.7).setY(2));
            bat.setCustomNameVisible(true);
            
            int number = i + 1;
            if (onlyVictim) {
                bat.setCustomName(ColorUtil.colored("&7Deperino's horcrux #" + number));
            } else {
                bat.setCustomName(ColorUtil.colored("&7" + victim.getName() + "'s horcrux #" + number));
            }
            bats.add(bat);
        }

        if (onlyVictim) {
            for (Bat bat : bats) {
                EntityUtil.entityForPlayerOnly(bat, victim);
            }
        }

        Run.delayed(() -> {
            if (!bats.isEmpty()) {
                for (Bat bat : bats) {
                    Location batLocation = bat.getLocation().clone();

                    ParticleWrapper particleWrapper;

                    try{
                        particleWrapper = new ParticleWrapper(WrappedParticle.create(Particle.SMOKE_LARGE, null));
                    }catch (Exception exception){
                        particleWrapper = new ParticleWrapper(EnumWrappers.Particle.SMOKE_LARGE);
                    }

                    Cosmetics.getInstance().getVersionSupport().displayParticle(
                            null,
                            batLocation,
                            particleWrapper,
                            1
                    );

                }
            }
        }, 5L);

        Run.delayed(() -> {
            for (Bat bat : bats) {
                bat.remove();
            }
            bats.clear();
        }, 80L);
    }
}