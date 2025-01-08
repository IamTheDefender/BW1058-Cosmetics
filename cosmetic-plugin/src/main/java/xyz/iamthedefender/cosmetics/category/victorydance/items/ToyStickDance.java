package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.utils.ColorUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ToyStickDance extends VictoryDance implements Listener {
    private final Map<Player, Long> cooldown = new HashMap<>();

    @Override
    public ItemStack getItem() {
        return XMaterial.STICK.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "toy-stick";
    }

    @Override
    public String getDisplayName() {
        return "Toy Stick";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7This humble baton sold at", "&7\"Sticks R Us\" is actually a", "&7magic wand. Also it blows up", "&7things.");
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.EPIC;
    }

    @Override
    public void execute(Player winner) {
        ItemStack i = new ItemStack(Material.STICK);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(ColorUtil.colored("&aToy Stick"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ColorUtil.colored("&7Right Click on a block"));
        lore.add(ColorUtil.colored("&7to fly!"));
        i.setItemMeta(im);
        winner.getInventory().addItem(i);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || !event.getItem().equals(getItem())) return;
        if (cooldown.containsKey(event.getPlayer())) {
            long difference = System.currentTimeMillis() - cooldown.get(event.getPlayer());
            if (difference < TimeUnit.SECONDS.toMillis(1)) {
                return;
            }
        }
        event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(-6).setY(6));
        for (Location loc : UsefulUtilsVD.generateSphere(event.getPlayer().getLocation(), 6, false)) {
            final Block block = loc.getBlock();
            UsefulUtilsVD.bounceBlock(block);
            block.breakNaturally();
        }
        cooldown.put(event.getPlayer(), System.currentTimeMillis());
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldown.remove(event.getPlayer());
            }
        }.runTaskLater(Cosmetics.getInstance(), 20);
    }
}