package xyz.iamthedefender.cosmetics.category.sprays.preview;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.cryptomorin.xseries.XSound;
import xyz.iamthedefender.cosmetics.api.cosmetics.*;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
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
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.Spray;
import xyz.iamthedefender.cosmetics.api.menu.SystemGui;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.category.sprays.util.SpraysUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static xyz.iamthedefender.cosmetics.util.StartupUtils.getCosmeticLocation;
import static xyz.iamthedefender.cosmetics.util.StartupUtils.getPlayerLocation;

public class SprayPreview extends CosmeticPreview {

    private final Map<UUID, Map<Integer, org.bukkit.inventory.ItemStack>> inventories = new HashMap<>();

    private ItemFrame frame;
    private MapView view;

    private PacketAdapter adapter;

    public SprayPreview(CosmeticsType type) {
        super(type);
    }

    @Override
    public void showPreview(Player player, xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics selected, Location previewLocation, Location playerLocation) throws IllegalArgumentException{
        if (!(selected instanceof Spray)) return;

        Spray spray = (Spray) selected;

        if (spray.getField(FieldsType.RARITY, player) == RarityType.NONE) {
            XSound.ENTITY_VILLAGER_NO.play(player, 1.0f, 1.0f);
            return;
        }

        if (previewLocation == null || playerLocation == null) {
            throw new IllegalArgumentException("Preview location or Player location is not set!");
        }

        handleLocation(player, playerLocation);

        ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(playerLocation, EntityType.ARMOR_STAND);
        as.setVisible(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                100, 2));

        for (Player player1 : Bukkit.getOnlinePlayers()) {
            if (player1.equals(player)) continue;

            player1.hidePlayer(player);
        }

        sendFrame(player, selected, playerLocation);

        PacketContainer cameraPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        cameraPacket.getIntegers().write(0, as.getEntityId());

        PacketContainer resetPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CAMERA);
        resetPacket.getIntegers().write(0, player.getEntityId());
        CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, cameraPacket);


        setOnEnd(player, () -> {
            if (!as.isDead()) as.remove();

            frame.setItem(new ItemStack(Material.AIR));
            frame.remove();
            CosmeticsPlugin.getInstance().getProtocolManager().removePacketListener(adapter);
            CosmeticsPlugin.getInstance().getProtocolManager().sendServerPacket(player, resetPacket);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        });
    }

    public void sendFrame(Player player, Cosmetics selected, Location playerLocation) {
        view = Bukkit.createMap(player.getWorld());
        playerLocation = playerLocation.clone();

        /* Old spray preview code
        CustomRenderer renderer = new CustomRenderer();
        ConfigManager config = ConfigUtils.getSprays();

        String sprayFile = config.getString(CosmeticsType.Sprays.getSectionKey() + "." + selected + ".file");
        String sprayURL = config.getString(CosmeticsType.Sprays.getSectionKey() + "." + selected + ".url");
        if (sprayFile == null) {
            if (!renderer.load(sprayURL)) {
                player.sendMessage(ColorUtil.translate("&cLooks like there's an error rendering the Spray, contact the admin!"));
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not load the URL for the " + selected + " Check if the URL in Sprays.yml is valid!");
                return;
            }
        } else {
            File file = new File(Cosmetics.getInstance().getHandler().getAddonPath() + "/" + Cosmetics.getInstance().getConfig().getString("Spray-Dir") + "/" + sprayFile);
            if (!renderer.load(file)) {
                player.sendMessage(ColorUtil.translate("&cLooks like there's an error rendering the Spray, contact the admin!"));
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not load the File for the " + selected + " Check if the File in Sprays.yml is valid!");
                Logger.getLogger("Minecraft").log(Level.SEVERE, "FilePath: " + file.getAbsolutePath());
                return;
            }
        }

        view.addRenderer(renderer);
         */

        playerLocation.setPitch(0);
        playerLocation.add(0, 1.5, 0);
        Location firstBlock = playerLocation.clone().add(playerLocation.getDirection().multiply(2));
        firstBlock.getBlock().setType(Material.BARRIER);
        firstBlock.getChunk().load(true);
        frame = (ItemFrame) player.getWorld().spawnEntity(firstBlock.getBlock().getRelative(getCardinalDirection(playerLocation)).getLocation(), EntityType.ITEM_FRAME);

        adapter = new PacketAdapter(CosmeticsPlugin.getInstance(), PacketType.Play.Server.SPAWN_ENTITY) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacket().getIntegers().read(0) == frame.getEntityId() &&
                !event.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        };

        CosmeticsPlugin.getInstance().getProtocolManager().addPacketListener(adapter);
        frame.setFacingDirection(getCardinalDirection(playerLocation), true);
        SpraysUtil.spawnSprays(player, frame, true, (Spray) selected);

        XSound.ENTITY_SILVERFISH_HURT.play(player, 10f, 10f);

        Run.every((r) -> {
            if(frame.isDead() || !frame.isValid()){
                firstBlock.getBlock().setType(Material.AIR);
                r.cancel();
            }
        }, 10L);
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