package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.handler.IArenaHandler;
import xyz.iamthedefender.cosmetics.api.util.Run;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class YeeHawDance extends VictoryDance {
    @Override
    public ItemStack getItem() {
        return XMaterial.SADDLE.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "yee-haw";
    }

    @Override
    public String getDisplayName() {
        return "Yee Haw";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Ride a horse like it's 1876.");
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
        Horse horse = (Horse) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.HORSE);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.setJumpStrength(10.0);
        horse.setPassenger(winner);
        horse.setAdult();
        horse.setNoDamageTicks(Integer.MAX_VALUE);
        horse.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 190000000, 3));
        horse.setTamed(true);
        horse.setOwner(winner);

        Run.every((r) -> {
            IArenaHandler arena = Cosmetics.getInstance().getHandler().getArenaUtil().getArenaByPlayer(winner);
            if(arena == null){
                horse.remove();
                r.cancel();
                return;
            }

            if(horse.getPassenger() == null){
                horse.remove();
                r.cancel();
            }
        }, 20 * 20L);

        // Horse Hit Event
        HCore.registerEvent(EntityDamageEvent.class).filter(event -> !horse.isDead()).consume((event) -> {
           if (event.getEntity() == horse){
               event.setCancelled(true);
           }
        });
    }
}
