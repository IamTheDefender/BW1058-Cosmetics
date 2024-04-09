package xyz.iamthedefender.cosmetics.listener;

import xyz.iamthedefender.cosmetics.Cosmetics;
import xyz.iamthedefender.cosmetics.api.event.CosmeticPurchaseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CosmeticPurchaseListener implements Listener {

    @EventHandler
    public void onPurchase(CosmeticPurchaseEvent e){
        if (e.isCancelled())
            return;
        Cosmetics.getInstance()
                .getPlayerManager()
                .getPlayerOwnedData(e.getPlayer().getUniqueId())
                .updateOwned();

    }
}
