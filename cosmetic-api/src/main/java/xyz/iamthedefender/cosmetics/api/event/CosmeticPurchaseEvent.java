package xyz.iamthedefender.cosmetics.api.event;

import lombok.Getter;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CosmeticPurchaseEvent extends Event implements Cancellable {
    private boolean isCancelled;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * -- GETTER --
     *  Get the player that purchased the cosmetic.
     *
     * @return Player
     */
    @Getter
    private final Player player;

    /**
     * -- GETTER --
     *  Get the category of the cosmetic.
     *
     * @return CosmeticsType
     */
    @Getter
    private final CosmeticsType category;

    public CosmeticPurchaseEvent(Player p, CosmeticsType category){
        this.isCancelled = false;
        this.player = p;
        this.category = category;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = true;
    }

}
