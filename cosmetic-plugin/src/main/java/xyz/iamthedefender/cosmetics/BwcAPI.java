package xyz.iamthedefender.cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.iamthedefender.cosmetics.api.CosmeticsAPI;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticPreview;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.*;
import xyz.iamthedefender.cosmetics.api.database.IDatabase;
import xyz.iamthedefender.cosmetics.api.handler.IHandler;
import xyz.iamthedefender.cosmetics.api.menu.SystemGuiManager;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;
import xyz.iamthedefender.cosmetics.data.PlayerData;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.List;

public class BwcAPI implements CosmeticsAPI {

    /**
     * Check if MySQL is enabled.
     * @return true if enabled.
     */
    public boolean isMySQL() {
        return  CosmeticsPlugin.getInstance().getConfig().getBoolean("mysql.enable");
    }

    @Override
    public IHandler getHandler() {
        return CosmeticsPlugin.getInstance().getHandler();
    }

    @Override
    public SystemGuiManager getSystemGuiManager() {
        return CosmeticsPlugin.getInstance().getSystemGuiManager();
    }

    @Override
    public JavaPlugin getPlugin() {
        return CosmeticsPlugin.getInstance();
    }

    @Override
    public ConfigManager getMenuData() {
        return CosmeticsPlugin.getInstance().menuData;
    }

    @Override
    public List<BedDestroy> getBedDestroyList() {
        return StartupUtils.bedDestroyList;
    }

    @Override
    public List<DeathCry> getDeathCryList() {
        return StartupUtils.deathCryList;
    }

    @Override
    public List<FinalKillEffect> getFinalKillList() {
        return StartupUtils.finalKillList;
    }

    @Override
    public List<ProjectileTrail> getProjectileTrailList() {
        return StartupUtils.projectileTrailList;
    }

    @Override
    public List<Glyph> getGlyphsList() {
        return StartupUtils.glyphsList;
    }

    @Override
    public List<VictoryDance> getVictoryDanceList() {
        return StartupUtils.victoryDancesList;
    }

    @Override
    public List<WoodSkin> getWoodSkinList() {
        return StartupUtils.woodSkinsList;
    }

    @Override
    public List<Spray> getSprayList() {
        return StartupUtils.sprayList;
    }

    @Override
    public List<KillMessage> getKillMessageList() {
        return StartupUtils.killMessageList;
    }

    @Override
    public List<ShopKeeperSkin> getShopKeeperSkinList() {
        return StartupUtils.shopKeeperSkinList;
    }

    @Override
    public List<IslandTopper> getIslandTopperList() {
        return StartupUtils.islandTopperList;
    }

    @Override
    public IVersionSupport getVersionSupport() {
        return CosmeticsPlugin.getInstance().getVersionSupport();
    }

    @Override
    public List<CosmeticPreview> getPreviewList() {
        return CosmeticsPlugin.getInstance().getPreviewList();
    }

    /**
     * Get the selected cosmetic for a player.
     * @param p   Player
     * @param cos Cosmetic type
     * @return    String
     */
    public String getSelectedCosmetic(Player p, CosmeticsType cos){
        if (p == null){
            return null;
        }
        PlayerData playerData = CosmeticsPlugin.getInstance().getPlayerManager().getPlayerData(p.getUniqueId());
        switch (cos){
            case BedBreakEffects:
                return playerData.getBedDestroy();
            case DeathCries:
                return playerData.getDeathCry();
            case FinalKillEffects:
                return playerData.getFinalKillEffect();
            case Glyphs:
                return playerData.getGlyph();
            case IslandTopper:
                return playerData.getIslandTopper();
            case KillMessage:
                return playerData.getKillMessage();
            case ProjectileTrails:
                return playerData.getProjectileTrail();
            case ShopKeeperSkin:
                return playerData.getShopkeeperSkin();
            case Sprays:
                return playerData.getSpray();
            case VictoryDances:
                return playerData.getVictoryDance();
            case WoodSkins:
                return playerData.getWoodSkin();
        }
        return "User not found!";
    }

    /**
     * Set the selected cosmetic for a player.
     * @param p     Player.
     * @param cos   Cosmetic type.
     * @param value Cosmetic value.
     */
    public void setSelectedCosmetic(Player p, CosmeticsType cos, String value){
        PlayerData playerData = CosmeticsPlugin.getInstance().getPlayerManager().getPlayerData(p.getUniqueId());
        switch (cos){
            case BedBreakEffects:
                playerData.setBedDestroy(value);
                break;
            case DeathCries:
                playerData.setDeathCry(value);
                break;
            case FinalKillEffects:
                playerData.setFinalKillEffect(value);
                break;
            case Glyphs:
                DebugUtil.addMessage("Glyph: " + value);
                playerData.setGlyph(value);
                break;
            case IslandTopper:
                playerData.setIslandTopper(value);
                break;
            case KillMessage:
                playerData.setKillMessage(value);
                break;
            case ProjectileTrails:
                playerData.setProjectileTrail(value);
                break;
            case ShopKeeperSkin:
                playerData.setShopkeeperSkin(value);
                break;
            case Sprays:
                playerData.setSpray(value);
                break;
            case VictoryDances:
                playerData.setVictoryDance(value);
                break;
            case WoodSkins:
                playerData.setWoodSkin(value);
                break;
        }
        
        Run.async(playerData::save);
    }

    /**
     * Check if the plugin is running on a proxy.
     * @return true if running on a proxy.
     */
    public boolean isProxy(){
        return Bukkit.getPluginManager().getPlugin("BedWarsProxy") != null ||
                Bukkit.getPluginManager().getPlugin("BWProxy2023") != null;
    }

    public IDatabase getDatabase(){
        return CosmeticsPlugin.getInstance().getRemoteDatabase();
    }
}