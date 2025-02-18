package xyz.iamthedefender.cosmetics.api.cosmetics.category;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
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


public abstract class FinalKillEffect extends Cosmetics {
    /**
     * Execute the final kill effect
     * This method should be called when a player kills another player.
     * And the victim has no bed left.
     *
     * @param killer player who killed the victim.
     * @param victim player who was killed.
     */
    public abstract void execute(Player killer, Player victim, Location location, boolean onlyVictim);

    /**
     * Register the final kill effect.
     * This method should be called when the plugin is enabled.
     */
    @Override
    public void register(){
        // save to config
        String category = "finalkill-effect";
        String configPath = category + "." + getIdentifier() + ".";
        ConfigType type = ConfigType.FINAL_KILL_EFFECTS;
        saveIfNotFound(type, configPath + "price", getPrice());
         saveIfNotFound(type, configPath + "rarity", getRarity().toString());
        String item = null;
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
        finalLore.addAll(Arrays.asList("&8Final Kill Effects", ""));
        finalLore.addAll(getLore());
        finalLore.addAll(Arrays.asList("", "&eRight-Click to preview!", "" ,"&7Rarity: {rarity}","&7Cost: &6{cost}", "", "{status}"));

        saveIfNotExistsLang("cosmetics." + configPath + "lore", finalLore);
        Utility.getApi().getFinalKillList().add(this);
    }

    /**
     * Get the field of the final kill effect.
     * @param fields the field you want to get.
     * @param p the player who is viewing the final kill effect.
     * @return the field of the final kill effect.
     */
    public Object getField(FieldsType fields, Player p){
        String category = "finalkill-effect";
        String configPath = category + "." + getIdentifier() + ".";
        ConfigManager config = ConfigUtils.getFinalKillEffects();
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
     * Get the default final kill effect.
     * @param player the player who is viewing the final kill effect.
     * @return the default final kill effect.
     */
    public static @NotNull FinalKillEffect getDefault(Player player){
        for(FinalKillEffect finalKillEffect : Utility.getApi().getFinalKillList()){
            if (finalKillEffect.getField(FieldsType.RARITY, player) == RarityType.NONE){
                return finalKillEffect;
            }
        }

        // This will never return null!
        return null;
    }

    @Override
    public CosmeticsType getCosmeticType() {
        return CosmeticsType.FinalKillEffects;
    }
}
