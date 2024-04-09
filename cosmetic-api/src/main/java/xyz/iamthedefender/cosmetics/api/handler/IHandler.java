package xyz.iamthedefender.cosmetics.api.handler;

import java.util.UUID;

public interface IHandler {

    void register();

    ISetupSession getSetupSession(UUID playerUUID);
    IScoreboardUtil getScoreboardUtil();
    IArenaUtil getArenaUtil();
    ILanguage getLanguageUtil();
    String getAddonPath();
    HandlerType getHandlerType();

}
