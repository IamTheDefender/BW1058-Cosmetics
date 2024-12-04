package xyz.iamthedefender.cosmetics.category.bedbreakeffects.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import org.bukkit.Location;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.handler.ITeamHandler;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bed destroy effect.
 * This effect spawns many bed bugs near the bed location.
 */
public class BedBugsBedDestroy extends BedDestroy {

    /** {@inheritDoc} */
    @Override
    public ItemStack getItem() {
        return XMaterial.ENDERMITE_SPAWN_EGG.parseItem();
    }
    /** {@inheritDoc} */
    @Override
    public String base64() {
        return null;
    }
    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return "bed-bugs";
    }
    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
        return "Bed Bugs";
    }
    /** {@inheritDoc} */
    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns many bed bugs near", "&7the bed location!");
    }
    /** {@inheritDoc} */
    @Override
    public int getPrice() {
        return 10000;
    }
    /** {@inheritDoc} */
    @Override
    public RarityType getRarity() {
        return RarityType.RARE;
    }
    /** {@inheritDoc} */
    @Override
    public void execute(Player player, Location bedLocation, ITeamHandler victimTeam) {
        List<Endermite> endermites = new ArrayList<>();

        Run.every(() -> {
            Location loc = UsefulUtilsVD.getRandomLocation(bedLocation, 1);
            Endermite mite = (Endermite) player.getWorld().spawnEntity(loc.add(0,1,0), EntityType.ENDERMITE);
            endermites.add(mite);
        }, 1, 4);

        Run.delayed(() -> {
            endermites.forEach(Entity::remove);
        }, 100L);
    }

}