package xyz.iamthedefender.cosmetics.category.bedbreakeffects.items;


import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.BedDestroy;
import xyz.iamthedefender.cosmetics.api.handler.ITeamHandler;

import java.util.Arrays;
import java.util.List;
/**
 * Bed destroy effect.
 * Spawns a enderman that steal the bed!
 */
public class TheifBedDestroy extends BedDestroy {
    /** {@inheritDoc} */
    @Override
    public ItemStack getItem() {
        return XMaterial.ENDER_PEARL.parseItem();
    }
    /** {@inheritDoc} */
    @Override
    public String base64() {
        return null;
    }
    /** {@inheritDoc} */
    @Override
    public String getIdentifier() {
        return "theif";
    }
    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
        return "Theif";
    }
    /** {@inheritDoc} */
    @Override
    public List<String> getLore() {
        return Arrays.asList("&7Spawns a enderman that", "&7steal the bed!");
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
        Enderman enderman = (Enderman) player.getWorld().spawnEntity(bedLocation, EntityType.ENDERMAN);
        assert XMaterial.RED_BED.parseMaterial() != null;
        enderman.setCarriedMaterial(new MaterialData(XMaterial.RED_BED.parseMaterial()));
        HCore.syncScheduler().after(70L).run(enderman::remove);
    }

}
