package gg.steve.anthem.delay;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.delay.exception.InvalidDelayTypeException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.utils.actionbarapi.ActionBarType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Delay {
    private UUID uuid;
    private CooldownType type;
    private int remaining;
    private BukkitTask task;
    private boolean complete;
    private Location destination;
    private Location starting;
    private FPlayer fPlayer;

    public Delay(UUID uuid, CooldownType type, Location destination) throws InvalidDelayTypeException {
        this.uuid = uuid;
        if (!type.name().contains("_TELEPORT")) {
            throw new InvalidDelayTypeException();
        }
        this.type = type;
        this.remaining = type.getDuration();
        this.complete = false;
        this.destination = destination;
        this.starting = Bukkit.getPlayer(uuid).getLocation();
        start();
    }

    public Delay(UUID uuid, CooldownType type, Location destination, FPlayer fPlayer) throws InvalidDelayTypeException {
        this.uuid = uuid;
        if (!type.name().contains("_TELEPORT")) {
            throw new InvalidDelayTypeException();
        }
        this.type = type;
        this.remaining = type.getDuration();
        this.complete = false;
        this.destination = destination;
        this.fPlayer = fPlayer;
        this.starting = Bukkit.getPlayer(uuid).getLocation();
        start();
    }

    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(AnthemFactions.get(), () -> {
            if (this.remaining > 0) {
                if (!DelayManager.isSamePosition(Bukkit.getPlayer(this.uuid).getLocation(), this.starting)) {
                    Bukkit.getPluginManager().callEvent(new DelayCompletionEvent(this));
                    this.task.cancel();
                    return;
                }
                sendActionBar(uuid);
                this.remaining--;
                return;
            }
            Bukkit.getPluginManager().callEvent(new DelayCompletionEvent(this));
            this.task.cancel();
        }, 0L, 20L);
    }

    public int getRemaining() {
        return this.remaining;
    }

    public void sendActionBar(UUID uuid) {
        ActionBarType.DELAY_COUNTDOWN.send(Bukkit.getPlayer(this.uuid), String.valueOf(this.remaining));
    }

    public BukkitTask getTask() {
        return task;
    }

    public Location getDestination() {
        return destination;
    }

    public UUID getUUID() {
        return uuid;
    }

    public CooldownType getType() {
        return type;
    }

    public Location getStarting() {
        return starting;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean complete() {
        return this.complete;
    }

    public FPlayer getfPlayer() {
        return fPlayer;
    }
}
