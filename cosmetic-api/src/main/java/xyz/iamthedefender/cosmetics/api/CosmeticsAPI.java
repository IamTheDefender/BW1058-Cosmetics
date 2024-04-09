package xyz.iamthedefender.cosmetics.api;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.*;
import xyz.iamthedefender.cosmetics.api.database.IDatabase;
import xyz.iamthedefender.cosmetics.api.handler.IHandler;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;

import java.util.List;

public interface CosmeticsAPI {
    IDatabase getDatabase();
    String getSelectedCosmetic(Player player, CosmeticsType type);
    void setSelectedCosmetic(Player player, CosmeticsType type, String value);
    boolean isProxy();
    boolean isMySQL();

    IHandler getHandler();

    JavaPlugin getPlugin();

    ConfigManager getMenuData();

    List<BedDestroy> getBedDestroyList();
    List<DeathCry> getDeathCryList();
    List<FinalKillEffect> getFinalKillList();
    List<ProjectileTrail> getProjectileTrailList();
    List<Glyph> getGlyphsList();
    List<VictoryDance> getVictoryDanceList();
    List<WoodSkin> getWoodSkinList();
    List<Spray> getSprayList();
    List<KillMessage> getKillMessageList();
    List<ShopKeeperSkin> getShopKeeperSkinList();
    List<IslandTopper> getIslandTopperList();
    IVersionSupport getVersionSupport();
}
