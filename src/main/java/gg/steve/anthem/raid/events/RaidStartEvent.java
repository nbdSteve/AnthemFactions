package gg.steve.anthem.raid.events;

import com.sun.webkit.dom.EventImpl;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.raid.Tier;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RaidStartEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Faction defendingFaction;
    private Faction raidingFaction;
    private Tier tier;
    private boolean cancel;

    public RaidStartEvent(Faction defendingFaction, Faction raidingFaction, Tier tier) {
        this.defendingFaction = defendingFaction;
        this.raidingFaction = raidingFaction;
        this.tier = tier;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Faction getDefendingFaction() {
        return defendingFaction;
    }

    public Faction getRaidingFaction() {
        return raidingFaction;
    }

    public Tier getTier() {
        return tier;
    }
}
