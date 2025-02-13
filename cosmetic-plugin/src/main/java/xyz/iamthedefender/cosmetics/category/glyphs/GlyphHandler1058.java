

package xyz.iamthedefender.cosmetics.category.glyphs;

import com.andrei1058.bedwars.api.events.player.PlayerGeneratorCollectEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.Glyph;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.HashMap;

public class GlyphHandler1058 implements Listener {

	public static HashMap<Location, Integer> glyphs = new HashMap<>();

	@EventHandler
	public void onGenCollect1058(PlayerGeneratorCollectEvent e) {

		boolean isGlyphsEnabled = CosmeticsPlugin.getInstance().getConfig().getBoolean("glyphs.enabled");
		if (!isGlyphsEnabled) return;

		if (e.getItemStack().getType() == Material.DIAMOND || e.getItemStack().getType() == Material.EMERALD) {
			String selected = CosmeticsPlugin.getInstance().getApi().getSelectedCosmetic(e.getPlayer(), CosmeticsType.Glyphs);

				for (Block loc : UsefulUtilsVD.getBlocksInRadius(e.getPlayer().getLocation(), 2, false)) {
					if (loc.getType() == Material.DIAMOND_BLOCK || loc.getType() == Material.EMERALD_BLOCK) {
						DebugUtil.addMessage("Executing " + selected + " Glyph for " + e.getPlayer().getDisplayName());
						for(Glyph glyphs1 : StartupUtils.glyphsList){
							if (selected.equals(glyphs1.getIdentifier())){
								if (glyphs1.getField(FieldsType.RARITY, e.getPlayer()) == RarityType.NONE) return;
								glyphs1.execute(e.getPlayer(), loc.getLocation().add(0,8,0).subtract(2,0,0));
							}
						}
					}
				}
		}
	}
}