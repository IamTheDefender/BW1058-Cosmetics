package xyz.iamthedefender.cosmetics.category.victorydance;

import com.tomkeuper.bedwars.api.events.gameplay.GameEndEvent;
import com.tomkeuper.bedwars.api.events.player.PlayerLeaveArenaEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.event.VictoryDancesExecuteEvent;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VictoryDanceHandler2023 implements Listener {

    public static Map<UUID, VictoryDance> victoryDanceMap = new HashMap<>();

    @EventHandler
    public void onGameEnd2023(GameEndEvent e) {

        boolean isVictoryDancesEnabled = Cosmetics.getInstance().getConfig().getBoolean("victory-dances.enabled");
        if (!isVictoryDancesEnabled) return;

        for (UUID uuid : e.getWinners()) {
            Player p = Bukkit.getPlayer(uuid);
            String selected = Cosmetics.getInstance().getApi().getSelectedCosmetic(p, CosmeticsType.VictoryDances);
            VictoryDancesExecuteEvent event = new VictoryDancesExecuteEvent(p);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled())
                return;

            DebugUtil.addMessage("Executing " + selected + " Victory Dance for " + p.getDisplayName());
            for(VictoryDance victoryDance : StartupUtils.victoryDancesList){
                if (selected.equals(victoryDance.getIdentifier())){
                    if (victoryDance.getField(FieldsType.RARITY, p) == RarityType.NONE) return;
                    victoryDance.execute(p);

                    victoryDanceMap.put(uuid, victoryDance);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeaveArena(PlayerLeaveArenaEvent event) {
        Player player = event.getPlayer();

        Optional.ofNullable(victoryDanceMap.get(player.getUniqueId()))
                .ifPresent(victoryDance -> victoryDance.stopExecution(player));
    }
}