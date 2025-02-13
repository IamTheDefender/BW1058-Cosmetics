

package xyz.iamthedefender.cosmetics.category.sprays.util;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.Spray;
import xyz.iamthedefender.cosmetics.api.handler.IArenaHandler;
import xyz.iamthedefender.cosmetics.api.particle.ParticleWrapper;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpraysUtil
{
    public static HashMap<String, Long> cooldown = new HashMap<>();

    /**
     * Spawn a spray for a player in an item frame.
     *
     * @param player The player for whom to spawn the spray.
     * @param itemFrame The item frame in which to display the spray.
     */
    public static void spawnSprays(Player player, ItemFrame itemFrame, boolean isPreview, Spray selectedSpray) {
        Run.sync(() -> {
            MapView view = Bukkit.createMap(player.getWorld());

            ConfigManager config = ConfigUtils.getSprays();
            if (SpraysUtil.cooldown.containsKey(player.getName()) && !isPreview) {
                // Player is in cooldown
                long cooldownEndTime = SpraysUtil.cooldown.get(player.getName());
                if (cooldownEndTime > System.currentTimeMillis() && cooldownEndTime != 0) {
                    player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0f, 1.0f);
                    player.sendMessage(ColorUtil.translate(Utility.getMSGLang(player, "cosmetics.spray-msg")));
                    return;
                }
                SpraysUtil.cooldown.remove(player.getName());
            }
            DebugUtil.addMessage("Playing " + selectedSpray.getIdentifier() + " Spray for " + player.getDisplayName() + " is preview: " + isPreview);
            if (!isPreview) {
                IArenaHandler arenaHandler = CosmeticsPlugin.getInstance().getHandler().getArenaUtil().getArenaByPlayer(player);
                if (arenaHandler == null) return;
                SpraysUtil.cooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
            }
            DebugUtil.addMessage("Check 1");

            view.removeRenderer(view.getRenderers().get(0));
            final CustomRenderer renderer = new CustomRenderer();

            String sprayUrl = selectedSpray.getField(FieldsType.URL, player).toString();
            String sprayFile = selectedSpray.getField(FieldsType.FILE, player).toString();

            DebugUtil.addMessage("Playing " + selectedSpray.getIdentifier() + " Spray for " + player.getDisplayName());

            if (sprayFile == null) {
                sprayFile = selectedSpray.getIdentifier() + "." + FileUtil.getFileExtension(sprayUrl);
            } else {
                sprayFile = selectedSpray.getField(FieldsType.FILE, player).toString();
            }


            File file = new File(CosmeticsPlugin.getInstance().getHandler().getAddonPath() + "/" + CosmeticsPlugin.getInstance().getConfig().getString("Spray-Dir") + "/" + sprayFile);
            if (!renderer.load(file)) {
                player.sendMessage(ColorUtil.translate("&cLooks like there's an error rendering the Spray, contact the admin!"));
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not load the File for the " + selectedSpray.getIdentifier() + ". Check if the File in Sprays.yml is valid: " + file.getPath());
            } else {
                addRendererAndShowSpray(player, itemFrame, renderer, view, isPreview);
            }
        });
    }

    /**
     * Add the renderer to the map view and display the spray.
     *
     * @param player The player for whom to display the spray.
     * @param itemFrame The item frame in which to display the spray.
     * @param renderer The custom renderer to add.
     * @param view The map view to render.
     */
    private static void addRendererAndShowSpray(Player player, ItemFrame itemFrame, CustomRenderer renderer, MapView view, boolean isPreview) {
        ItemStack map = CosmeticsPlugin.getInstance().getApi().getVersionSupport().applyRenderer(renderer, view);
        itemFrame.setItem(map);
        itemFrame.setRotation(Rotation.NONE);
        if(isPreview){
            player.getInventory().addItem(map);
            player.getInventory().setItem(0, map);
        }

        ParticleWrapper particleWrapper = ParticleWrapper.getParticle("FALLING_DUST").orElse(ParticleWrapper.getParticle("BLOCK_DUST").orElse(null));

        if (particleWrapper != null) {
            particleWrapper.support().displayParticle(
                    player,
                    itemFrame.getLocation(),
                    particleWrapper,
                    10,
                    0.0f
            );
        }

        itemFrame.getNearbyEntities(1,1,1)
                .stream()
                .filter(entity -> entity.getType() == EntityType.ARMOR_STAND && entity.hasMetadata("HOLO_ITEM_FRAME"))
                .forEach(Entity::remove);
    }

}


