

package xyz.iamthedefender.cosmetics.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.CosmeticsAPI;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.*;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.data.PlayerData;
import xyz.iamthedefender.cosmetics.data.PlayerOwnedData;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CosmeticsAPI api = CosmeticsPlugin.getInstance().getApi();

        if (api.isMySQL()) {
            PlayerData playerData = CosmeticsPlugin.getInstance().getPlayerManager().getPlayerData(event.getPlayer().getUniqueId());
            if (!playerData.exists()) {
                setDefaultCosmetics(playerData, event.getPlayer());
                playerData.createData();
            }
            playerData.load();
            updatePlayerOwnedData(event.getPlayer());
            return;
        }

        if (!CosmeticsPlugin.getInstance().getPlayerManager().getPlayerDataHashMap().containsKey(event.getPlayer().getUniqueId())) {
            PlayerData playerData = createNewPlayerData(event.getPlayer());
            if (!playerData.exists()) {
                setDefaultCosmetics(playerData, event.getPlayer());
                playerData.createData();
            }
            CosmeticsPlugin.getInstance().getPlayerManager().addPlayerData(playerData);
        }

        if (!CosmeticsPlugin.getInstance().getPlayerManager().getPlayerOwnedDataHashMap().containsKey(event.getPlayer().getUniqueId())) {
            updatePlayerOwnedDataAsync(event.getPlayer());
        } else {
            updatePlayerOwnedData(event.getPlayer());
        }
    }

    private void setDefaultCosmetics(PlayerData playerData, Player player) {
        playerData.setBedDestroy(BedDestroy.getDefault(player).getIdentifier());
        playerData.setDeathCry(DeathCry.getDefault(player).getIdentifier());
        playerData.setFinalKillEffect(FinalKillEffect.getDefault(player).getIdentifier());
        playerData.setGlyph(Glyph.getDefault(player).getIdentifier());
        playerData.setIslandTopper(IslandTopper.getDefault(player).getIdentifier());
        playerData.setKillMessage(KillMessage.getDefault(player).getIdentifier());
        playerData.setProjectileTrail(ProjectileTrail.getDefault(player).getIdentifier());
        playerData.setShopkeeperSkin(ShopKeeperSkin.getDefault(player).getIdentifier());
        playerData.setSpray(Spray.getDefault(player).getIdentifier());
        playerData.setVictoryDance(VictoryDance.getDefault(player).getIdentifier());
        playerData.setWoodSkin(WoodSkin.getDefault(player).getIdentifier());
    }

    private PlayerData createNewPlayerData(Player player) {
        return new PlayerData(player.getUniqueId());
    }

    private void updatePlayerOwnedData(Player player) {
        PlayerOwnedData playerOwnedData = CosmeticsPlugin.getInstance().getPlayerManager().getPlayerOwnedData(player.getUniqueId());
        playerOwnedData.updateOwned();
    }

    private void updatePlayerOwnedDataAsync(Player player) {
        Run.async(() -> {
            PlayerOwnedData playerOwnedData = CosmeticsPlugin.getInstance().getPlayerManager().getPlayerOwnedData(player.getUniqueId());
            playerOwnedData.updateOwned();
        });
    }
}
