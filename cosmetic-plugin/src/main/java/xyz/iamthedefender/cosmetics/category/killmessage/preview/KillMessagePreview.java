package xyz.iamthedefender.cosmetics.category.killmessage.preview;

import com.cryptomorin.xseries.XSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticPreview;
import xyz.iamthedefender.cosmetics.api.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import xyz.iamthedefender.cosmetics.api.menu.SystemGui;
import xyz.iamthedefender.cosmetics.api.util.ColorUtil;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.category.killmessage.util.KillMessageUtils;

import java.util.List;

public class KillMessagePreview extends CosmeticPreview {


    public KillMessagePreview() {
        super(CosmeticsType.KillMessage);
    }

    @Override
    public void showPreview(Player player, Cosmetics selected, Location previewLocation, Location playerLocation) throws IllegalArgumentException {
        XSound.ENTITY_ARROW_HIT_PLAYER.play(player, 1.0f, 1.0f);
        List<String> message = Utility.getListLang(player, "cosmetics." + "kill-message" + ".preview.message");
        String selectedId = selected.getIdentifier();

        SystemGui systemGui = CosmeticsPlugin.getInstance().getSystemGuiManager().getByPlayer(player);

        for (String s : message) {
            if (s.contains("%message%")){
                KillMessageUtils.sendKillMessage(player, "Player", null, false, ChatColor.GREEN, ChatColor.GRAY, "PvP", "Old Message" ,true, selectedId, player.getDisplayName());
                KillMessageUtils.sendKillMessage(player, "Player", null, false, ChatColor.GREEN, ChatColor.GRAY, "Void", "Old Message" ,true, selectedId, player.getDisplayName());
                KillMessageUtils.sendKillMessage(player, "Player", null, false, ChatColor.GREEN, ChatColor.GRAY, "Shoot", "Old Message" ,true, selectedId, player.getDisplayName());
                KillMessageUtils.sendKillMessage(player, "Player", null, false, ChatColor.GREEN, ChatColor.GRAY, "Explosion", "Old Message" ,true, selectedId, player.getDisplayName());
            }else{
                player.sendMessage(ColorUtil.translate(s));
            }
        }

        if (systemGui != null) {
            Run.delayed(() -> systemGui.open(player), getEndDelay());
        }
    }
}
