package xyz.iamthedefender.cosmetics.category.glyphs.items;

import com.hakan.core.HCore;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.Glyph;
import xyz.iamthedefender.cosmetics.category.glyphs.util.glyphUtil;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GlyphItems {

    public void registerConfigItems(){
        FileConfiguration config = ConfigUtils.getGlyphs().getYml();
        ConfigManager configManager = ConfigUtils.getGlyphs();
        ConfigurationSection glyphSection = config.getConfigurationSection("glyph");

        for(String identifier :  glyphSection.getKeys(false)){
            String path = "glyph." + identifier + ".";
            Glyph glyphs = new Glyph() {
                @Override
                public ItemStack getItem() {
                    return configManager.getItemStack(path + "item");
                }

                @Override
                public String base64() {
                    return null;
                }

                @Override
                public String getIdentifier() {
                    return identifier;
                }

                @Override
                public String getDisplayName() {
                    return replaceHyphens(identifier);
                }

                @Override
                public List<String> getLore() {
                    if (getRarity() == RarityType.NONE){
                        return List.of("&7Selecting this option disables your", "&7Glyph.");
                    }
                    return List.of("&7Select " + getDisplayName() + " as your Glyph!");
                }

                @Override
                public int getPrice() {
                    return config.getInt(path + "price");
                }

                @Override
                public RarityType getRarity() {
                    return RarityType.valueOf(config.getString(path + "rarity").toUpperCase().toUpperCase());
                }

                @Override
                public void execute(Player player, Location location) {
                    String fileLocation = config.getString(path + "file");
                    File file = new File(Cosmetics.getInstance().getHandler().getAddonPath() + "/Glyphs/" + fileLocation);
                    HCore.asyncScheduler().every(100, TimeUnit.MILLISECONDS).limit(10).run(()-> {
                        glyphUtil.sendGlyphs(file, location);
                            });
                }
            };
            glyphs.register();
        }
    }

    public String replaceHyphens(String str) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : str.toCharArray()) {
            if (c == '-') {
                result.append(' ');
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }
}
