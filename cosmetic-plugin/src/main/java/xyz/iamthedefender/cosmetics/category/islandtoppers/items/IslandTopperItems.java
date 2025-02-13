package xyz.iamthedefender.cosmetics.category.islandtoppers.items;

import com.sk89q.worldedit.MaxChangedBlocksException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.IslandTopper;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import xyz.iamthedefender.cosmetics.category.islandtoppers.util.IslandToppersUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IslandTopperItems {

    public void registerItems() {
        FileConfiguration config = ConfigUtils.getIslandToppers().getYml();
        ConfigManager configManager = ConfigUtils.getIslandToppers();
        ConfigurationSection topperSection = config.getConfigurationSection("island-topper");
        for(String id : topperSection.getKeys(false)){
            String path = "island-topper." + id + ".";
            IslandTopper islandTopper = new IslandTopper() {
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
                    return id;
                }

                @Override
                public String getDisplayName() {
                    return replaceHyphens(id);
                }

                @Override
                public List<String> getLore() {
                    if (getRarity() == RarityType.NONE){
                        return List.of("&7Selecting this option disables your", "&7Island Topper.");
                    }
                    return List.of("&7Select " + getDisplayName() + " as your Island Topper!");
                }

                @Override
                public int getPrice() {
                    return configManager.getInt(path + "price");
                }

                @Override
                public RarityType getRarity() {
                    return RarityType.valueOf(config.getString(path + "rarity").toUpperCase());
                }

                @Override
                public void execute(Player player, Location topperLocation, String selected) {
                    if (selected.equals("none")) return;
                    String fileName = ConfigUtils.getIslandToppers().getString("island-topper." + selected + ".file");
                    if (fileName == null){
                        Bukkit.getLogger().severe("Can't find file for " + selected + " island topper!");
                        return;
                    }
                    File file = new File(CosmeticsPlugin.getInstance().getHandler().getAddonPath() + "/IslandToppers/" + fileName);
                    if (!file.exists()){
                        Bukkit.getLogger().severe("The file " + file.getName() + " does not exists!");
                        return;
                    }
                    try {
                        IslandToppersUtil.sendIslandTopper(topperLocation.getWorld(), topperLocation, player, file);
                    } catch (MaxChangedBlocksException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            islandTopper.register();
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
