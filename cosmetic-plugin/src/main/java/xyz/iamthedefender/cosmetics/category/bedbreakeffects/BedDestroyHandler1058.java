package xyz.iamthedefender.cosmetics.category.bedbreakeffects;

import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.event.BedBreakEffectExecuteEvent;
import xyz.iamthedefender.cosmetics.util.BedWarsWrapper;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;


public class BedDestroyHandler1058 implements Listener{
	
	@EventHandler
	public void onBedBreak1058(PlayerBedBreakEvent e) {
		String selected = CosmeticsPlugin.getInstance().getApi().getSelectedCosmetic(e.getPlayer(), CosmeticsType.BedBreakEffects);
		BedBreakEffectExecuteEvent event = new BedBreakEffectExecuteEvent(e.getPlayer());
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (event.isCancelled()) return;
		boolean isBedDestroysEnabled = CosmeticsPlugin.getInstance().getConfig().getBoolean("bed-break-effects.enabled");
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