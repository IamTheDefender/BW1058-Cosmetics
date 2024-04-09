package xyz.iamthedefender.cosmetics.category.finalkilleffects;

import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.CosmeticsAPI;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.FinalKillEffect;
import xyz.iamthedefender.cosmetics.api.event.FinalKillEffectsExecuteEvent;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FinalKillEffectHandler1058 implements Listener {

    @EventHandler
    public void onFinalKill1058(PlayerKillEvent e){
        CosmeticsAPI api = Cosmetics.getInstance().getApi();
        if (e.getKiller() == null) return;

        boolean isFinalKillEffectsEnabled = Cosmetics.getInstance().getConfig().getBoolean("final-kill-effects.enabled");
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