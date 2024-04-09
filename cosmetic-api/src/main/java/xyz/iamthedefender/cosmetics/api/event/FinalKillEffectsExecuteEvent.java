package xyz.iamthedefender.cosmetics.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FinalKillEffectsExecuteEvent extends Event implements Cancellable {
    private Boolean cancelled;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    /**
     * -- GETTER --
     *  Get the victim.
     *
     * @return Player
     */
    @Getter
    private final Player victim;
    /**
     * -- GETTER --
     *  Get the killer.
     *
     * @return Player
     */
    @Getter
    private final Player killer;

    /**
     * -- GETTER --
     *  Get the selected final kill effect.
     *
     * @return String
     */
    @Getter
    private final String selected;
    public FinalKillEffectsExecuteEvent(Player victim, Player killer, String selected){
        this.cancelled = false;
        this.victim = victim;
        this.killer = killer;
        this.selected = selected;
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