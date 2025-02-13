package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;

import java.util.List;

public class FloatingLanternDance extends VictoryDance {
    @Override
    public ItemStack getItem() {
        return XMaterial.PLAYER_HEAD.parseItem();
    }

    @Override
    public String base64() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRjNzM2ODY0YTY0OWY2NjVmMDRmMjhiYjE0YTNjNGVhMGYyNDVlODQ2MDE3YWNmZTM2NmU3ZDEzNWI0ZmNhOCJ9fX0=";
    }

    @Override
    public String getIdentifier() {
        return "floating-lantern";
    }

    @Override
    public String getDisplayName() {
        return "Floating Lantern";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Spawn floating lanterns to", "&7brighten up the night.");
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

        Run.every(() -> {
            Location loc = UsefulUtilsVD.getRandomLocation(winner.getLocation(), 10);
            Bat bat = (Bat) winner.getWorld().spawnEntity(loc, EntityType.BAT);
            bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            bat.setNoDamageTicks(Integer.MAX_VALUE);

            addEntity(winner, bat);

            ArmorStand stand = (ArmorStand)winner.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            stand.setVisible(false);

            addEntity(winner, stand);

            bat.setPassenger(stand);

            stand.setHelmet(UsefulUtilsVD.gethead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTRjNzM2ODY0YTY0OWY2NjVmMDRmMjhiYjE0YTNjNGVhMGYyNDVlODQ2MDE3YWNmZTM2NmU3ZDEzNWI0ZmNhOCJ9fX0="));

        }, 8L);
    }
}
