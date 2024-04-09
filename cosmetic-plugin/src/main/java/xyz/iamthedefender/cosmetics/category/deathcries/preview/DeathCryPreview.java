package xyz.iamthedefender.cosmetics.category.deathcries.preview;

import com.cryptomorin.xseries.XSound;
import xyz.iamthedefender.cosmetics.api.cosmetics.FieldsType;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.DeathCry;
import xyz.iamthedefender.cosmetics.util.StartupUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DeathCryPreview {


    public void sendPreviewCry(Player player, String previewID){
        for(DeathCry cry : StartupUtils.deathCryList){
            if (cry.getIdentifier().equals(previewID)){
                if (cry.getField(FieldsType.RARITY, player) == null || cry.getField(FieldsType.RARITY, player) == RarityType.NONE) return;
                float pitch = Float.parseFloat(String.valueOf(cry.getField(FieldsType.PITCH, player)));
                float volume = Float.parseFloat(String.valueOf(cry.getField(FieldsType.VOLUME, player)));
                try {
                    XSound sound = (XSound) cry.getField(FieldsType.SOUND, player);
                    sound.play(player, volume, pitch);
                }catch (IllegalArgumentException | NullPointerException e){
                    Bukkit.getLogger().severe("Looks like there is an error with the sound for " + cry.getIdentifier() + " death cry: " + e.getMessage());
                }
                return;
            }
        }
    }
}
