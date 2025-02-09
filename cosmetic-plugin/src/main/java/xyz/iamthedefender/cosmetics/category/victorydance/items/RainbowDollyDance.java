package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RainbowDollyDance extends VictoryDance implements Listener {
    @Override
    public ItemStack getItem() {
        return XMaterial.WHITE_WOOL.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "rainbow-dolly";
    }

    @Override
    public String getDisplayName() {
        return "Rainbow Dolly";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Spawn Dolly the sheep, When you", "&7hit Dolly she, clones herself.");
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
        Sheep sheep = (Sheep) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.SHEEP);
        sheep.setColor(DyeColor.RED);
        sheep.setCustomName("Dolly");
        sheep.setNoDamageTicks(Integer.MAX_VALUE);

        addEntity(winner, sheep);
    }

    @EventHandler
    public void onSheepInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.SHEEP) {
            Sheep sheep = (Sheep) event.getRightClicked();
            if (sheep.getCustomName() == null) {
                return;
            }

            if (Objects.equals(sheep.getCustomName(), "Dolly")) {
                final ArrayList<String> color = new ArrayList<>();
                color.add("RED");
                color.add("BLUE");
                color.add("BROWN");
                color.add("GREEN");
                color.add("PINK");
                color.add("YELLOW");
                color.add("GRAY");
                color.add("LIME");
                color.add("CYAN");
                color.add("MAGENTA");
                String colors = color.get(MathUtil.getRandom(0, color.size() - 1));

                Sheep newSheep = (Sheep) sheep.getWorld().spawnEntity(sheep.getLocation(), EntityType.SHEEP);
                newSheep.getWorld().createExplosion(newSheep.getLocation(), 1.0f);
                newSheep.setColor(DyeColor.valueOf(colors));
                newSheep.setVelocity(sheep.getLocation().getDirection().multiply(-0.5).setY(1));
                newSheep.setCustomName("Dolly");
                addEntity(event.getPlayer(), newSheep);
            }
        }
    }
}
