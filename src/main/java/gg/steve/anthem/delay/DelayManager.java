package gg.steve.anthem.delay;

import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.exception.DelayAlreadyActiveException;
import gg.steve.anthem.exception.InvalidDelayTypeException;
import gg.steve.anthem.exception.NotOnDelayException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DelayManager implements Listener {
    private static HashMap<UUID, List<Delay>> delays;

    public static void init() {
        delays = new HashMap<>();
    }

    public static void addDelay(UUID uuid, CooldownType type, Location destination) throws DelayAlreadyActiveException {
        delays.computeIfAbsent(uuid, k -> new ArrayList<>());
        if (onDelay(uuid, type)) throw new DelayAlreadyActiveException();
        try {
            delays.get(uuid).add(new Delay(uuid, type, destination));
        } catch (InvalidDelayTypeException e) {
            e.printStackTrace();
        }
    }

    public static Delay getDelay(UUID uuid, CooldownType type) throws NotOnDelayException {
        if (delays.get(uuid) == null) throw new NotOnDelayException();
        for (Delay delay : delays.get(uuid)) {
            if (delay.getType().equals(type)) return delay;
        }
        throw new NotOnDelayException();
    }

    public static boolean onDelay(UUID uuid, CooldownType type) {
        if (delays.get(uuid) == null) return false;
        for (Delay delay : delays.get(uuid)) {
            if (delay.getType().equals(type)) return true;
        }
        return false;
    }

    @EventHandler
    public void completionEvent(DelayCompletionEvent event) {
        delays.remove(event.getDelay().getUUID());
        if (!event.isCancelled()) return;
        Bukkit.getPlayer(event.getDelay().getUUID()).teleport(event.getDelay().getDestination());
        event.getDelay().setComplete(true);
        event.getDelay().getTask().cancel();
    }
}