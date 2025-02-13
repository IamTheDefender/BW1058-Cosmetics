package xyz.iamthedefender.cosmetics.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;
import xyz.iamthedefender.cosmetics.api.event.CosmeticPurchaseEvent;

public class CosmeticPurchaseListener implements Listener {

    @EventHandler
    public void onPurchase(CosmeticPurchaseEvent e){
        if (e.isCancelled())
            return;
        CosmeticsPlugin.getInstance()
                .getPlayerManager()
                .getPlayerOwnedData(e.getPlayer().getUniqueId())
                .updateOwned();

    }
}
