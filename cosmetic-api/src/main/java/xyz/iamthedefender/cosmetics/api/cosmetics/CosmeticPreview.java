package xyz.iamthedefender.cosmetics.api.cosmetics;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.menu.SystemGui;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class CosmeticPreview {

    /*
    TODO: Handle "None" cosmetics!
     for (Spray spray : StartupUtils.sprayList) {
            if (spray.getIdentifier().equals(selected)){
                if (spray.getField(FieldsType.RARITY, player) == RarityType.NONE) {
                    gui.open(player);
                    XSound.ENTITY_VILLAGER_NO.play(player, 1.0f, 1.0f);
                    return;
                }
            }
        }
     */

    private final CosmeticsType type;
    private final Map<Player, List<ItemStack>> inventoryStorage;
    private final Map<Player, Runnable> onEnd;

    public CosmeticPreview(CosmeticsType type) {
        this.type = type;
        inventoryStorage = new HashMap<>();
        onEnd = new HashMap<>();
    }

    public abstract void showPreview(Player player, Cosmetics selected, Location previewLocation, Location playerLocation) throws IllegalArgumentException;

    public void handleLocation(Player player, Location newLocation) throws IllegalArgumentException{
        Location oldLocation = player.getLocation().clone();

        if (newLocation == null) {
            throw new IllegalArgumentException("Player location is not set!");
        }

        SystemGui systemGui = Utility.getApi().getSystemGuiManager().getByPlayer(player);

        hidePlayer(player);
        player.teleport(newLocation);

        inventoryStorage.put(player, List.of(player.getInventory().getContents()));
        player.getInventory().clear();

        player.closeInventory();


        Run.delayed(() -> {
            if (!onEnd.containsKey(player)) return;

            onEnd.remove(player).run();
            player.teleport(oldLocation);
            showPlayer(player);

            List<ItemStack> items = inventoryStorage.get(player);

            if (items == null) return;

            for (int i = 0; i < items.size(); i++) {
                player.getInventory().setItem(i, items.get(i));
            }

            if (systemGui != null) {
                systemGui.open(player);
            }
        }, getEndDelay());
    }

    private void hidePlayer(Player player) {
        Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(player)).forEach(p -> p.hidePlayer(Utility.getPlugin(), player));
    }

    private void showPlayer(Player player) {
        Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(player)).forEach(p -> p.showPlayer(Utility.getPlugin(), player));
    }

    public void setOnEnd(Player player, Runnable runnable) {
        onEnd.put(player, runnable);
    }

    public long getEndDelay() {
        return 5 * 20L;
    }

}
