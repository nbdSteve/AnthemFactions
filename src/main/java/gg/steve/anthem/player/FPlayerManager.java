package gg.steve.anthem.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class FPlayerManager implements Listener {
    public static Map<Player, FPlayer> players;

    public static void init() {
        players = new HashMap<>();
    }

    public static FPlayer getFPlayer(Player player) {
        if (players.containsKey(player)) {
            players.put(player, new FPlayer(player));
        }
        return players.get(player);
    }

    public static void addFPlayer(Player player) {
        players.put(player, new FPlayer(player));
    }

    public static void removeFPlayer(Player player) {
        players.remove(player);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        addFPlayer(event.getPlayer());
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        removeFPlayer(event.getPlayer());
    }
}
