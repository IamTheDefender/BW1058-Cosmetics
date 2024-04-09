package xyz.iamthedefender.cosmetics.category.bedbreakeffects;

import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.event.BedBreakEffectExecuteEvent;
import xyz.iamthedefender.cosmetics.util.BedWarsWrapper;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BedDestroyHandler2023 implements Listener {

    @EventHandler
    public void onBedBreak2023(com.tomkeuper.bedwars.api.events.player.PlayerBedBreakEvent e) {
        String selected = Cosmetics.getInstance().getApi().getSelectedCosmetic(e.getPlayer(), CosmeticsType.BedBreakEffects);
        BedBreakEffectExecuteEvent event = new BedBreakEffectExecuteEvent(e.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        boolean isBedDestroysEnabled = Cosmetics.getInstance().getConfig().getBoolean("bed-break-effects.enabled");
        if (!isBedDestroysEnabled) return;

        DebugUtil.addMessage("Executing " + selected + " Bed Break Effect for " + e.getPlayer().getDisplayName());
        Player p = e.getPlayer();
        Location loc = e.getVictimTeam().getBed();

        for(BedDestroy bedDestroy : StartupUtils.bedDestroyList){
            if (selected.equals(bedDestroy.getIdentifier())){
                if (bedDestroy.getField(FieldsType.RARITY, p) != RarityType.NONE) {
                    bedDestroy.execute(p, loc, BedWarsWrapper.wrap(e.getVictimTeam()));
                }
            }
        }
    }
}
