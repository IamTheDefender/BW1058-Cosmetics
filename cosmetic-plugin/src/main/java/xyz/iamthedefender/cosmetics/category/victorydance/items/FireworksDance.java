package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.ShopKeeperHandler1058;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FireworksDance extends VictoryDance {
    @Override
    public ItemStack getItem() {
        return XMaterial.FIREWORK_ROCKET.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "fireworks";
    }

    @Override
    public String getDisplayName() {
        return "Fireworks";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Celebrate with a splendid", "&7fireworks show!");
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
    public void execute(Player winner) {
        new BukkitRunnable() {
            public void run() {
                if (ShopKeeperHandler1058.arenas.containsKey(winner.getWorld().getName())) {
                    final Location loc = winner.getEyeLocation();
                    UsefulUtilsVD.spawnFireWorks(winner, 1, Color.RED, Color.BLUE, loc, false);
                    new BukkitRunnable() {
                        public void run() {
                            UsefulUtilsVD.spawnFireWorks(winner, 1, Color.ORANGE, Color.YELLOW, loc, false);
                        }
                    }.runTaskLater(Cosmetics.getInstance(), 300L);
                }
                else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Cosmetics.getInstance(), 0L, 60L);
    }
}
