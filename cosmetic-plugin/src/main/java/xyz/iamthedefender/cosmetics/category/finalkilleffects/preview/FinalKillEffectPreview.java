package xyz.iamthedefender.cosmetics.category.finalkilleffects.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.cryptomorin.xseries.XSound;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.PlayerFilter;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import xyz.iamthedefender.cosmetics.api.menu.SystemGui;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.*;

import static xyz.iamthedefender.cosmetics.util.StartupUtils.getCosmeticLocation;
import static xyz.iamthedefender.cosmetics.util.StartupUtils.getPlayerLocation;

public class FinalKillEffectPreview {

    private final Map<UUID, Map<Integer, ItemStack>> inventories = new HashMap<>();

    private FinalKillEffect killEffect;

    public void sendPreviewKillEffect(Player player, String selected, SystemGui gui) {
        for (FinalKillEffect killEffect : StartupUtils.finalKillList) {
            if (killEffect.getIdentifier().equals(selected)){
                if (killEffect.getField(FieldsType.RARITY, player) == RarityType.NONE) {
                    gui.open(player);
                    XSound.ENTITY_VILLAGER_NO.play(player, 1.0f, 1.0f);
                    return;
                } else {
                    this.killEffect = killEffect;
                }
            }
        }

        UUID playerUUID = player.getUniqueId();

        Location beforeLocation = player.getLocation().clone();

        Inventory playerInv = player.getInventory();
        if (!inventories.containsKey(playerUUID)) inventories.put(playerUUID, new HashMap<>());

        Map<Integer, org.bukkit.inventory.ItemStack> items = inventories.get(playerUUID);

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
            player.sendMessage(ColorUtil.translate("&cEither Preview location or Player location is not set! Contact the admin."));
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

        sendKillEffect(player, finalCosmeticLocation);

        PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getIntegers().write(0, as.getEntityId());

        PacketContainer resetPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        resetPacket.getIntegers().write(0, player.getEntityId());
        Cosmetics.getInstance().getProtocolManager().sendServerPacket(player, cameraPacket);

        Run.delayed(() -> {
            if (!as.isDead()) as.remove();

            Cosmetics.getInstance().getProtocolManager().sendServerPacket(player, resetPacket);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.teleport(beforeLocation);

            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (player1.equals(player)) continue;

                player1.showPlayer(player);
            }
            for (Map.Entry<Integer, org.bukkit.inventory.ItemStack> entry: items.entrySet()) {
                playerInv.setItem(entry.getKey(), entry.getValue());
            }

            gui.open(player);
        }, 5 * 20L);
    }

    public void sendKillEffect(Player player, Location location) {
        if (this.killEffect == null) return;
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
        victimNPC.getEntity().setMetadata("NPC2", new FixedMetadataValue(Cosmetics.getInstance(), ""));
        derperinoNPC.getEntity().setMetadata("NPC1", new FixedMetadataValue(Cosmetics.getInstance(), ""));
        victimNPC.getNavigator().setTarget(derperinoNPC.getEntity(), true);

        new BukkitRunnable() {
            @Override
            public void run() {
                Player deperinoPlayer = (Player) derperinoNPC.getEntity();
                derperinoNPC.despawn();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        killEffect.execute(player, player, leftLocation, true);
                    }
                }.runTask(Cosmetics.getInstance());
            }
        }.runTaskLater(Cosmetics.getInstance(), 22L);

        new BukkitRunnable() {
            int tick = 5;
            @Override
            public void run() {
                if (tick == 0){

                    if (victimNPC.getEntity() != null){
                        if (!victimNPC.getEntity().isDead()){
                            victimNPC.despawn();
                        }
                    }
                    if (derperinoNPC.getEntity() != null) {
                        if (!derperinoNPC.getEntity().isDead()) {
                            derperinoNPC.despawn();
                        }
                    }

                    cancel();
                }
                tick--;
            }
        }.runTaskTimer(Cosmetics.getInstance(), 0, 20);
    }
}