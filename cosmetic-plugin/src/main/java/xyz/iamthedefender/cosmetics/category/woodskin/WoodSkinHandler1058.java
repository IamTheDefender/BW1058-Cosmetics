package xyz.iamthedefender.cosmetics.category.woodskin;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import com.andrei1058.bedwars.api.events.shop.ShopBuyEvent;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.util.Utility;

import java.util.Optional;

public class WoodSkinHandler1058 implements Listener {

    @EventHandler
    public void onShopBuy(ShopBuyEvent e) {
        boolean isWoodSkinsEnabled = Cosmetics.getInstance().getConfig().getBoolean("wood-skins.enabled");
        if (!isWoodSkinsEnabled) return;

        Player p = e.getBuyer();
        IBuyItem item = e.getCategoryContent().getContentTiers().get(0).getBuyItemsList().get(0);
        ItemStack stack = item.getItemStack();

        if (Utility.isWoodOrLogBlock(stack.getType())) {
            String selected = Cosmetics.getInstance().getApi().getSelectedCosmetic(p, CosmeticsType.WoodSkins);
            Optional<XMaterial> optional = XMaterial.matchXMaterial(selected.replace("-", "_").toUpperCase());
            if (optional.isEmpty()) return;
            XMaterial m = optional.get();
            stack.setType(m.parseMaterial());
            stack.setDurability(m.getData());
        }
    }

    @EventHandler
    public void onShopOpen(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();

        Player p = (Player) e.getPlayer();
        IArena arena = BedWars.getAPI().getArenaUtil().getArenaByPlayer(p);
        String selected = Cosmetics.getInstance().getApi().getSelectedCosmetic(p, CosmeticsType.WoodSkins);

        if (arena == null) return;

        boolean isWoodSkinInventory =
                e.getView().getTitle().equals(Utility.getMSGLang(p, "shop-items-messages.inventory-name"))
                        || e.getView().getTitle().equals(Utility.getMSGLang(p, "shop-items-messages.blocks-category.inventory-name"))
                        || e.getView().getTitle().equals(Utility.getMSGLang(p, "shop-items-messages.quick-buy-add-inventory-name"));

        if (isWoodSkinInventory) {
            for (ItemStack i : inv.getContents()) {
                if (i == null) continue;
                if (i.getType() == XMaterial.AIR.parseMaterial()) continue;

                if (Utility.isWoodOrLogBlock(i.getType()) && selected != null) {
                    Optional<XMaterial> optional = XMaterial.matchXMaterial(selected.replace("-", "_").toUpperCase());
                    if (optional.isEmpty()) return;
                    XMaterial m = optional.get();
                    i.setType(m.parseMaterial());
                    i.setDurability(m.getData());
                }
            }
        }
    }
}