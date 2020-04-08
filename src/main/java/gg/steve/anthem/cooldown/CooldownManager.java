package gg.steve.anthem.cooldown;

import gg.steve.anthem.exception.CooldownActiveException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CooldownManager {
    private static HashMap<UUID, List<Cooldown>> cooldowns;

    public static void init() {
        cooldowns = new HashMap<>();
    }

    public static void addCooldown(UUID uuid, Cooldown cooldown) throws CooldownActiveException {
        if (!cooldowns.containsKey(uuid)) cooldowns.put(uuid, new ArrayList<>());
        for (Cooldown active : cooldowns.get(uuid)) {
            if (active.getType().equals(cooldown.getType())) {
                if (active.isActive()) {
                    throw new CooldownActiveException();
                } else {
                    cooldowns.get(uuid).remove(active);
                    break;
                }
            }
        }
        cooldowns.get(uuid).add(cooldown);
    }

    public static boolean isOnCooldown(UUID uuid, CooldownType type) {
        for (Cooldown cooldown : cooldowns.get(uuid)) {
            if (cooldown.getType().equals(type)) {
                if (cooldown.isActive()) {
                    return true;
                } else {
                    cooldowns.get(uuid).remove(cooldown);
                    return false;
                }
            }
        }
        return false;
    }

    public static void message(UUID uuid, CooldownType type) {
        for (Cooldown cooldown : cooldowns.get(uuid)) {
            if (cooldown.getType().equals(type)) {
                cooldown.message(uuid);
                return;
            }
        }
    }

    public static Cooldown getCooldown(UUID uuid, CooldownType type) {
        for (Cooldown cooldown : cooldowns.get(uuid)) {
            if (cooldown.getType().equals(type)) return cooldown;
        }
        return null;
    }

    public static void removeCooldown(UUID uuid, CooldownType type) {
        for (Cooldown cooldown : cooldowns.get(uuid)) {
            if (cooldown.getType().equals(type)) cooldowns.get(uuid).remove(cooldown);
            return;
        }
    }
}
