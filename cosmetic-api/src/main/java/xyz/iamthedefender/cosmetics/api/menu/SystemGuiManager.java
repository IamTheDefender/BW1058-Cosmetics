package xyz.iamthedefender.cosmetics.api.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@Getter
public class SystemGuiManager implements Listener {
    private final HashMap<Player, SystemGui> guiStorage;
    private final HashMap<Player, SystemGui> lastOpenGuiStorage;
    private final JavaPlugin plugin;

    public SystemGuiManager(JavaPlugin plugin){
        guiStorage = new HashMap<>();
        lastOpenGuiStorage = new HashMap<>();
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if(clickedInventory == null) return;
        if(getByPlayer(player) == null) return;

        if(!getByPlayer(player).getOptions().contains(SystemGui.Option.DO_NOT_CANCEL_CLICK)){
            event.setCancelled(true);
        }

        SystemGui systemGui = getByPlayer(player);
        ClickableItem item = systemGui.getItemsStorage().get(event.getSlot());

        if(item == null) {
            getByPlayer(player).onClick(event);
            return;
        }

        item.getAction().accept(event);
    }

    @EventHandler
    public void onPlayerDrag(InventoryDragEvent event){
        Player player = (Player) event.getWhoClicked();
        if(getByPlayer(player) == null) return;

        if(!getByPlayer(player).getOptions().contains(SystemGui.Option.DO_NOT_CANCEL_CLICK)){
            event.setCancelled(true);
        }

        for (Integer inventorySlot : event.getInventorySlots()) {
            InventoryClickEvent inventoryClickEvent = new InventoryClickEvent(event.getView(), InventoryType.SlotType.CONTAINER, inventorySlot, ClickType.RIGHT, InventoryAction.PLACE_ONE);
            getByPlayer(player).onClick(inventoryClickEvent);
        }

    }

    @EventHandler
    public void onPlayerClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        if(getByPlayer(player) != null){
            SystemGui systemGui = getByPlayer(player);
            Bukkit.getScheduler().runTaskLater(plugin, () -> systemGui.onClose(player), 1L);
            guiStorage.remove(player);

            getLastOpenGuiStorage().put(player, systemGui);
        }
    }

    public SystemGui getByPlayer(Player player){
        return guiStorage.get(player);
    }

    public void setByPlayer(Player player, SystemGui systemGui){
        guiStorage.put(player, systemGui);
    }
}
