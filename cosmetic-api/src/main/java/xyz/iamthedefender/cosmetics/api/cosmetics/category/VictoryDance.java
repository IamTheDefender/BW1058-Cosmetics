package xyz.iamthedefender.cosmetics.api.cosmetics.category;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigType;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.iamthedefender.cosmetics.api.util.Utility.saveIfNotExistsLang;
import static xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils.get;
import static xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils.saveIfNotFound;

public abstract class VictoryDance extends Cosmetics {


    private final String category = "victory-dance";
    ConfigManager config = ConfigUtils.getVictoryDances();
    ConfigType type = ConfigType.VICTORY_DANCES;

    /**
     * Register the victory dance
     * This method should be called when the plugin is enabled.
     */
    @Override
    public void register(){
        // save to config
        String configPath = category + "." + getIdentifier() + ".";
        saveIfNotFound(type, configPath + "price", getPrice());
        saveIfNotFound(type, configPath + "rarity", getRarity().toString());
        if (!XMaterial.matchXMaterial(getItem()).isSupported()) {
            Bukkit.getLogger().severe("The item is not supported! (Information: Category name is " + category + " and item name is " + getIdentifier());
            return;
        }
        if (XMaterial.matchXMaterial(getItem()).isSimilar(XMaterial.PLAYER_HEAD.parseItem())){
            get(type).setItemStack(configPath + "item", getItem(), base64());
        }else{
            get(type).setItemStack(configPath + "item", getItem());
        }

        // save to language file
        saveIfNotExistsLang("cosmetics." + configPath + "name", getDisplayName());
        // Format the lore
        List<String> finalLore = new ArrayList<>();
        finalLore.addAll(Arrays.asList("&8Victory Dance", ""));
        finalLore.addAll(getLore());
        finalLore.addAll(Arrays.asList("", "&7Rarity: {rarity}","&7Cost: &6{cost}", "", "{status}"));

        saveIfNotExistsLang("cosmetics." + configPath + "lore", finalLore);
        Utility.getApi().getVictoryDanceList().add(this);
    }

    /**
     * Get the topper's field
     * @param fields the field to get
     * @param p the player to get the field
     * @return the field
     */
    public Object getField(FieldsType fields, Player p){
        String configPath = category + "." + getIdentifier() + ".";

        switch (fields){
            case NAME:
                return Utility.getMSGLang(p, "cosmetics." + configPath + "name");
            case PRICE:
                return config.getInt(configPath + "price");
            case LORE:
                return Utility.getListLang(p, "cosmetics." + configPath + "lore");
            case RARITY:
                return RarityType.valueOf(config.getString(configPath + "rarity"));
            case ITEM_STACK:
                return config.getItemStack(configPath + "item");
            default:
                return null;
        }
    }

    /**
     * Play the victory dance
     * @param winner the winner of the game
     */
    public abstract void execute(Player winner);


    /**
     * Get the default victory dance
     * @param player the player to get the default victory dance
     * @return the default victory dance
     */
    public static @NotNull VictoryDance getDefault(@Nullable Player player){
        try {
            for (VictoryDance victoryDance : Utility.getApi().getVictoryDanceList()) {
                if (victoryDance.getField(FieldsType.RARITY, player) == RarityType.NONE) {
                    return victoryDance;
                }
            }
        }catch (Exception exception){
            Bukkit.getLogger().severe("There was an error with cosmetics addon config file!");
            Bukkit.getLogger().severe("Server will restart to fix this bug!");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }
        // This will never return null!
        return null;
    }
}
