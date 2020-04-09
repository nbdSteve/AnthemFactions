package gg.steve.anthem.delay;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.exception.InvalidDelayTypeException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Delay {
    private UUID uuid;
    private CooldownType type;
    private Long finish;
    private BukkitTask task;
    private boolean complete;
    private Location destination;

    public Delay(UUID uuid, CooldownType type, Location destination) throws InvalidDelayTypeException {
        this.uuid = uuid;
        if (!type.name().contains("_TELEPORT")) {
            throw new InvalidDelayTypeException();
        }
        this.type = type;
        this.finish = System.currentTimeMillis() + (type.getDuration() * 1000);
        this.complete = false;
        this.destination = destination;
        start();
    }

    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(AnthemFactions.get(), () -> {
            if (finish <= System.currentTimeMillis()) return;
            Bukkit.getPluginManager().callEvent(new DelayCompletionEvent(this));
        }, 0L, 20L);
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

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean complete() {
        return this.complete;
    }
}
