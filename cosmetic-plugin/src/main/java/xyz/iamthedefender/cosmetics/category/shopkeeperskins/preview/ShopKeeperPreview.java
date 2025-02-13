package xyz.iamthedefender.cosmetics.category.shopkeeperskins.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticPreview;
import xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.utils.ShopKeeperSkinsUtils;

public class ShopKeeperPreview extends CosmeticPreview {

    public ShopKeeperPreview() {
        super(CosmeticsType.ShopKeeperSkin);
    }

    @Override
    public void showPreview(Player player, Cosmetics selected, Location previewLocation, Location playerLocation) throws IllegalArgumentException {
        handleLocation(player, playerLocation);

        ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(playerLocation, EntityType.ARMOR_STAND);
        as.setVisible(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                100, 2));

        PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getIntegers().write(0, as.getEntityId());

        PacketContainer resetPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        resetPacket.getIntegers().write(0, player.getEntityId());
        CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, cameraPacket);

        ShopKeeperSkinsUtils.spawnShopKeeperNPCForPreview(player, previewLocation, selected.getIdentifier());

        setOnEnd(player, () -> {
            if (!as.isDead()) as.remove();

            CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, resetPacket);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        });
    }
}