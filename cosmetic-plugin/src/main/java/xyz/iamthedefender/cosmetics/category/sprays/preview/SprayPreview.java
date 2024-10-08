package xyz.iamthedefender.cosmetics.category.sprays.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.Spray;
import xyz.iamthedefender.cosmetics.category.sprays.util.SpraysUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static xyz.iamthedefender.cosmetics.util.StartupUtils.getCosmeticLocation;
import static xyz.iamthedefender.cosmetics.util.StartupUtils.getPlayerLocation;

public class SprayPreview {

    private final Map<UUID, Map<Integer, org.bukkit.inventory.ItemStack>> inventories = new HashMap<>();

    private ItemFrame frame;
    private MapView view;

    private int currentID;
    private PacketAdapter adapter;

    public void sendSprayPreview(Player player, String selected, InventoryGui gui){
        for (Spray spray : StartupUtils.sprayList) {
            if (spray.getIdentifier().equals(selected)){
                if (spray.getField(FieldsType.RARITY, player) == RarityType.NONE) {
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
            player.sendMessage(ColorUtil.colored("&cEither Preview location or Player location is not set! Contact the admin."));
        }

        if (cosmeticLocation == null || playerLocation == null) return;

        final Location finalPlayerLocation = playerLocation;
        final Location finalCosmeticLocation = cosmeticLocation;
        final Location itemFrameLocation = finalCosmeticLocation.clone().add(0.0, 1.0, 0.0);

        ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(finalPlayerLocation, EntityType.ARMOR_STAND);
        as.setVisible(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                100, 2));

        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (player1.equals(player)) continue;

            player1.hidePlayer(player);
        }

        sendFrame(player, itemFrameLocation, selected, finalPlayerLocation);

        PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getIntegers().write(0, as.getEntityId());

        PacketContainer resetPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        resetPacket.getIntegers().write(0, player.getEntityId());
        Cosmetics.getInstance().getProtocolManager().sendServerPacket(player, cameraPacket);


        HCore.syncScheduler().after(5, TimeUnit.SECONDS).run(() -> {
            if (!as.isDead()) as.remove();

            //PacketContainer itemFrameDestroyPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            //itemFrameDestroyPacket.getIntegerArrays().write(0, new int[]{currentID});
            //Cosmetics.getInstance().getProtocolManager().sendServerPacket(player, itemFrameDestroyPacket);
            frame.setItem(new ItemStack(Material.AIR));
            frame.remove();
            Cosmetics.getInstance().getProtocolManager().removePacketListener(adapter);
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
        });
    }

    public org.bukkit.inventory.ItemStack createMapItem() {
        org.bukkit.inventory.ItemStack bukkitItem = new org.bukkit.inventory.ItemStack(Material.MAP);
        bukkitItem.setDurability(view.getId());
        return bukkitItem;
    }

    public void sendFrame(Player player, Location location, String selected, Location playerLocation) {
        view = Bukkit.createMap(player.getWorld());
        playerLocation = playerLocation.clone();

        /**
        CustomRenderer renderer = new CustomRenderer();
        ConfigManager config = ConfigUtils.getSprays();

        String sprayFile = config.getString(CosmeticsType.Sprays.getSectionKey() + "." + selected + ".file");
        String sprayURL = config.getString(CosmeticsType.Sprays.getSectionKey() + "." + selected + ".url");
        if (sprayFile == null) {
            if (!renderer.load(sprayURL)) {
                player.sendMessage(ColorUtil.colored("&cLooks like there's an error rendering the Spray, contact the admin!"));
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not load the URL for the " + selected + " Check if the URL in Sprays.yml is valid!");
                return;
            }
        } else {
            File file = new File(Cosmetics.getInstance().getHandler().getAddonPath() + "/" + Cosmetics.getInstance().getConfig().getString("Spray-Dir") + "/" + sprayFile);
            if (!renderer.load(file)) {
                player.sendMessage(ColorUtil.colored("&cLooks like there's an error rendering the Spray, contact the admin!"));
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not load the File for the " + selected + " Check if the File in Sprays.yml is valid!");
                Logger.getLogger("Minecraft").log(Level.SEVERE, "FilePath: " + file.getAbsolutePath());
                return;
            }
        }

        view.addRenderer(renderer);
         **/


        org.bukkit.inventory.ItemStack mapItem = createMapItem();
        playerLocation.setPitch(0);
        playerLocation.add(0, 1.5, 0);
        Location firstBlock = playerLocation.clone().add(playerLocation.getDirection().multiply(2));
        firstBlock.getBlock().setType(Material.BARRIER);
        firstBlock.getChunk().load(true);
        frame = (ItemFrame) player.getWorld().spawnEntity(firstBlock.getBlock().getRelative(getCardinalDirection(playerLocation)).getLocation(), EntityType.ITEM_FRAME);


        adapter = new PacketAdapter(Cosmetics.getInstance(), PacketType.Play.Server.SPAWN_ENTITY) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getIntegers().read(0) == frame.getEntityId() &&
                !event.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        };
        Cosmetics.getInstance().getProtocolManager().addPacketListener(adapter);
        frame.setFacingDirection(getCardinalDirection(playerLocation), true);
        SpraysUtil.spawnSprays(player, frame, true);
        XSound.ENTITY_SILVERFISH_HURT.play(player, 10f, 10f);
        HCore.syncScheduler().every(10L).run((r) -> {
            if(frame.isDead() || !frame.isValid()){
                firstBlock.getBlock().setType(Material.AIR);
            }
        });
    }

    public static BlockFace getCardinalDirection(Location location) {
        double yaw = location.getYaw();

        if (yaw < 0) {
            yaw += 360;
        }

        // Inverted, so if facing is SOUTH it will return NORTH
        if (yaw >= 315 || yaw < 45) {
            return BlockFace.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return BlockFace.EAST;
        } else if (yaw >= 135 && yaw < 225) {
            return BlockFace.NORTH;
        } else if (yaw >= 225) {
            return BlockFace.WEST;
        } else {
            return BlockFace.SELF;
        }
    }

}