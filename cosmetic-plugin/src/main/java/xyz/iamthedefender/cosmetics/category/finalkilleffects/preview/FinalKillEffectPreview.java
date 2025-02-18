package xyz.iamthedefender.cosmetics.category.finalkilleffects.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticPreview;
import xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.Utility;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FinalKillEffectPreview extends CosmeticPreview {

    public FinalKillEffectPreview() {
        super(CosmeticsType.FinalKillEffects);
    }

    @Override
    public void showPreview(Player player, Cosmetics selected, Location previewLocation, Location playerLocation) throws IllegalArgumentException {
        handleLocation(player, playerLocation);

        ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(playerLocation, EntityType.ARMOR_STAND);
        as.setVisible(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                100, 2));

        Runnable onEnd = sendKillEffect(player, previewLocation, (FinalKillEffect) selected);

        PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getIntegers().write(0, as.getEntityId());

        PacketContainer resetPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        resetPacket.getIntegers().write(0, player.getEntityId());
        CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, cameraPacket);

        setOnEnd(player, () -> {
            if (!as.isDead()) as.remove();

            CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, resetPacket);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);

            onEnd.run();
        });
    }

    public Runnable sendKillEffect(Player player, Location location, FinalKillEffect killEffect) {
        NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());

        Block block = location.getBlock();
        Block left = block.getRelative(BlockFace.WEST, 3);
        Block bottomLeft = left.getRelative(BlockFace.SOUTH, 5);
        Location leftLocation = left.getLocation().clone();
        Location bottomLeftLocation = bottomLeft.getLocation().clone();

        leftLocation.setX(leftLocation.getBlockX() + 0.5);
        leftLocation.setZ(leftLocation.getBlockZ() + 0.5);

        bottomLeftLocation.setX(bottomLeftLocation.getBlockX() + 0.5);
        bottomLeftLocation.setZ(bottomLeftLocation.getBlockZ() + 0.5);

        NPC victimNPC = registry.createNPC(EntityType.PLAYER, "");
        NPC derperinoNPC = registry.createNPC(EntityType.PLAYER, "");

        victimNPC.setName(player.getDisplayName());
        derperinoNPC.setName(ColorUtil.translate("&7Derperino"));

        List<String> victimNPCValues = Arrays.asList(Objects.requireNonNull(Utility.getFromName(player.getName())));

        String derperinoNPCValue = "ewogICJ0aW1lc3RhbXAiIDogMTY4NDU1ODYwMzIyNywKICAicHJvZmlsZUlkIiA6ICI1NjY3NWIyMjMyZjA0ZWUwODkxNzllOWM5MjA2Y2ZlOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVJbmRyYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81MzhiZjY4MTQ5MWE2ZmM1NmZlZDdhNjlmNDQ5MmYyN2ExZDE4YTdhMDM5ZTNjOWEzZWMyYjkzZmFkOWZlZDY2IgogICAgfQogIH0KfQ";
        String derperinoNPCSign = "BHb9Cye246WSOcArShXCY8Qv+yXWgANNwnKgCcIh6EEZMuWF6pQFgvYuqPn8l+SikiO4qimBcsjKLAigRCO7nnWCjE/GqtTEAmk5ermP5p+56tbS+AEvnSSOG5+0MtI8hcOEDnZTEI3GMcx/cQSmnRylMNlgMYAt7GL7uAkAd8bjnxG01lrrX5KqIzFnvc9quvruKeDV9fvAwgpc8zzZJwcVzOTZhrLxm1rj+iaVmLrP7PpRMOLF9bx3Q4URLedALLbX5PzkRQvZQBgGUsdCx+UDGjicp8gq7RIYnx+RnCEYHrkf9rrs6b8SW0qyAkhxLqlNZeCIPU8GECD4OpOMxathuQ4anI0j9bntXV/Yegdd42vQVjJVTAnQEGahqI5yTyaxL9r5GbegUHi2YQRTnZMuWNAETUxgaaC0v4kvV/DIDowmBgIAP6Anp2JSDAYXkW/mr/WBjyhk0oG31IHwySU1AuV5mch0v1AV5jrBi3Hjrxp6S+j7vMSpXYpzHmx0O92OfaiSg4J8tyJ/3cRxbGUas2Uc2ZuYa3Ke1FGyKmWVWqcX6APmLXagN5Zuug/aCHSPaoogNY29+YK7JQtRlJisUrG30sh7JUmKeIYMtOuQIgzVdHGhvC6xDqcK9hYnLV8XHNVqXZvq8ArrYD/Nw03MMWomFLM0NUaHaNYmLHo=";

        String victimNPCValue = victimNPCValues.get(0);
        String victimNPCSign = victimNPCValues.get(1);

        victimNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(UUID.randomUUID().toString(), victimNPCSign, victimNPCValue);
        victimNPC.getOrAddTrait(SkinTrait.class).setTexture(victimNPCValue, victimNPCSign);

        derperinoNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(UUID.randomUUID().toString(), derperinoNPCSign, derperinoNPCValue);
        derperinoNPC.getOrAddTrait(SkinTrait.class).setTexture(derperinoNPCValue, derperinoNPCSign);

        victimNPC.getOrAddTrait(PlayerFilter.class).setAllowlist();
        victimNPC.getOrAddTrait(PlayerFilter.class).addPlayer(player.getUniqueId());

        victimNPC.addTrait(Equipment.class);
        victimNPC.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, new ItemStack(Material.IRON_SWORD));

        derperinoNPC.getOrAddTrait(PlayerFilter.class).setAllowlist();
        derperinoNPC.getOrAddTrait(PlayerFilter.class).addPlayer(player.getUniqueId());

//        victimNPC.getOrAddTrait(Gravity.class).toggle();
//        derperinoNPC.getOrAddTrait(Gravity.class).toggle();

        victimNPC.spawn(bottomLeftLocation);
        derperinoNPC.spawn(leftLocation);
        victimNPC.getEntity().setMetadata("NPC2", new FixedMetadataValue(CosmeticsPlugin.getInstance(), ""));
        derperinoNPC.getEntity().setMetadata("NPC1", new FixedMetadataValue(CosmeticsPlugin.getInstance(), ""));
        victimNPC.getNavigator().setTarget(derperinoNPC.getEntity(), true);

        Run.delayed(() -> {
            derperinoNPC.despawn();
            killEffect.execute(player, player, leftLocation, false);
        }, 22L);

        return () -> {
            victimNPC.despawn();
            derperinoNPC.despawn();
        };
    }
}