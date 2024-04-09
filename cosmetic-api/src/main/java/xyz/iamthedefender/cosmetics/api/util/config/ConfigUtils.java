package xyz.iamthedefender.cosmetics.api.util.config;


import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.util.Utility;

import java.util.Arrays;
import java.util.Objects;

public class ConfigUtils {

    public static ConfigManager getBedDestroys() {
        return new ConfigManager(Utility.getPlugin(), "BedBreakEffect", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getDeathCries() {
        return new ConfigManager(Utility.getPlugin(), "DeathCries", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getFinalKillEffects() {
        return new ConfigManager(Utility.getPlugin(), "FinalKillEffects", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getGlyphs() {
        return new ConfigManager(Utility.getPlugin(), "Glyphs", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getIslandToppers() {
        return new ConfigManager(Utility.getPlugin(), "IslandToppers", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getKillMessages() {
        return new ConfigManager(Utility.getPlugin(), "KillMessages", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getProjectileTrails() {
        return new ConfigManager(Utility.getPlugin(), "ProjectileTrails", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getShopKeeperSkins() {
        return new ConfigManager(Utility.getPlugin(), "ShopKeeperSkins", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getSprays() {
        return new ConfigManager(Utility.getPlugin(), "Sprays", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getVictoryDances() {
        return new ConfigManager(Utility.getPlugin(), "VictoryDances", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getWoodSkins() {
        return new ConfigManager(Utility.getPlugin(), "WoodSkins", Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }


    public static ConfigManager get(ConfigType configType){
        if (configType == ConfigType.Main_Config){
            return getMainConfig();
        }
        if (configType == ConfigType.Main_Menu){
            return Utility.getApi().getMenuData();
        }
        return new ConfigManager(Utility.getPlugin(), configType.getFileName(), Utility.getApi().getHandler().getAddonPath() + "/Categories");
    }

    public static ConfigManager getMainConfig(){
        return new ConfigManager(Utility.getPlugin(), "config", Utility.getApi().getHandler().getAddonPath());
    }

    public static void saveIfNotFound(ConfigType configType, String path ,Object data){
        if (Objects.isNull(get(configType).getYml().get(path))){
            get(configType).set(path, data);
            get(configType).save();
            get(configType).reload();
        }
    }

    public static void addSlotsList(){
        for (CosmeticsType cosmeticsType : CosmeticsType.values()){
            cosmeticsType.getConfig().getYml().addDefault("slots", Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34).toString());

            cosmeticsType.getConfig().save();
        }
    }

    public static void addExtrasToLang(){
        Utility.saveIfNotExistsLang("Cosmetics.not-purchase-able", "&cLOCKED");
    }
}