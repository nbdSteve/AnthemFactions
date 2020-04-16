package gg.steve.anthem.delay;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.delay.exception.InvalidDelayTypeException;
import gg.steve.anthem.utils.MessageUtil;
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

    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(AnthemFactions.get(), () -> {
            if (this.remaining > 0) {
                messageCountdown(uuid);
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

    public void messageCountdown(UUID uuid) {
        MessageUtil.message("lang", "delay-countdown", Bukkit.getPlayer(uuid), "{seconds-remaining}", String.valueOf(getRemaining()));
    }

    public void messageComplete(UUID uuid) {
        MessageUtil.message("lang", "delay", Bukkit.getPlayer(uuid), "{seconds-remaining}", String.valueOf(getRemaining()));
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
}
