package gg.steve.anthem.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FPlayerManager implements Listener {
    public static Map<UUID, FPlayer> players;

    public static void init() {
        players = new HashMap<>();
    }

    public static FPlayer getFPlayer(UUID uuid) {
        if (!players.containsKey(uuid)) {
            players.put(uuid, new FPlayer(uuid));
        }
        return players.get(uuid);
    }

    public static void addFPlayer(UUID uuid) {
        players.put(uuid, new FPlayer(uuid));
    }

    public static void removeFPlayer(UUID uuid) {
        players.remove(uuid);
    }

    public static void updateFPlayer(UUID uuid) {
        players.remove(uuid);
        players.put(uuid, new FPlayer(uuid));
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        addFPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        removeFPlayer(event.getPlayer().getUniqueId());
    }
}