package xyz.iamthedefender.cosmetics.versionsupport;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;

import java.util.List;

public class VersionSupport_1_8_R3 implements IVersionSupport {

    @Override
    public @NotNull String getVersion() {
        return "v1.8.8 handler";
    }

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
    public boolean isValidParticle(String name) {

        try{
            EnumParticle.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle) {
        displayParticle(player, location, particle, 1, 1.0f, null, null);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle, int count) {
        displayParticle(player, location, particle, count, 1.0f, null, null);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed) {
        displayParticle(player, location, particle, count, speed, null, null);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed, Vector offset) {
        displayParticle(player, location, particle, count, speed, offset, null);
    }

    @Override
    public void displayParticle(Player player, Location location, ParticleWrapper particleWrapper, Color color) {
        displayParticle(player, location, particleWrapper, 1, 1.0f, null, color);
    }

    private void displayParticle(Player player, Location location, ParticleWrapper particle, int count, float speed, Vector offset, Color color) {
       try {

           for (int i = 0; i < count; i++) {
               PacketContainer packet = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

               double offsetX = offset != null ? (Math.random() * 2 - 1) * offset.getX() : 0;
               double offsetY = offset != null ? (Math.random() * 2 - 1) * offset.getY() : 0;
               double offsetZ = offset != null ? (Math.random() * 2 - 1) * offset.getZ() : 0;

               // Location
               packet.getFloat().write(0, (float) (location.getX() + offsetX));
               packet.getFloat().write(1, (float) (location.getY() + offsetY));
               packet.getFloat().write(2, (float) (location.getZ() + offsetZ));

               // Speed
               packet.getFloat().write(6, speed);

               // Count
               packet.getIntegers().write(0, color != null ? 0 : 1);

               List<EnumWrappers.Particle> extraDataParticles = List.of(EnumWrappers.Particle.BLOCK_CRACK, EnumWrappers.Particle.ITEM_CRACK, EnumWrappers.Particle.BLOCK_DUST);

               if (particle.getWrapperParticle() != null && extraDataParticles.contains(particle.getWrapperParticle())) {
                   packet.getIntegerArrays().write(0, new int[] { Material.SAND.getId() });
               }

               // Particle type
               packet.getParticles().write(0, particle.getWrapperParticle());

               // Color (if applicable)
               if (color != null) {
                   packet.getFloat().write(3, color.getRed() / 255f);
                   packet.getFloat().write(4, color.getGreen() / 255f);
                   packet.getFloat().write(5, color.getBlue() / 255f);
               }

               // Send packet
               if (player != null) {
                   ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
               } else {
                   ProtocolLibrary.getProtocolManager().broadcastServerPacket(packet);
               }
           }
       }catch (Exception e) {
           throw new RuntimeException("Failed to display particle", e);
       }
    }


}
