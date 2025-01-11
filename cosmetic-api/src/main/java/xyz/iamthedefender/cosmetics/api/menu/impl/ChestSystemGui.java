package xyz.iamthedefender.cosmetics.api.menu.impl;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.menu.ClickableItem;
import xyz.iamthedefender.cosmetics.api.menu.SystemGui;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import xyz.iamthedefender.cosmetics.api.util.Utility;

import java.util.HashMap;
import java.util.function.Consumer;

@Getter
public abstract class ChestSystemGui extends SystemGui {
    private final Inventory inventory;

    public ChestSystemGui(String title, int rows){
        if(rows < 0)
            rows = 1;

        inventory = Bukkit.createInventory(null, rows * 9, ColorUtil.translate(title));
        itemsStorage = new HashMap<>();
    }

    public void setItem(int slot, ItemStack item, @NonNull Consumer<InventoryClickEvent> action){
        inventory.setItem(slot, item);
        itemsStorage.put(slot, new ClickableItem(item, action));
    }

    public void setItem(int slot, ItemStack item){
        inventory.setItem(slot, item);
        itemsStorage.put(slot, new ClickableItem(item, (e) -> {

        }));
    }

    @Override
    public ClickableItem getItem(int slot) {
        return itemsStorage.getOrDefault(slot, null);
    }

    @Override
    public void removeItem(int slot) {
        inventory.setItem(slot, null);
        itemsStorage.remove(slot);
    }

    public int firstEmpty(){
        return inventory.firstEmpty();
    }

    @Override
    public void open(Player player) {
        getInventory().clear();
        getItemsStorage().clear();
        onOpen(player);
        player.openInventory(inventory);
        Utility.getApi().getSystemGuiManager().setByPlayer(player, this);
    }
}
