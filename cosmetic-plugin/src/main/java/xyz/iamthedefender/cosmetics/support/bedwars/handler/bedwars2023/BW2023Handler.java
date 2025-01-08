package xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars2023;

import com.tomkeuper.bedwars.api.BedWars;
import com.tomkeuper.bedwars.api.language.Language;
import com.tomkeuper.bedwars.api.server.ServerType;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.handler.*;
import xyz.iamthedefender.cosmetics.category.bedbreakeffects.BedDestroyHandler2023;
import xyz.iamthedefender.cosmetics.category.deathcries.DeathCryHandler2023;
import xyz.iamthedefender.cosmetics.category.finalkilleffects.FinalKillEffectHandler2023;
import xyz.iamthedefender.cosmetics.category.glyphs.GlyphHandler2023;
import xyz.iamthedefender.cosmetics.category.islandtoppers.IslandTopperHandler2023;
import xyz.iamthedefender.cosmetics.category.killmessage.KillMessageHandler2023;
import xyz.iamthedefender.cosmetics.category.projectiletrails.ProjectileHandler;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.ShopKeeperHandler2023;
import xyz.iamthedefender.cosmetics.category.sprays.SpraysHandler2023;
import xyz.iamthedefender.cosmetics.category.victorydance.VictoryDanceHandler2023;
import xyz.iamthedefender.cosmetics.category.woodskin.WoodSkinHandler2023;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class BW2023Handler implements IHandler {

    private final BedWars api = com.tomkeuper.bedwars.BedWars.getAPI();

    @Override
    public void register() {
        Cosmetics plugin = Cosmetics.getInstance();

        BedWars2023 bedWars2023 = new BedWars2023(plugin);
        bedWars2023.start();

        StartupUtils.registerListeners(new ProjectileHandler(plugin));
        StartupUtils.registerListeners(new WoodSkinHandler2023());
        StartupUtils.registerListeners(new VictoryDanceHandler2023());
        StartupUtils.registerListeners(new ShopKeeperHandler2023());
        StartupUtils.registerListeners(new KillMessageHandler2023());
        StartupUtils.registerListeners(new IslandTopperHandler2023());
        StartupUtils.registerListeners(new GlyphHandler2023());
        StartupUtils.registerListeners(new FinalKillEffectHandler2023());
        StartupUtils.registerListeners(new BedDestroyHandler2023());
        StartupUtils.registerListeners(new SpraysHandler2023());
        StartupUtils.registerListeners(new DeathCryHandler2023());
    }

    @Override
    public HandlerType getHandlerType() {
        if (api.getServerType() == ServerType.SHARED) return HandlerType.SHARED;
        if (api.getServerType() == ServerType.BUNGEE) return HandlerType.BUNGEE;
        return HandlerType.MULTIARENA;
    }


    @Override
    public IArenaUtil getArenaUtil() {
        return player -> {
            if (api.getArenaUtil().getArenaByPlayer(player) == null) return null;

            return (IArenaHandler) player1 -> new ITeamHandler() {
                @Override
                public Location getBed() {
                    return api.getArenaUtil().getArenaByPlayer(player1).getTeam(player1).getBed().getBlock().getLocation();
                }

                @Override
                public List<Player> getPlayers() {
                    return api.getArenaUtil().getArenaByPlayer(player1).getTeam(player1).getMembers();
                }

                @Override
                public String getName() {
                    return api.getArenaUtil().getArenaByPlayer(player1).getTeam(player1).getName();
                }

                @Override
                public Location getTeamUpgrades() {
                    return api.getArenaUtil().getArenaByPlayer(player1).getTeam(player1).getTeamUpgrades();
                }

                @Override
                public Location getShop() {
                    return api.getArenaUtil().getArenaByPlayer(player1).getTeam(player1).getShop();
                }

                @Override
                public Location getSpawn() {
                    return api.getArenaUtil().getArenaByPlayer(player1).getTeam(player1).getSpawn();
                }

                @Override
                public int getSize() {
                    return api.getArenaUtil().getArenaByPlayer(player1).getTeam(player1).getSize();
                }
            };
        };
    }

    @Override
    public IScoreboardUtil getScoreboardUtil() {
        return new IScoreboardUtil() {

            @Override
            public void giveScoreboard(Player player, boolean b) {
                api.getScoreboardUtil().givePlayerScoreboard(player, b);
            }

            @Override
            public void removePlayerScoreboard(Player player) {
                api.getScoreboardUtil().removePlayerScoreboard(player);
            }
        };
    }

    @Override
    public ILanguage getLanguageUtil() {
        return new ILanguage() {
            @Override
            public String getMessage(Player player, String path) {
                return api.getPlayerLanguage(player).getString(path);
            }

            @Override
            public List<String> getMessageList(Player player, String path) {
                return api.getPlayerLanguage(player).getList(path);
            }

            @Override
            public void saveIfNotExists(String path, Object data) {
                Language.saveIfNotExists(path, data);
            }
        };
    }

    @Override
    public String getAddonPath() {
        return api.getAddonsPath().getPath() + File.separator + Cosmetics.getInstance().getDescription().getName();
    }

    @Override
    public ISetupSession getSetupSession(UUID playerUUID) {
        com.tomkeuper.bedwars.api.server.ISetupSession session = api.getSetupSession(playerUUID);
        if (session == null) return null;
        ISetupSession cosmeticsSessionHandler = new ISetupSession() {
            @Override
            public UUID getPlayerUUID() {
                return session.getPlayer().getUniqueId();
            }

            @Override
            public FileConfiguration getConfig() {
                return session.getConfig().getYml();
            }

            @Override
            public void saveConfigLoc(String path, Location value) {
                session.getConfig().saveConfigLoc(path, value);
            }

            @Override
            public void saveConfig() {
                session.getConfig().save();
            }
        };
        return cosmeticsSessionHandler;
    }
}
