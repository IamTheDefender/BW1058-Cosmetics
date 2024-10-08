package xyz.iamthedefender.cosmetics.category.shopkeeperskins;

import com.hakan.core.HCore;
import com.tomkeuper.bedwars.api.arena.GameState;
import com.tomkeuper.bedwars.api.arena.team.ITeam;
import com.tomkeuper.bedwars.api.events.gameplay.GameStateChangeEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.ShopKeeperSkin;
import xyz.iamthedefender.cosmetics.api.handler.HandlerType;
import xyz.iamthedefender.cosmetics.api.handler.IHandler;
import xyz.iamthedefender.cosmetics.util.DebugUtil;
import xyz.iamthedefender.cosmetics.util.MathUtil;
import xyz.iamthedefender.cosmetics.util.StartupUtils;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ShopKeeperHandler2023 implements Listener {

    @EventHandler
    public void onGameStart2023(com.tomkeuper.bedwars.api.events.gameplay.GameStateChangeEvent event) {

        boolean isShopkeepersEnabled = Cosmetics.getInstance().getConfig().getBoolean("shopkeeper-skins.enabled");
        if (!isShopkeepersEnabled) return;

        if (event.getNewState().name().equals("playing")) {
            ShopKeeperHandler1058.arenas.put(event.getArena().getWorldName(), true);
            List<ITeam> teams = event.getArena().getTeams();
            DebugUtil.addMessage("Executing ShopKeeper Skins for arena " + event.getArena().getArenaName());
            new BukkitRunnable() {
                public void run() {
                    for (com.tomkeuper.bedwars.api.arena.team.ITeam team : teams) {
                        if (team.getMembers().isEmpty()) continue; // Skip empty teams

                        Location shopLocation = team.getShop();
                        Location upgradeLocation = team.getTeamUpgrades();
                        World world = shopLocation.getWorld();
                        DebugUtil.addMessage("Executing ShopKeeper Skins for team " + team.getName());
                        // Delete existing NPCs
                        world.getEntities().stream()
                                .filter(e -> (e.getLocation().distance(shopLocation) <= 0.2 || e.getLocation().distance(upgradeLocation) <= 0.2 && e instanceof Villager))
                                .forEach(Entity::remove);

                        // Choose random player from the team
                        Player player = team.getMembers().get(MathUtil.getRandom(0, team.getMembers().size() -1));
                        String skin = Cosmetics.getInstance().getApi().getSelectedCosmetic(player, CosmeticsType.ShopKeeperSkin);
                        DebugUtil.addMessage("Selected skin: " + skin);
                        // Spawn new NPCs
                        for (ShopKeeperSkin skins : StartupUtils.shopKeeperSkinList) {
                            if (skin.equals(skins.getIdentifier())) {
                                try {
                                    skins.execute(player, shopLocation, upgradeLocation);
                                }catch (Exception ignored){
                                }
                            }
                        }


                        if (Cosmetics.getInstance().getHandler().getHandlerType() != HandlerType.BUNGEE){
                            for (Player p : team.getMembers()) {
                                IHandler handler = Cosmetics.getInstance().getHandler();
                                handler.getScoreboardUtil().removePlayerScoreboard(p);
                                handler.getScoreboardUtil().giveScoreboard(p, true);
                            }
                        }
                    }
                }
            }.runTaskLater(Cosmetics.getInstance(), 30L);
        }
    }

    @EventHandler
    public void onGameStartEvent(GameStateChangeEvent event){
        if (event.getNewState() != GameState.playing) return;
        getServer().getScheduler().runTaskLater(Cosmetics.getInstance(), () -> {
            World world = event.getArena().getWorld();
            for (Entity entity : world.getEntities()) {
                boolean isCitizensNPC = entity.hasMetadata("NPC");
                if (!entity.isDead() && isCitizensNPC){
                    NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                    npc.data().setPersistent(NPC.Metadata.DEATH_SOUND, "");
                    npc.data().setPersistent(NPC.Metadata.AMBIENT_SOUND, "");
                    npc.data().setPersistent(NPC.Metadata.HURT_SOUND, "");
                    npc.data().setPersistent(NPC.Metadata.SILENT, true);
                }
            }
        }, 40L);
    }

    @EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent e){

        boolean isShopkeepersEnabled = Cosmetics.getInstance().getConfig().getBoolean("shopkeeper-skins.enabled");
        if (!isShopkeepersEnabled) return;

        if (e.getPlayer().hasMetadata("NPC2")){
            e.setCancelled(true);
            HCore.syncScheduler().after(2).run((() -> {
                CitizensAPI.getNPCRegistry().getNPC(e.getPlayer()).despawn();
            }));
        }
    }

    @EventHandler
    public void onGameEnd2023(com.tomkeuper.bedwars.api.events.gameplay.GameEndEvent e) {

        boolean isShopkeepersEnabled = Cosmetics.getInstance().getConfig().getBoolean("shopkeeper-skins.enabled");
        if (!isShopkeepersEnabled) return;

        String name = e.getArena().getWorldName();

        new BukkitRunnable(){
            @Override
            public void run() {

                ShopKeeperHandler1058.arenas.remove(name);
            }
        }.runTaskLater(Cosmetics.getInstance(), 300L);
    }
}