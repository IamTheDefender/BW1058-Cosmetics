package xyz.iamthedefender.cosmetics.category.glyphs.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticPreview;
import xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import xyz.iamthedefender.cosmetics.category.glyphs.util.GlyphUtil;
import xyz.iamthedefender.cosmetics.category.glyphs.util.ImageParticles;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlyphPreview extends CosmeticPreview {

    public GlyphPreview() {
        super(CosmeticsType.Glyphs);
    }

    @Override
    public void showPreview(Player player, Cosmetics selected, Location previewLocation, Location playerLocation) throws IllegalArgumentException {
        handleLocation(player, playerLocation);

        ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(playerLocation, EntityType.ARMOR_STAND);
        as.setVisible(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                100, 2));

        sendGlyphParticles(player, previewLocation, selected.getIdentifier());

        PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getIntegers().write(0, as.getEntityId());

        PacketContainer resetPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        resetPacket.getIntegers().write(0, player.getEntityId());
        CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, cameraPacket);


        setOnEnd(player, () -> {
            if (!as.isDead()) as.remove();

            CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, resetPacket);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        });
    }

    private void sendGlyphParticles(Player player, Location location, String selected) {
        ConfigManager config = ConfigUtils.getGlyphs();

        String glyphFile = config.getString(CosmeticsType.Glyphs.getSectionKey() + "." + selected + ".file");

        if (glyphFile == null) {
            player.sendMessage(ColorUtil.translate("&cLooks like the glyphFile is null? Contact a developer!"));
            Logger.getLogger("Minecraft").log(Level.SEVERE, glyphFile + " is null! 1");
            return;
        }

        File file = new File(
                CosmeticsPlugin.getInstance().getHandler().getAddonPath() +
                        "/Glyphs/" +
                        glyphFile);

        if (!file.exists()){
            player.sendMessage(ColorUtil.translate("&cLooks like the glyphFile is null? Contact a developer!"));
            Logger.getLogger("Minecraft").log(Level.SEVERE, glyphFile + " is null! 2");
            return;
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (final IOException e) {
            Logger.getLogger("Minecraft").log(Level.SEVERE, "UNABLE TO READ FILE! GLYPHUTIL()");
        }
        if (image == null) return;

        ImageParticles imageParticles = new ImageParticles(image, 1);
        imageParticles.setAnchor(50, 10);
        imageParticles.setDisplayRatio(0.1);

        location.add(0.5, 2, 0.5);

        Map<Location, Color> particles = imageParticles.getParticles(location, location.getPitch(), 180.0f);

        long perIteration = 2L;
        long time = getEndDelay() / perIteration;
        AtomicLong counter = new AtomicLong(time);

        Run.everyAsync((r) -> {
            if (counter.decrementAndGet() < 0) r.cancel();

            for (Location spot : particles.keySet()) {
                GlyphUtil.sendRedstoneParticle(player, spot, particles.get(spot));
            }
        }, perIteration);
    }
}