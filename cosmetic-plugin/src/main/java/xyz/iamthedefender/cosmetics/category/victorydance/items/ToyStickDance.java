package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import com.hakan.core.utils.ColorUtil;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ToyStickDance extends VictoryDance {
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
        final long[] cooldown = {0};
        HCore.registerEvent(PlayerInteractEvent.class).filter(e -> i.equals(e.getItem())).consume((event) -> {
            if (cooldown[0] != 0 && cooldown[0] > System.currentTimeMillis()){
                return;
            }else if (cooldown[0] == 0 || cooldown[0] < System.currentTimeMillis()){
                cooldown[0] = System.currentTimeMillis() + 1000;
            }
            event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().multiply(-6).setY(6));
            for (Location loc : UsefulUtilsVD.generateSphere(event.getPlayer().getLocation(), 6, false)) {
                final Block block = loc.getBlock();
                UsefulUtilsVD.bounceBlock(block);
                block.breakNaturally();
            }
        });
    }
}
