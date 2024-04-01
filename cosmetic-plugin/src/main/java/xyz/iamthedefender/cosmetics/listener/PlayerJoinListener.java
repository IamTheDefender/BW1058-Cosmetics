

package xyz.iamthedefender.cosmetics.listener;

import com.hakan.core.HCore;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.CosmeticsAPI;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.*;
import xyz.iamthedefender.cosmetics.data.PlayerData;
import xyz.iamthedefender.cosmetics.data.PlayerOwnedData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CosmeticsAPI api = Cosmetics.getInstance().getApi();

        // Saving for MySQL is different
        if (api.isMySQL()) {
            PlayerData playerData = Cosmetics.getInstance().getPlayerManager().getPlayerData(event.getPlayer().getUniqueId());
            if (!playerData.exists()) {
                playerData.setBedDestroy(BedDestroy.getDefault(event.getPlayer()).getIdentifier());
                playerData.setDeathCry(DeathCry.getDefault(event.getPlayer()).getIdentifier());
                playerData.setFinalKillEffect(FinalKillEffect.getDefault(event.getPlayer()).getIdentifier());
                playerData.setGlyph(Glyph.getDefault(event.getPlayer()).getIdentifier());
                playerData.setIslandTopper(IslandTopper.getDefault(event.getPlayer()).getIdentifier());
                playerData.setKillMessage(KillMessage.getDefault(event.getPlayer()).getIdentifier());
                playerData.setProjectileTrail(ProjectileTrail.getDefault(event.getPlayer()).getIdentifier());
                playerData.setShopkeeperSkin(ShopKeeperSkin.getDefault(event.getPlayer()).getIdentifier());
                playerData.setSpray(Spray.getDefault(event.getPlayer()).getIdentifier());
                playerData.setVictoryDance(VictoryDance.getDefault(event.getPlayer()).getIdentifier());
                playerData.setWoodSkin(WoodSkin.getDefault(event.getPlayer()).getIdentifier());
                playerData.createData();
            }
            PlayerOwnedData playerOwnedData = Cosmetics.getInstance().getPlayerManager().getPlayerOwnedData(event.getPlayer().getUniqueId());
            playerOwnedData.updateOwned();
        }

        // Saving for SQLite is different, workaround for SQLite database is busy
        if (!api.isMySQL()) {
            if (!Cosmetics.getInstance().getPlayerManager().getPlayerDataHashMap().containsKey(event.getPlayer().getUniqueId())) {
                PlayerData playerData = new PlayerData(event.getPlayer().getUniqueId());
                if (!playerData.exists()) {
                    playerData.setBedDestroy(BedDestroy.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setDeathCry(DeathCry.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setFinalKillEffect(FinalKillEffect.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setGlyph(Glyph.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setIslandTopper(IslandTopper.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setKillMessage(KillMessage.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setProjectileTrail(ProjectileTrail.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setShopkeeperSkin(ShopKeeperSkin.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setSpray(Spray.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setVictoryDance(VictoryDance.getDefault(event.getPlayer()).getIdentifier());
                    playerData.setWoodSkin(WoodSkin.getDefault(event.getPlayer()).getIdentifier());
                    playerData.createData();
                }
                Cosmetics.getInstance().getPlayerManager().addPlayerData(playerData);
            }

            if (!Cosmetics.getInstance().getPlayerManager().getPlayerOwnedDataHashMap().containsKey(event.getPlayer().getUniqueId())) {
                HCore.asyncScheduler().run(() -> {
                    PlayerOwnedData playerOwnedData = Cosmetics.getInstance().getPlayerManager().getPlayerOwnedData(event.getPlayer().getUniqueId());
                    playerOwnedData.updateOwned();
                });
            } else {
              Cosmetics.getInstance().getPlayerManager().getPlayerOwnedData(event.getPlayer().getUniqueId()).updateOwned();
            }
        }
    }
}
