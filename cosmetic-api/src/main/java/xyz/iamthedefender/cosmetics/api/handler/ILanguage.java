package xyz.iamthedefender.cosmetics.api.handler;

import org.bukkit.entity.Player;

import java.util.List;

public interface ILanguage {

    String getMessage(Player player, String path);
    List<String> getMessageList(Player player, String path);
    void saveIfNotExists(String path, Object data);
}
