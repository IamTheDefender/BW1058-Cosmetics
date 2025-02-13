package xyz.iamthedefender.cosmetics.category.deathcries.preview;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.api.cosmetics.*;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.DeathCry;

public class DeathCryPreview extends CosmeticPreview {

    public DeathCryPreview() {
        super(CosmeticsType.DeathCries);
    }

    @Override
    public void showPreview(Player player, Cosmetics selected, Location previewLocation, Location playerLocation) throws IllegalArgumentException {
        DeathCry cry = (DeathCry) selected;

        if (cry.getField(FieldsType.RARITY, player) == null || cry.getField(FieldsType.RARITY, player) == RarityType.NONE) return;

        float pitch = Float.parseFloat(String.valueOf(cry.getField(FieldsType.PITCH, player)));
        float volume = Float.parseFloat(String.valueOf(cry.getField(FieldsType.VOLUME, player)));

        try {
            XSound sound = (XSound) cry.getField(FieldsType.SOUND, player);
            sound.play(player, volume, pitch);
        }catch (IllegalArgumentException | NullPointerException e){
            Bukkit.getLogger().severe("Looks like there is an error with the sound for " + cry.getIdentifier() + " death cry: " + e.getMessage());
        }
    }
}
