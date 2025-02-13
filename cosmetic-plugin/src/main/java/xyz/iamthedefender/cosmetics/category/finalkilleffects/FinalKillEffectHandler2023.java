package xyz.iamthedefender.cosmetics.category.finalkilleffects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.CosmeticsAPI;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import xyz.iamthedefender.cosmetics.api.event.FinalKillEffectsExecuteEvent;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

public class FinalKillEffectHandler2023 implements Listener {

    @EventHandler
    public void onFinalKill2023(com.tomkeuper.bedwars.api.events.player.PlayerKillEvent e){
        CosmeticsAPI api = CosmeticsPlugin.getInstance().getApi();
        if (e.getKiller() == null) return;

        boolean isFinalKillEffectsEnabled = CosmeticsPlugin.getInstance().getConfig().getBoolean("final-kill-effects.enabled");
        if (!isFinalKillEffectsEnabled) return;

        String selected = api.getSelectedCosmetic(e.getKiller(), CosmeticsType.FinalKillEffects);
        Player victim = e.getVictim();
        Player killer = e.getKiller();

        FinalKillEffectsExecuteEvent event = new FinalKillEffectsExecuteEvent(victim, killer, selected);
        Bukkit.getPluginManager().callEvent(event);
        if (!e.getCause().isFinalKill()) return;


        for(FinalKillEffect finalKillEffects : StartupUtils.finalKillList){
            if (selected.equals(finalKillEffects.getIdentifier())){
                if (finalKillEffects.getField(FieldsType.RARITY, e.getKiller()) == RarityType.NONE) return;
                finalKillEffects.execute(e.getKiller(), e.getVictim(), e.getVictim().getLocation(), false);
            }
        }
        DebugUtil.addMessage("Playing " + selected + " Final Kill Effect for " + e.getKiller().getDisplayName());
    }
}
