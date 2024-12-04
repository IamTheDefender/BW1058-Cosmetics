package xyz.iamthedefender.cosmetics.api.cosmetics;


import lombok.Getter;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;

import java.util.List;

@Getter
public enum CosmeticsType {
    FinalKillEffects("Final Kill Effect", ConfigUtils.getFinalKillEffects(), Utility.getApi().getFinalKillList(), "finalkill-effect", "finalkilleffect"),
    ProjectileTrails("Projectile Trail", ConfigUtils.getProjectileTrails(), Utility.getApi().getProjectileTrailList(), "projectile-trails", "projectiletrail"),
    BedBreakEffects("Bed Destroy", ConfigUtils.getBedDestroys(), Utility.getApi().getBedDestroyList(), "bed-destroy", "beddestroy"),
    Glyphs("Glyph", ConfigUtils.getGlyphs(), Utility.getApi().getGlyphsList(), "glyph", "glyph"),
    DeathCries("Death Cry", ConfigUtils.getDeathCries(), Utility.getApi().getDeathCryList(), "death-cry", "deathcry"),
    VictoryDances("Victory Dance", ConfigUtils.getVictoryDances(), Utility.getApi().getVictoryDanceList(), "victory-dance", "victorydance"),
    WoodSkins("Wood Skin", ConfigUtils.getWoodSkins(), Utility.getApi().getWoodSkinList(), "wood-skins", "woodskin"),
    Sprays("Spray", ConfigUtils.getSprays(), Utility.getApi().getSprayList(), "sprays", "spray"),
    KillMessage("Kill Message", ConfigUtils.getKillMessages(), Utility.getApi().getKillMessageList(), "kill-message", "killmessage"),
    ShopKeeperSkin("ShopKeeper Skin", ConfigUtils.getShopKeeperSkins(), Utility.getApi().getShopKeeperSkinList(), "shopkeeper-skins", "shopkeeperskin"),
    IslandTopper("Island Topper", ConfigUtils.getIslandToppers(), Utility.getApi().getIslandTopperList(), "island-topper", "islandtopper");

    private final String formatedName;
    private final ConfigManager configManager;
    private final List<?> itemsList;
    private final String sectionKey;
    private final String permissionFormat;
    CosmeticsType(String formatedName, ConfigManager configManager, List<?> itemsList, String sectionKey, String permissionFormat){
        this.formatedName = formatedName;
        this.configManager = configManager;
        this.itemsList = itemsList;
        this.sectionKey = sectionKey;
        this.permissionFormat = permissionFormat;
    }

    public ConfigManager getConfig() {
        return configManager;
    }

}
