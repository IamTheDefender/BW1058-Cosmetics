package xyz.iamthedefender.cosmetics.category.killmessage.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.KillMessage;
import xyz.iamthedefender.cosmetics.util.StringUtils;

import java.util.List;

public class KillMessageItems {

    public static void registerConfigItems(){
        ConfigurationSection section = CosmeticsType.KillMessage.getConfig().getYml().getConfigurationSection(CosmeticsType.KillMessage.getSectionKey());
        if (section == null) return;
        ConfigManager config = CosmeticsType.KillMessage.getConfig();

        for(String id : section.getKeys(false)){
            try {
                String path = CosmeticsType.KillMessage.getSectionKey() + "." + id + ".";
                KillMessage killMessage = new KillMessage() {
                    // Do nothing because API currently doesn't support it
                    @Override
                    public String execute(Player player) {
                        return null;
                    }

                    @Override
                    public ItemStack getItem() {
                        return config.getItemStack(path + "item");
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
                        return StringUtils.replaceHyphensAndCaptalizeFirstLetter(id);
                    }

                    @Override
                    public List<String> getLore() {
                        return List.of("&7Select the " + getDisplayName() + " Kill,", "&7Message for in-game chat", "&7messages!");
                    }

                    @Override
                    public int getPrice() {
                        return config.getInt(path + "price");
                    }

                    @Override
                    public RarityType getRarity() {
                        return RarityType.valueOf(config.getString(path + "rarity").toUpperCase());
                    }
                };
                killMessage.register();
            }catch (Exception e){
                Cosmetics.getInstance().getLogger().warning("Error while loading KillMessage: " + id);
            }
        }
    }
}
