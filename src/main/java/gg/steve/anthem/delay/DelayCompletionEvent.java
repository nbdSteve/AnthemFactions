package gg.steve.anthem.delay;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DelayCompletionEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Delay delay;
    private boolean cancel;

    public DelayCompletionEvent(Delay delay) {
        this.delay = delay;
    }

    public Delay getDelay() {
        return this.delay;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
