package gg.steve.anthem.delay;

import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.delay.exception.DelayAlreadyActiveException;
import gg.steve.anthem.delay.exception.InvalidDelayTypeException;
import gg.steve.anthem.delay.exception.NotOnDelayException;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.utils.MessageUtil;
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

    public static void message(UUID uuid, CooldownType type) throws NotOnDelayException {
        if (!onDelay(uuid, type)) throw new NotOnDelayException();
        for (Delay delay : delays.get(uuid)) {
            if (delay.getType().equals(type)) {
                delay.messageCountdown(uuid);
                return;
            }
        }
    }

    @EventHandler
    public void completionEvent(DelayCompletionEvent event) {
        delays.remove(event.getDelay().getUUID());
        if (event.isCancelled()) return;
        if (!isSamePosition(Bukkit.getPlayer(event.getDelay().getUUID()).getLocation(), event.getDelay().getStarting())) {
            MessageUtil.message("lang", "teleport-failed", Bukkit.getPlayer(event.getDelay().getUUID()));
            return;
        }
        Bukkit.getPlayer(event.getDelay().getUUID()).teleport(event.getDelay().getDestination());
        MessageType.TELEPORT.message(Bukkit.getPlayer(event.getDelay().getUUID()), event.getDelay().getType().toString().split("_")[0]);
        event.getDelay().setComplete(true);
        event.getDelay().getTask().cancel();
    }

    public boolean isSamePosition(Location loc1, Location loc2) {
        if (loc1.getX() == loc2.getX()
                && loc1.getY() == loc2.getY()
                && loc1.getZ() == loc2.getZ()) return true;
        return false;
    }
}