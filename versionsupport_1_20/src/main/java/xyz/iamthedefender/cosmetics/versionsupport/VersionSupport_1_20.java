package xyz.iamthedefender.cosmetics.versionsupport;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedParticle;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class VersionSupport_1_20 implements IVersionSupport {
    @Override
    public ItemStack getSkull(String base64) {
        try {
            PlayerProfile profile = getProfile(getUrlFromBase64(base64).toString());
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if(meta == null) return head;
            meta.setOwnerProfile(profile);
            head.setItemMeta(meta);
            return head;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull ItemStack applyRenderer(MapRenderer mapRenderer, MapView mapView) {
        ItemStack map = XMaterial.FILLED_MAP.parseItem();
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.addRenderer(mapRenderer);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        if(mapMeta == null) {
            Utility.getApi().getPlugin().getLogger().severe("Failed to apply renderer to map, map meta is null!");
            return map;
        }
        mapMeta.setMapView(mapView);
        map.setItemMeta(mapMeta);
        return map;
    }

    private PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID()); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject);
        profile.setTextures(textures);
        return profile;
    }

    private URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        // We simply remove the "beginning" and "ending" part of the JSON, so we're left with only the URL. You could use a proper
        // JSON parser for this, but that's not worth it. The String will always start exactly with this stuff anyway
        return new URL(decoded.substring("{\"textures\":{\"SKIN\":{\"url\":\"".length(), decoded.length() - "\"}}}".length()));
    }

    @Override
    public void displayRedstoneParticle(Player player, Location location, Color color) {
        EnumWrappers.Particle particle = EnumWrappers.Particle.REDSTONE;

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        packet.getFloat().write(0, 0f);
        packet.getFloat().write(1, 0f);
        packet.getFloat().write(2, 0f);

        packet.getFloat().write(3, 1.0f);

        packet.getIntegers().write(0, 1);

        packet.getNewParticles().write(0, WrappedParticle.fromHandle(particle));

        packet.getFloat().write(4, color.getRed() / 255f);
        packet.getFloat().write(5, color.getGreen() / 255f);
        packet.getFloat().write(6, color.getBlue() / 255f);

        if(player != null) {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            return;
        }

        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }
}
