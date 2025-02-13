package xyz.iamthedefender.cosmetics.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;

public class EntityUtil {

    public static void entityForPlayerOnly(Entity entityToHide, Player player){
        if (!CosmeticsPlugin.getInstance().getEntityPlayerHashMap().containsKey(entityToHide.getEntityId())){
            CosmeticsPlugin.getInstance().getEntityPlayerHashMap().put(entityToHide.getEntityId(), player);
        }
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        int[] entityIds = new int[] { entityToHide.getEntityId() };

        try{
            packet.getIntegerArrays().write(0, entityIds);
        }catch (Exception e){
            packet.getModifier().write(0, new IntArrayList(entityIds));
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.getUniqueId().equals(player.getUniqueId())){
                ProtocolLibrary.getProtocolManager().sendServerPacket(onlinePlayer, packet);
            }
        }
    }
}
