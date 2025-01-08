package xyz.iamthedefender.cosmetics.category.shopkeeperskins.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.ShopKeeperSkin;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.utils.ShopKeeperSkinsUtils;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static xyz.iamthedefender.cosmetics.util.StartupUtils.getCosmeticLocation;
import static xyz.iamthedefender.cosmetics.util.StartupUtils.getPlayerLocation;

public class ShopKeeperPreview {

    private final Map<UUID, Map<Integer, org.bukkit.inventory.ItemStack>> inventories = new HashMap<>();


    public void sendPreviewShopKeeperSkin(Player player, String selected, InventoryGui gui){
        for (ShopKeeperSkin shopKeeperSkin : StartupUtils.shopKeeperSkinList) {
            if (shopKeeperSkin.getIdentifier().equals(selected)){
                if (shopKeeperSkin.getField(FieldsType.RARITY, player) == RarityType.NONE) {
                    gui.open(player);
                    XSound.ENTITY_VILLAGER_NO.play(player, 1.0f, 1.0f);
                    return;
                }
            }
        }

        UUID playerUUID = player.getUniqueId();

        Location beforeLocation = player.getLocation().clone();
        Inventory playerInv = player.getInventory();
        if (!inventories.containsKey(playerUUID)) inventories.put(playerUUID, new HashMap<>());

        Map<Integer, ItemStack> items = inventories.get(playerUUID);

        for (int i = 0; i < playerInv.getSize(); i++) {
            if (playerInv.getItem(i) == null) continue;
            if (playerInv.getItem(i).getType() == null) continue;
            if (playerInv.getItem(i).getType() == Material.AIR) continue;

            items.put(i, playerInv.getItem(i));
        }

        playerInv.clear();
        player.closeInventory();
        Location cosmeticLocation = null, playerLocation = null;

        try {
             cosmeticLocation = getCosmeticLocation();
             playerLocation = getPlayerLocation();
        }catch (Exception exception){
            exception.printStackTrace();
            player.sendMessage(ColorUtil.colored("&cEither Preview location or Player location is not set! Contact the admin."));
        }

        if (cosmeticLocation == null || playerLocation == null) return;

        final Location finalPlayerLocation = playerLocation;
        final Location finalCosmeticLocation = cosmeticLocation;

        ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(finalPlayerLocation, EntityType.ARMOR_STAND);
        as.setVisible(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                100, 2));

        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (player1.equals(player)) continue;

            player1.hidePlayer(player);
        }

        PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getIntegers().write(0, as.getEntityId());

        PacketContainer resetPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        resetPacket.getIntegers().write(0, player.getEntityId());
        Cosmetics.getInstance().getProtocolManager().sendServerPacket(player, cameraPacket);

        ShopKeeperSkinsUtils.spawnShopKeeperNPCForPreview(player, finalCosmeticLocation, selected);

        Run.delayed(() -> {
            if (!as.isDead()) as.remove();
            Cosmetics.getInstance().getProtocolManager().sendServerPacket(player, resetPacket);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.teleport(beforeLocation);

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (player1.equals(player)) continue;

                player1.showPlayer(player);
            }

            for (Map.Entry<Integer, ItemStack> entry: items.entrySet()) {
                playerInv.setItem(entry.getKey(), entry.getValue());
            }

            gui.open(player);
        }, 5 * 20L);
    }
}