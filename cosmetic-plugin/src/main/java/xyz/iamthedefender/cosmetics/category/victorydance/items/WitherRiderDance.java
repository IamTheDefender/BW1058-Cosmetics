package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.ShopKeeperHandler1058;

import java.util.List;

public class WitherRiderDance extends VictoryDance implements Listener {
    @Override
    public ItemStack getItem() {
        return XMaterial.NETHER_STAR.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "wither-rider";
    }

    @Override
    public String getDisplayName() {
        return "Wither Rider";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Ride a wither straight from the", "&7nether - click to shoot Wither", "&7skulls.");
    }

    @Override
    public int getPrice() {
        return 25000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.EPIC;
    }

    @Override
    public void execute(Player winner) {
        Wither wither = (Wither) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.WITHER);
        wither.setPassenger(winner);
        wither.setMetadata("VD", new FixedMetadataValue(Cosmetics.getInstance(), ""));
        wither.setCustomName(ColorUtil.translate("&a" + winner.getName() + "'s Wither!"));
        wither.setNoDamageTicks(Integer.MAX_VALUE);

        addEntity(winner, wither);

        addTask(winner, Run.every(r -> {
            if(!ShopKeeperHandler1058.arenas.containsKey(winner.getWorld().getName())) {
                wither.remove();
                r.cancel();
                return;
            }

            if (wither.getPassenger() != winner){
                wither.setPassenger(winner);
            }

            Vector direction = winner.getEyeLocation().clone().getDirection().normalize().multiply(0.5);
            wither.setVelocity(direction);
            wither.setTarget(null);
        }, 1L));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(!(player.getVehicle() instanceof Wither)) return;

        if(!player.getVehicle().hasMetadata("VD")) return;

        event.setCancelled(true);

        WitherSkull witherSkull = player.getWorld().spawn(player.getEyeLocation().clone().add(player.getEyeLocation().getDirection().normalize().multiply(2)), WitherSkull.class);
        witherSkull.setShooter(player);
        witherSkull.setVelocity(player.getEyeLocation().clone().getDirection().normalize().multiply(3));

        addEntity(player, witherSkull);
    }
}
