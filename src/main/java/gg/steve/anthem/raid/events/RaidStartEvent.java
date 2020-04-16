package gg.steve.anthem.raid.events;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.raid.FRaid;
import gg.steve.anthem.raid.Tier;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RaidStartEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private FRaid raid;
    private Faction defendingFaction;
    private Faction raidingFaction;
    private Tier tier;
    private boolean cancel;
    private FPlayer starter;

    public RaidStartEvent(FRaid raid, Faction defendingFaction, Faction raidingFaction, Tier tier, FPlayer starter) {
        this.raid = raid;
        this.defendingFaction = defendingFaction;
        this.raidingFaction = raidingFaction;
        this.tier = tier;
        this.starter = starter;
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

    public FPlayer getStarter() {
        return starter;
    }

    public FRaid getRaid() {
        return raid;
    }
}
