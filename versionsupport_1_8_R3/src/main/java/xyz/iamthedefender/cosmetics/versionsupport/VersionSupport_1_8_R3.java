package xyz.iamthedefender.cosmetics.versionsupport;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;

public class VersionSupport_1_8_R3 implements IVersionSupport {

    @Override
    public ItemStack getSkull(String base64) {
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();

        if(head == null) throw new RuntimeException("Failed to get skull (v1.8.8)");

        ItemMeta itemMeta = head.getItemMeta();

        if(itemMeta == null) return head;

        itemMeta =XSkull.of(itemMeta).profile(Profileable.detect(base64)).lenient().apply();

        head.setItemMeta(itemMeta);

        return head;
    }

    @Override
    public @NotNull ItemStack applyRenderer(MapRenderer mapRenderer, MapView mapView) {
        ItemStack map = XMaterial.FILLED_MAP.parseItem();
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.addRenderer(mapRenderer);
        map.setDurability(mapView.getId());
        return map;
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particleWrapper, Color color) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getFloat().write(0, (float) location.getX());
        packet.getFloat().write(1, (float) location.getY());
        packet.getFloat().write(2, (float) location.getZ());

        packet.getFloat().write(6, 1.0f);

        packet.getIntegers().write(0, 0);

        packet.getFloat().write(3, color.getRed() / 255f);
        packet.getFloat().write(4, color.getGreen() / 255f);
        packet.getFloat().write(5, color.getBlue() / 255f);

        packet.getParticles().write(0, particleWrapper.getWrapperParticle());

        if(player != null){
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            return;
        }

        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        packet.getFloat().write(0, 0f);
        packet.getFloat().write(1, 0f);
        packet.getFloat().write(2, 0f);

        packet.getFloat().write(3, 1.0f);

        packet.getIntegers().write(0, 1);

        packet.getParticles().write(0, particle.getWrapperParticle());

        if(player != null){
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            return;
        }

        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle, int count) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        packet.getFloat().write(0, 0f);
        packet.getFloat().write(1, 0f);
        packet.getFloat().write(2, 0f);

        packet.getFloat().write(3, 1.0f);

        packet.getIntegers().write(0, count);

        packet.getParticles().write(0, particle.getWrapperParticle());

        if(player != null){
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            return;
        }

        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        packet.getFloat().write(0, 0f);
        packet.getFloat().write(1, 0f);
        packet.getFloat().write(2, 0f);

        packet.getFloat().write(3, speed);

        packet.getIntegers().write(0, count);

        packet.getParticles().write(0, particle.getWrapperParticle());

        if(player != null){
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            return;
        }

        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed, Vector offset) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        packet.getFloat().write(0, (float) offset.getX());
        packet.getFloat().write(1, (float) offset.getY());
        packet.getFloat().write(2, (float) offset.getZ());

        packet.getFloat().write(3, speed);

        packet.getIntegers().write(0, count);

        packet.getParticles().write(0, particle.getWrapperParticle());

        if(player != null){
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            return;
        }

        ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
    }


}
