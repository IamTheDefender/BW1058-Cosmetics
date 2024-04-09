package xyz.iamthedefender.cosmetics.api.handler;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public interface ISetupSession {
    UUID getPlayerUUID();
    FileConfiguration getConfig();
    void saveConfigLoc(String path, Location value);
    void saveConfig();
}
