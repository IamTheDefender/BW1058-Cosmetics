package xyz.iamthedefender.cosmetics.api.menu;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Getter
public abstract class SystemGui {
    public HashMap<Integer, ClickableItem> itemsStorage;
    public List<Option> options;
    public SystemGui(){
        itemsStorage = new HashMap<>();
        options = new ArrayList<>();
    }
    public abstract void open(Player player);
    public abstract void onOpen(Player player);
    public abstract void onClose(Player player);
    public abstract Inventory getInventory();
    public abstract void setItem(int slot, ItemStack item);
    public abstract void setItem(int slot, ItemStack itemStack, @NonNull Consumer<InventoryClickEvent> action);
    public abstract ClickableItem getItem(int slot);
    public abstract void removeItem(int slot);
    public void onClick(InventoryClickEvent event) {}

    public void addOption(Option option){
        options.add(option);
    }

    public void removeOption(Option option){
        options.remove(option);
    }

    public enum Option {
        DO_NOT_CANCEL_CLICK;
    }
}
