package xyz.iamthedefender.cosmetics.support.bedwars.handler.bedwars1058;

import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.BedWars;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.handler.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class BW1058ProxyHandler implements IHandler {
    @Override
    public void register() {

    }

    @Override
    public HandlerType getHandlerType() {
        return HandlerType.BUNGEE;
    }

    @Override
    public ISetupSession getSetupSession(UUID playerUUID) {
        return null;
    }

    @Override
    public IScoreboardUtil getScoreboardUtil() {
        return null;
    }

    @Override
    public IArenaUtil getArenaUtil() {
        return null;
    }

    @Override
    public String getAddonPath() {
        return BedWarsProxy.getPlugin().getDataFolder() + File.separator + "Addons" + File.separator + Cosmetics.getInstance().getDescription().getName();
    }

    @Override
    public ILanguage getLanguageUtil() {
        BedWars.LanguageUtil languageUtil = BedWarsProxy.getAPI().getLanguageUtil();
        return new ILanguage() {
            @Override
            public String getMessage(Player player, String path) {
                return languageUtil.getMsg(player, path);
            }

            @Override
            public List<String> getMessageList(Player player, String path) {
                return languageUtil.getList(player, path);
            }

            @Override
            public void saveIfNotExists(String path, Object data) {
                languageUtil.saveIfNotExists(path, data);
            }
        };
    }
}
