package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuardiansDance extends VictoryDance {
    @Override
    public ItemStack getItem() {
        return XMaterial.PRISMARINE_BRICKS.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "guardians";
    }

    @Override
    public String getDisplayName() {
        return "Guardians";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Perform a Guardian", "&7summoning ritual.");
    }

    @Override
    public int getPrice() {
        return 30000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.RARE;
    }

    @Override
    public void execute(Player winner) {
        GuardianTriangle guardianTriangle = new GuardianTriangle(winner);
        HCore.registerEvent(PlayerLeaveArenaEvent.class).limit(1).consume((event) -> {
           guardianTriangle.stop();
        });
    }
}


class GuardianTriangle {
    private final Player player;
    private final Location playerLocation;
    private boolean stopped;
    private final List<Entity> guardians;

    public GuardianTriangle(Player player1) {
        this.playerLocation = player1.getLocation();
        this.stopped = false;
        this.guardians = new ArrayList<>();
        this.player = player1;
        player.setNoDamageTicks(Integer.MAX_VALUE);
        start();
    }

    public void start() {
        if (!stopped) {
            // Spawn guardians at player location + 1 y, one at each side
            for (int i = 0; i < 3; i++) {
                Location guardianLocation = playerLocation.clone().add(0, 1, 0);
                guardianLocation.setYaw(i * 120); // Rotate each guardian to make a triangle shape
                guardianLocation.getWorld().spawnEntity(guardianLocation, EntityType.GUARDIAN);
                guardians.add(guardianLocation.getWorld().spawnEntity(guardianLocation, EntityType.GUARDIAN));
            }

            // Make guardians float and attack player with beams
            Bukkit.getScheduler().runTaskTimer(Cosmetics.getInstance(), () -> {
                if (!stopped) {
                    for (Entity guardian : guardians) {
                        ((Guardian) guardian).setNoDamageTicks(Integer.MAX_VALUE);
                        ((Guardian) guardian).setTarget(player);
                    }
                }
            }, 0, 20);
        }
    }

    public void stop() {
        stopped = true;
        player.setNoDamageTicks(Integer.MAX_VALUE);
        guardians.forEach(Entity::remove);
    }
}

