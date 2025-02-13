package xyz.iamthedefender.cosmetics.category.woodskin;

import com.cryptomorin.xseries.XMaterial;
import com.tomkeuper.bedwars.BedWars;
import com.tomkeuper.bedwars.api.arena.IArena;
import com.tomkeuper.bedwars.api.arena.shop.IBuyItem;
import com.tomkeuper.bedwars.api.events.shop.ShopBuyEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.WoodSkin;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

public class WoodSkinHandler2023 implements Listener {

    @EventHandler
    public void onShopBuy(ShopBuyEvent e) {
        boolean isWoodSkinsEnabled = CosmeticsPlugin.getInstance().getConfig().getBoolean("wood-skins.enabled");
        if (!isWoodSkinsEnabled) return;

        Player p = e.getBuyer();
        IBuyItem item = e.getCategoryContent().getContentTiers().get(0).getBuyItemsList().get(0);
        ItemStack stack = item.getItemStack();

        if (Utility.isWoodOrLogBlock(stack.getType())) {
            String selected = CosmeticsPlugin.getInstance().getApi().getSelectedCosmetic(p, CosmeticsType.WoodSkins);
            WoodSkin selectedWoodSkin = find(selected);

            if(selectedWoodSkin == null) return;

            ItemStack configItem = (ItemStack) selectedWoodSkin.getField(FieldsType.ITEM_STACK, p);

            stack.setType(configItem.getType());
            stack.setDurability(configItem.getDurability());
        }
    }

    @EventHandler
    public void onShopOpen(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();

        Player p = (Player) e.getPlayer();
        IArena arena = BedWars.getAPI().getArenaUtil().getArenaByPlayer(p);
        String selected = CosmeticsPlugin.getInstance().getApi().getSelectedCosmetic(p, CosmeticsType.WoodSkins);
        WoodSkin selectedWoodSkin = find(selected);

        if(selectedWoodSkin == null) return;

        if (arena == null) return;

        boolean isWoodSkinInventory =
                e.getView().getTitle().equals(Utility.getMSGLang(p, "shop-items-messages.inventory-name"))
                        || e.getView().getTitle().equals(Utility.getMSGLang(p, "shop-items-messages.blocks-category.inventory-name"))
                        || e.getView().getTitle().equals(Utility.getMSGLang(p, "shop-items-messages.quick-buy-add-inventory-name"));

        if(!isWoodSkinInventory) return;

        ItemStack item = (ItemStack) selectedWoodSkin.getField(FieldsType.ITEM_STACK, p);

        if (item == null) return;

        for (ItemStack itemStack : inv.getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == XMaterial.AIR.parseMaterial()) continue;
            if (!Utility.isWoodOrLogBlock(itemStack.getType())) continue;

            itemStack.setType(item.getType());
            itemStack.setDurability(item.getDurability());
        }
    }

    private WoodSkin find(String selected) {
        if (selected == null) return null;

        for (WoodSkin woodSkin : StartupUtils.woodSkinsList) {
            if (woodSkin.getIdentifier().equals(selected)) {
                return woodSkin;
            }
        }
        return null;
    }
}