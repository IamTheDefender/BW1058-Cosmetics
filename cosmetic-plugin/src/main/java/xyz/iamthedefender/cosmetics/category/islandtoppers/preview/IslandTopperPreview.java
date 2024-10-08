package xyz.iamthedefender.cosmetics.category.islandtoppers.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import com.hakan.core.utils.ColorUtil;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.IslandTopper;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import xyz.iamthedefender.cosmetics.category.islandtoppers.util.BlockData;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static xyz.iamthedefender.cosmetics.util.StartupUtils.getCosmeticLocation;
import static xyz.iamthedefender.cosmetics.util.StartupUtils.getPlayerLocation;

public class IslandTopperPreview {

    private final Map<UUID, Map<Integer, ItemStack>> inventories = new HashMap<>();

    public void sendIslandTopperPreview(Player player, String selected, InventoryGui gui) {
        for (IslandTopper islandTopper : StartupUtils.islandTopperList) {
            if (islandTopper.getIdentifier().equals(selected)){
                if (islandTopper.getField(FieldsType.RARITY, player) == RarityType.NONE) {
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


        sendIslandTopper(player, finalCosmeticLocation, selected);

        HCore.syncScheduler().after(5, TimeUnit.SECONDS).run(() -> {
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
        });
    }

    private void sendIslandTopper(Player player, Location location, String selected) {
        ConfigManager config = ConfigUtils.getIslandToppers();

        String topperFileName = config.getString(CosmeticsType.IslandTopper.getSectionKey() + "." + selected + ".file");

        if (topperFileName == null){
            Bukkit.getLogger().severe("Can't find file for " + selected + " island topper!");
            return;
        }

        File file = new File(Cosmetics.getInstance().getHandler().getAddonPath() + "/IslandToppers/" + topperFileName);
        if (!file.exists()){
            Bukkit.getLogger().severe("The file " + file.getName() + " does not exists!");
            return;
        }

        try {
            SchematicFormat schematicFormat = SchematicFormat.getFormat(file);
            if (schematicFormat == null){
                Bukkit.getLogger().severe("Schematic format is null! most probably file is invalid! (" + file.getName() + ")");
                return;
            }

            CuboidClipboard clipboard = schematicFormat.load(file);
            location = location.add(0, clipboard.getHeight() + 1, 0);
            clipboard = flipDirection(clipboard, rpGetPlayerDirection(player));
            Vector newOrigin = new Vector(location.getX(), location.getY(), location.getZ());
            newOrigin = newOrigin.add(clipboard.getOffset());

            boolean order = Cosmetics.getInstance().getConfig().getBoolean("island-toppers.order");

            if (!order){
                HashMap<Location, BlockData> blockLocations = new HashMap<>();

                for (int x = 0; x < clipboard.getSize().getBlockX(); x++) {
                    for (int y = 0; y < clipboard.getSize().getBlockY(); y++) {
                        for (int z = 0; z < clipboard.getSize().getBlockZ(); z++) {
                            BaseBlock baseBlock = clipboard.getBlock(new Vector(x, y, z));

                            if (baseBlock.isAir()) continue;

                            Optional<XMaterial> xMaterial;
                            try{
                                xMaterial = XMaterial.matchXMaterial(Material.getMaterial(baseBlock.getId()).name());
                            }catch (Exception e){
                                xMaterial = Optional.of(XMaterial.matchXMaterial(new MaterialData(baseBlock.getId(), (byte) baseBlock.getData()).getItemType()));
                            }

                            if (xMaterial.isPresent()) {
                                Vector targetVec = new Vector(x, y, z).add(newOrigin);
                                Location targetLoc = new Location(location.getWorld(),
                                        targetVec.getX(),
                                        targetVec.getY(),
                                        targetVec.getZ());

                                BlockData blockData = new BlockData(xMaterial.get().parseMaterial(), xMaterial.get().getData());

                                blockLocations.put(targetLoc, blockData);
                            }
                        }
                    }
                }

                List<Location> keys = new ArrayList<>(blockLocations.keySet());

                new BukkitRunnable() {
                    int index = 0;
                    boolean done = false;
                    @Override
                    public void run() {
                        if (blockLocations.isEmpty()){
                            cancel();
                        }

                        if (index >= blockLocations.size()){
                            if (done){
                                cancel();
                            } else {
                                index = 0;
                                done = true;
                            }
                        } else {
                            if (!done){
                                Location loc = keys.get(index);
                                Material material = blockLocations.get(loc).getMaterial();
                                byte data = blockLocations.get(loc).getData();

                                player.sendBlockChange(loc, material, data);

                                index++;
                            } else {
                                Location loc = keys.get(index);

                                player.sendBlockChange(loc, Material.AIR, (byte) 0);

                                index++;
                            }
                        }
                    }
                }.runTaskTimerAsynchronously(HCore.getInstance(), 0L, 0L);
            } else {
                LinkedHashMap<Location, BlockData> blockLocations = new LinkedHashMap<>();

                for (int x = 0; x < clipboard.getSize().getBlockX(); x++) {
                    for (int y = 0; y < clipboard.getSize().getBlockY(); y++) {
                        for (int z = 0; z < clipboard.getSize().getBlockZ(); z++) {
                            BaseBlock baseBlock = clipboard.getBlock(new Vector(x, y, z));

                            if (baseBlock.isAir()) continue;

                            Optional<XMaterial> xMaterial;
                            try{
                                xMaterial = XMaterial.matchXMaterial(Material.getMaterial(baseBlock.getId()).name());
                            }catch (Exception e){
                                xMaterial = Optional.of(XMaterial.matchXMaterial(new MaterialData(baseBlock.getId(), (byte) baseBlock.getData()).getItemType()));
                            }
                            if (xMaterial.isPresent()) {
                                Vector targetVec = new Vector(x, y, z).add(newOrigin);
                                Location targetLoc = new Location(location.getWorld(),
                                        targetVec.getX(),
                                        targetVec.getY(),
                                        targetVec.getZ());

                                BlockData blockData = new BlockData(xMaterial.get().parseMaterial(), xMaterial.get().getData());

                                blockLocations.put(targetLoc, blockData);
                            }
                        }
                    }
                }

                List<Location> keys = new ArrayList<>(blockLocations.keySet());

                new BukkitRunnable() {
                    int index = 0;
                    boolean done = false;
                    @Override
                    public void run() {
                        if (blockLocations.isEmpty()){
                            cancel();
                        }

                        if (index >= blockLocations.size()){
                            if (done){
                                cancel();
                            } else {
                                index = 0;
                                done = true;
                            }
                        } else {
                            if (!done){
                                Location loc = keys.get(index);
                                Material material = blockLocations.get(loc).getMaterial();
                                byte data = blockLocations.get(loc).getData();

                                player.sendBlockChange(loc, material, data);

                                index++;
                            } else {
                                Location loc = keys.get(index);

                                player.sendBlockChange(loc, Material.AIR, (byte) 0);

                                index++;
                            }
                        }
                    }
                }.runTaskTimerAsynchronously(HCore.getInstance(), 0L, 0L);
            }

        } catch (DataException | IOException e) {
            Bukkit.getLogger().severe("ERROR! ISLANDTOPPERPREVIEW, sendIslandTopper() method!");
        }
    }

    private String rpGetPlayerDirection(Player playerSelf){
        String dir;
        float y = playerSelf.getLocation().getYaw();
        if ( y < 0 ){y += 360;}
        y %= 360;
        int i = (int)((y+8) / 22.5);
        if (i == 0){dir = "west";}
        else if (i == 1){dir = "west northwest";}
        else if (i == 2){dir = "northwest";}
        else if (i == 3){dir = "north northwest";}
        else if (i == 4){dir = "north";}
        else if (i == 5){dir = "north northeast";}
        else if (i == 6){dir = "northeast";}
        else if (i == 7){dir = "east northeast";}
        else if (i == 8){dir = "east";}
        else if (i == 9){dir = "east southeast";}
        else if (i == 10){dir = "southeast";}
        else if (i == 11){dir = "south southeast";}
        else if (i == 12){dir = "south";}
        else if (i == 13){dir = "south southwest";}
        else if (i == 14){dir = "southwest";}
        else if (i == 15){dir = "west southwest";}
        else {dir = "west";}
        return dir;
    }

    private static CuboidClipboard flipDirection(CuboidClipboard clipboard, String direction) {
        switch (direction.toUpperCase()) {
            case "NORTH":
                clipboard.rotate2D(180);
                break;
            case "EAST":
                clipboard.rotate2D(270);
                break;
            case "WEST":
                clipboard.rotate2D(90);
                break;
            }
            return clipboard;
        }
}