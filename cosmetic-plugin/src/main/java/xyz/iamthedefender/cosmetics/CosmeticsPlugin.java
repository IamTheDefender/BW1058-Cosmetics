
package xyz.iamthedefender.cosmetics;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.iamthedefender.cosmetics.api.CosmeticsAPI;
import xyz.iamthedefender.cosmetics.api.configuration.ConfigManager;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.database.DatabaseType;
import xyz.iamthedefender.cosmetics.api.database.IDatabase;
import xyz.iamthedefender.cosmetics.api.handler.HandlerType;
import xyz.iamthedefender.cosmetics.api.handler.IHandler;
import xyz.iamthedefender.cosmetics.api.menu.SystemGuiManager;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.config.ConfigUtils;
import xyz.iamthedefender.cosmetics.api.util.config.DefaultsUtils;
import xyz.iamthedefender.cosmetics.api.versionsupport.IVersionSupport;
import xyz.iamthedefender.cosmetics.command.BedWarsCosmeticsCommand;
import xyz.iamthedefender.cosmetics.data.PlayerData;
import xyz.iamthedefender.cosmetics.data.PlayerOwnedData;
import xyz.iamthedefender.cosmetics.data.database.MySQL;
import xyz.iamthedefender.cosmetics.data.database.SQLite;
import xyz.iamthedefender.cosmetics.data.manager.PlayerManager;
import xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars1058.BW1058Handler;
import xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars1058.BW1058ProxyHandler;
import xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars2023.BW2023Handler;
import xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars2023.BW2023ProxyHandler;
import xyz.iamthedefender.cosmetics.util.MainMenuUtils;
import xyz.iamthedefender.cosmetics.util.Metrics;
import xyz.iamthedefender.cosmetics.util.StartupUtils;
import xyz.iamthedefender.cosmetics.util.VersionSupportUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
@Getter
public class CosmeticsPlugin extends JavaPlugin {

    public ConfigManager menuData;
    private PlayerManager playerManager;
    private Metrics metrics;

    public boolean dependenciesMissing = false;
    @Getter
    static boolean placeholderAPI;

    @Getter
    private static CosmeticsPlugin instance;

    private ProtocolManager protocolManager;

    private HashMap<Integer, Player> entityPlayerHashMap;

    private CosmeticsAPI api;
    private Economy economy;
    private IHandler handler;
    private IVersionSupport versionSupport;
    private IDatabase remoteDatabase;

    private SystemGuiManager systemGuiManager;


