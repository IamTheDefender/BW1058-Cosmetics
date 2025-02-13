package xyz.iamthedefender.cosmetics.category.deathcries;

import com.cryptomorin.xseries.XSound;
import com.tomkeuper.bedwars.api.events.player.PlayerKillEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.DeathCry;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.NoSuchElementException;

public class DeathCryHandler2023 implements Listener {

    @EventHandler
    public void onPlayerDeath2023(PlayerKillEvent e) {
        Player killed = e.getVictim();
        String selected = CosmeticsPlugin.getInstance().getApi().getSelectedCosmetic(killed, CosmeticsType.DeathCries);

        boolean isDeathCriesEnabled = CosmeticsPlugin.getInstance().getConfig().getBoolean("death-cries.enabled");
        if (!isDeathCriesEnabled) return;

        for (DeathCry deathCry : StartupUtils.deathCryList) {
            if (deathCry.getIdentifier().equals(selected)) {

                try {
                    float pitch = Float.parseFloat(String.valueOf(deathCry.getField(FieldsType.PITCH, killed)));
                    float volume = Float.parseFloat(String.valueOf(deathCry.getField(FieldsType.VOLUME, killed)));
                    XSound sound = (XSound) deathCry.getField(FieldsType.SOUND, killed);
                    sound.play(killed.getLocation(), volume, pitch);
                } catch (NoSuchElementException exception){
                    exception.printStackTrace();
                    Bukkit.getLogger().severe(deathCry.getIdentifier() + "Death cry has invalid sound!");
                }
            }
        }
        DebugUtil.addMessage("Executing " + selected + " Death Cry for " + killed.getDisplayName());
    }
}