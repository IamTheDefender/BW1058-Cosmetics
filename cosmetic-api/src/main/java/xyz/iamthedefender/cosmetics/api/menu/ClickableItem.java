package xyz.iamthedefender.cosmetics.api.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public class ClickableItem {

    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> action;

}
