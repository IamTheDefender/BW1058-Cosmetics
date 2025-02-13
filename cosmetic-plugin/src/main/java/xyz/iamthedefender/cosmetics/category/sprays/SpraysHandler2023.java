package xyz.iamthedefender.cosmetics.category.sprays;

import com.cryptomorin.xseries.XMaterial;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.Spray;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

public class SpraysHandler2023 implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {

        boolean isSpraysEnabled = CosmeticsPlugin.getInstance().getConfig().getBoolean("sprays.enabled");
        if (!isSpraysEnabled) return;

        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) e.getRightClicked();
            if (itemFrame.getItem() == null) return;
            String selected = CosmeticsPlugin.getInstance().getApi().getSelectedCosmetic(p, CosmeticsType.Sprays);
            XMaterial material = XMaterial.matchXMaterial(itemFrame.getItem());
            // AIR, MAP, FILLED_MAP
            if (material == XMaterial.AIR || material == XMaterial.MAP || material == XMaterial.FILLED_MAP) {
                for(Spray spray : StartupUtils.sprayList){
                    if (spray.getIdentifier().equals(selected)){
                        spray.execute(p, itemFrame);
                    }
                }
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onGameStart2023(com.tomkeuper.bedwars.api.events.gameplay.GameStateChangeEvent event) {

        boolean isSpraysEnabled = CosmeticsPlugin.getInstance().getConfig().getBoolean("sprays.enabled");
        if (!isSpraysEnabled) return;

        if (event.getNewState().name().equals("playing")) {
            for (final Entity e : event.getArena().getWorld().getEntities()) {
                if (e.getType() == EntityType.ITEM_FRAME) {
                    ItemFrame itemFrame = (ItemFrame) e;
                    if (itemFrame.getItem() == null || itemFrame.getItem().getType() == Material.AIR) {
                        ArmorStand stand = (ArmorStand) e.getWorld().spawnEntity(e.getLocation().subtract(0.0, 0.9, 0.0), EntityType.ARMOR_STAND);
                        stand.setVisible(false);
                        stand.setGravity(false);
                        stand.setCustomName(ColorUtil.translate("&eClick!"));
                        stand.setMetadata("HOLO_ITEM_FRAME", new FixedMetadataValue(CosmeticsPlugin.getInstance(), ""));
                        stand.setCustomNameVisible(true);
                        stand.setMarker(true);
                        stand.setSmall(true);
                    }
                }
            }
        }
    }
}
