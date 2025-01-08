

package xyz.iamthedefender.cosmetics.category.islandtoppers;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.IslandTopper;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

public class IslandTopperHandler1058 implements Listener {
    @EventHandler
    public void onSpawn1058(GameStateChangeEvent e) {

        boolean isIslandToppersEnabled = Cosmetics.getInstance().getConfig().getBoolean("island-toppers.enabled");
        if (!isIslandToppersEnabled) return;

        Run.delayed(() -> {
            for (ITeam teams : e.getArena().getTeams()) {
                Player player = null;
                for (Player p : teams.getMembers()) {
                    if (!Cosmetics.getInstance().getApi().getSelectedCosmetic(p, CosmeticsType.IslandTopper).equals("none")) {
                        player = p;
                    }
                }

                if (player == null) return;
                String pathForLocation = "Team." + teams.getName() + ".IslandTopper.";
                ConfigManager config = e.getArena().getConfig();
                Location location = config.getConfigLoc(pathForLocation + "location");
                if (location == null) {
                    return;
                }
                String selected = Cosmetics.getInstance().getApi().getSelectedCosmetic(player, CosmeticsType.IslandTopper);
                for(IslandTopper islandTopper : StartupUtils.islandTopperList){
                    if (islandTopper.getIdentifier().equals(selected)){
                        if (islandTopper.getField(FieldsType.RARITY, player) != RarityType.NONE) {
                            islandTopper.execute(player, location, selected);
                        }
                    }
                }
            }
        }, 20L);
    }
}