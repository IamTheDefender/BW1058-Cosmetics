package xyz.iamthedefender.cosmetics.category.shopkeeperskins.items;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.ShopKeeperSkin;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.utils.ShopKeeperSkinsUtils;
import xyz.iamthedefender.cosmetics.util.StartupUtils;
import xyz.iamthedefender.cosmetics.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShopKeeperItems {

    public void registerItems(){
        ConfigurationSection section = CosmeticsType.ShopKeeperSkin.getConfig().getYml().getConfigurationSection(CosmeticsType.ShopKeeperSkin.getSectionKey());
        if (section == null) return;
        ConfigManager config = CosmeticsType.ShopKeeperSkin.getConfig();
        for(String id : section.getKeys(false)){
            String path = CosmeticsType.ShopKeeperSkin.getSectionKey() + "." + id + ".";
            ShopKeeperSkin shopKeeperSkin = new ShopKeeperSkin() {
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
                    if (getRarity() == RarityType.NONE){
                        return List.of("&7Selecting this option disables your", "&7ShopKeeper Skin.");
                    }
                    return List.of("&7Select " + getDisplayName()  + " as your", "&7shopkeeper skin!");
                }

                @Override
                public int getPrice() {
                    return config.getInt(path + "price");
                }

                @Override
                public RarityType getRarity() {
                    return RarityType.valueOf(config.getString(path + "rarity").toUpperCase());
                }

                @Override
                public void execute(Player player, Location shopLocation, Location upgradeLocation) {
                    if (getField(FieldsType.RARITY, player) == RarityType.RANDOM){
                        List<ShopKeeperSkin> shopKeeperSkins = new ArrayList<>();
                        for (ShopKeeperSkin shopKeeperSkin : StartupUtils.shopKeeperSkinList) {
                            if (player.hasPermission(CosmeticsType.ShopKeeperSkin.getPermissionFormat() + "." + shopKeeperSkin.getIdentifier())){
                                shopKeeperSkins.add(shopKeeperSkin);
                            }
                        }
                        if (shopKeeperSkins.isEmpty()){
                            // ShopKeeperSkin#getDefault should not return null!
                            ShopKeeperSkinsUtils.spawnShopKeeperNPC(player, shopLocation, upgradeLocation, ShopKeeperSkin.getDefault(player).getIdentifier());
                        }else{
                            ShopKeeperSkin shopKeeperSkin1 = shopKeeperSkins.get(new Random().nextInt(shopKeeperSkins.size()));
                            ShopKeeperSkinsUtils.spawnShopKeeperNPC(player, shopLocation, upgradeLocation, shopKeeperSkin1.getIdentifier());
                        }
                         return;
                    }
                    ShopKeeperSkinsUtils.spawnShopKeeperNPC(player, shopLocation, upgradeLocation);
                }
            };
            shopKeeperSkin.register();
        }
    }
}
