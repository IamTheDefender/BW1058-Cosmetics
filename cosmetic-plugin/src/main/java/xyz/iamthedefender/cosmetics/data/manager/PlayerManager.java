package xyz.iamthedefender.cosmetics.data.manager;

import lombok.Getter;
import xyz.iamthedefender.cosmetics.data.PlayerData;
import xyz.iamthedefender.cosmetics.data.PlayerOwnedData;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PlayerManager {

    private final HashMap<UUID, PlayerData> playerDataHashMap;
    private final HashMap<UUID, PlayerOwnedData> playerOwnedDataHashMap;

    public PlayerManager() {
        playerDataHashMap = new HashMap<>();
        playerOwnedDataHashMap = new HashMap<>();

    }


    public void addPlayerData(PlayerData playerData) {
        playerDataHashMap.put(playerData.getUuid(), playerData);
    }

    public void addPlayerOwnedData(PlayerOwnedData playerOwnedData) {
        playerOwnedDataHashMap.put(playerOwnedData.getUuid(), playerOwnedData);
    }

    public PlayerData getPlayerData(UUID uuid) {
        PlayerData playerData = playerDataHashMap.computeIfAbsent(uuid, PlayerData::new);
        playerData.load();
        return playerData;
    }

    public PlayerOwnedData getPlayerOwnedData(UUID uuid) {
        PlayerOwnedData playerOwnedData = playerOwnedDataHashMap.computeIfAbsent(uuid, PlayerOwnedData::new);
        playerOwnedData.load();
        return playerOwnedData;
    }
}