    @Override
    public void onEnable() {
        instance = this;
        api = new BwcAPI();
        Bukkit.getServicesManager().register(CosmeticsAPI.class, api, this, ServicePriority.Highest);
        systemGuiManager = new SystemGuiManager(this);

        if (!StartupUtils.checkDependencies()){
            getLogger().severe("Cosmetics addon will now disable, make sure you have all dependencies installed!");
            getServer().getPluginManager().disablePlugin(this);
            dependenciesMissing = true;
            return;
        }

        handler = (api.isProxy() ? (StartupUtils.isBw2023 ? new BW2023ProxyHandler() : new BW1058ProxyHandler()) : (StartupUtils.isBw2023 ? new BW2023Handler() : new BW1058Handler()));
        StartupUtils.loadLibraries();

        versionSupport = StartupUtils.getVersionSupport();
        if(versionSupport == null){
            getLogger().severe("Could not find a version support for " + VersionSupportUtil.getVersion());
            setEnabled(false);
            dependenciesMissing = true;
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null){
            getLogger().severe("Cosmetics addon will now disable, make sure you have Vault supported Economy plugin installed!");
            getServer().getPluginManager().disablePlugin(this);
            dependenciesMissing = true;
            return;
        }

        getLogger().info("All dependencies found, continuing with plugin startup.");

        economy = rsp.getProvider();
        protocolManager = ProtocolLibrary.getProtocolManager();
        entityPlayerHashMap  = new HashMap<>();
        playerManager = new PlayerManager();
        StartupUtils.addEntityHideListener();
        // Download Glyphs
        StartupUtils.downloadGlyphs();
        ConfigUtils.getBedDestroys().save();
        ConfigUtils.getDeathCries().save();
        ConfigUtils.getFinalKillEffects().save();
        ConfigUtils.getGlyphs().save();
        ConfigUtils.getIslandToppers().save();
        ConfigUtils.getKillMessages().save();
        ConfigUtils.getProjectileTrails().save();
        ConfigUtils.getShopKeeperSkins().save();
        ConfigUtils.getSprays().save();
        ConfigUtils.getVictoryDances().save();
        ConfigUtils.getWoodSkins().save();
        ConfigUtils.getMainConfig().save();
        ConfigUtils.addExtrasToLang();

        this.menuData = new ConfigManager(this, "MainMenu", getHandler().getAddonPath());
        StartupUtils.loadLists();
        getLogger().info("Cosmetics list successfully loaded.");

        getLogger().info("Saving data to configs...");
        MainMenuUtils.saveLores();
        StartupUtils.updateConfigs();
        getLogger().info("Creating folders...");
        StartupUtils.createFolders();
        ConfigUtils.addSlotsList();

        getLogger().info("Loading data from resources in jar...");
        DefaultsUtils defaultsUtils = new DefaultsUtils();
        defaultsUtils.saveAllDefaults();
        StartupUtils.unzipSpray();

        getLogger().info("Configuration file successfully loaded.");
        getLogger().info("Loading " + (api.isMySQL() ? "MySQL" : "SQLite") + " database...");
        if (api.isMySQL()){
            remoteDatabase = new MySQL(this);
        }else{
            if (handler.getHandlerType() == HandlerType.BUNGEE){
                getLogger().severe("You cannot use SQLite in Bungee mode!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            remoteDatabase = new SQLite(this);
        }
        handler.register();
        
        getLogger().info("Registering event listeners...");
        StartupUtils.registerEvents();

        getLogger().info("Registering commands...");
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new BedWarsCosmeticsCommand());

        getLogger().info("Loading cosmetics...");
        StartupUtils.loadCosmetics();
        StartupUtils.convertSpraysURLs();
        getLogger().info("Addon have been loaded and enabled!");
        // This is a check to make sure victory dance config doesn't have any issues.
        VictoryDance.getDefault(null);

        metrics = new Metrics(this, 21340);

        Run.everyAsync(() -> {
            try (Connection connection = remoteDatabase.getConnection()){
                connection.createStatement();
            }catch (Exception e){
                remoteDatabase.connect();
            }
        }, 5L);

        Run.everyAsync(() -> {
            for (Player onlinePlayer : getServer().getOnlinePlayers()) {
                getPlayerManager().getPlayerOwnedData(onlinePlayer.getUniqueId()).updateOwned();
            }
        }, 5 * 20L);

    }

    @Override
    public void onDisable() {
        if (dependenciesMissing){
            getLogger().severe("Detected forced disable! plugin will not unload anything!");
            return;
        }
        if (remoteDatabase != null && remoteDatabase.getDatabaseType() == DatabaseType.SQLITE){
            getLogger().info("Saving player data to SQLite database...");
            getLogger().info("Please wait it may take some time!");
            for(PlayerData playerData : getPlayerManager().getPlayerDataHashMap().values()){
                playerData.save();
            }
            for (PlayerOwnedData playerOwnedData : getPlayerManager().getPlayerOwnedDataHashMap().values()) {
                playerOwnedData.save();
            }
            getLogger().info("Player data saved to SQLite database!");
        }
        try {
           if (remoteDatabase != null){
               remoteDatabase.getConnection().close();
           }
        } catch (SQLException e) {
            getLogger().severe("There was an error while closing connection to database: " + e.getMessage());
        }

        if(metrics != null) metrics.shutdown();
    }

    @Override
    public FileConfiguration getConfig() {
        return ConfigUtils.getMainConfig().getYml();
    }

    @Override
    public void reloadConfig() {
        ConfigUtils.getMainConfig().reload();
    }

    @Override
    public void saveConfig() {
        ConfigUtils.getMainConfig().save();
    }

    public static void setPlaceholderAPI(boolean placeholderAPI) {
        CosmeticsPlugin.placeholderAPI = placeholderAPI;
    }





}