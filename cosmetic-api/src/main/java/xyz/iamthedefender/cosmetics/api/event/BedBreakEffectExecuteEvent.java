package xyz.iamthedefender.cosmetics.api.event;

import lombok.Getter;
import xyz.iamthedefender.cosmetics.api.cosmetics.CosmeticsType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.iamthedefender.cosmetics.api.util.Utility;

/**
 * This event is called when a bed break effect is executed.
*/
public class BedBreakEffectExecuteEvent extends Event implements Cancellable {
    private Boolean cancelled;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    /**
     * -- GETTER --
     *  Get the player that broke the bed.
     *
     * @return Player
     */
    @Getter
    private final Player WhoBrokeTheBed;
    /**
     * -- GETTER --
     *  Get the selected bed break effect.
     *
     * @return String
     */
    @Getter
    private final String selected;

    public BedBreakEffectExecuteEvent(Player WhoBroke){
        this.cancelled = false;
        this.WhoBrokeTheBed = WhoBroke;
        this.selected = Utility.getApi().getSelectedCosmetic(WhoBroke, CosmeticsType.BedBreakEffects);
    }


    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public static HandlerList getHandlersList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
