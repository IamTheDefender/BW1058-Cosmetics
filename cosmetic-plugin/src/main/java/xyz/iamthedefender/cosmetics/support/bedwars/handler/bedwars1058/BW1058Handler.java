package xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars1058;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.server.ServerType;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.handler.*;
import xyz.iamthedefender.cosmetics.category.bedbreakeffects.BedDestroyHandler1058;
import xyz.iamthedefender.cosmetics.category.deathcries.DeathCryHandler1058;
import xyz.iamthedefender.cosmetics.category.finalkilleffects.FinalKillEffectHandler1058;
import xyz.iamthedefender.cosmetics.category.glyphs.GlyphHandler1058;
import xyz.iamthedefender.cosmetics.category.islandtoppers.IslandTopperHandler1058;
import xyz.iamthedefender.cosmetics.category.killmessage.KillMessageHandler1058;
import xyz.iamthedefender.cosmetics.category.projectiletrails.ProjectileHandler;
import xyz.iamthedefender.cosmetics.category.shopkeeperskins.ShopKeeperHandler1058;
import xyz.iamthedefender.cosmetics.category.sprays.SpraysHandler1058;
import xyz.iamthedefender.cosmetics.category.victorydance.VictoryDanceHandler1058;
import xyz.iamthedefender.cosmetics.category.woodskin.WoodSkinHandler1058;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class BW1058Handler implements IHandler {

    private final BedWars api = com.andrei1058.bedwars.BedWars.getAPI();

    @Override
    public void register() {
        StartupUtils.registerListeners(new ShopKeeperHandler1058());
        StartupUtils.registerListeners(new GlyphHandler1058());
        StartupUtils.registerListeners(new KillMessageHandler1058());
        StartupUtils.registerListeners(new VictoryDanceHandler1058());
        StartupUtils.registerListeners(new FinalKillEffectHandler1058());
        StartupUtils.registerListeners(new BedDestroyHandler1058());
        StartupUtils.registerListeners(new WoodSkinHandler1058());
        StartupUtils.registerListeners(new IslandTopperHandler1058());
        StartupUtils.registerListeners(new ProjectileHandler(Cosmetics.getInstance()));
        StartupUtils.registerListeners(new DeathCryHandler1058());
        StartupUtils.registerListeners(new SpraysHandler1058());
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
        com.andrei1058.bedwars.api.server.ISetupSession session = api.getSetupSession(playerUUID);
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
