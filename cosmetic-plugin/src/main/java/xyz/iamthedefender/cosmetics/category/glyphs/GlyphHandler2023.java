package xyz.iamthedefender.cosmetics.category.glyphs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.Glyph;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

public class GlyphHandler2023 implements Listener {

    @EventHandler
    public void onGenCollect2023(com.tomkeuper.bedwars.api.events.player.PlayerGeneratorCollectEvent e) {

        boolean isGlyphsEnabled = Cosmetics.getInstance().getConfig().getBoolean("glyphs.enabled");
        if (!isGlyphsEnabled) return;

        if (e.getItemStack().getType() == Material.DIAMOND || e.getItemStack().getType() == Material.EMERALD) {
            String selected = Cosmetics.getInstance().getApi().getSelectedCosmetic(e.getPlayer(), CosmeticsType.Glyphs);

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
