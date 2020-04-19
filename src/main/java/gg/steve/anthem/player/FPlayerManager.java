package gg.steve.anthem.player;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.upgrade.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDate;
import java.util.*;

public class FPlayerManager implements Listener {
    private static Map<UUID, FPlayer> players;
    private static List<String> disbandedPlayers;

    public static void init() {
        players = new HashMap<>();
        disbandedPlayers = new ArrayList<>();
        disbandedPlayers.addAll(FileManager.get("disbanded-players").getStringList("disbanded-players"));
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
        boolean bypassed = getFPlayer(uuid).isBypassed();
        players.remove(uuid);
        players.put(uuid, new FPlayer(uuid));
        if (bypassed) {
            getFPlayer(uuid).setBypassed(true);
        }
    }

    public static void addDisbandedPlayer(UUID uuid) {
        disbandedPlayers.add(uuid.toString());
    }

    public static void removeDisbandedPlayer(UUID uuid) {
        disbandedPlayers.remove(uuid.toString());
    }

    public static void saveDisbandedPlayersToFile() {
        FileManager.get("disbanded-players").set("disbanded-players", disbandedPlayers);
        FileManager.save("disbanded-players");
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        addFPlayer(event.getPlayer().getUniqueId());
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isRaiding() && fPlayer.inRaidWorld()) {
            if (fPlayer.getFaction().getUpgrade(UpgradeType.RAIDING).getLevel() > 2) {
                fPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
            }
            return;
        }
        if ((!fPlayer.isInHomeWorld()
                && !fPlayer.getWorld().getName().equalsIgnoreCase(FileManager.get("config").getString("main-world-name")))
                || disbandedPlayers.contains(fPlayer.getUUID().toString())) {
            removeDisbandedPlayer(fPlayer.getUUID());
            event.getPlayer().teleport(Bukkit.getWorld(FileManager.get("config").getString("main-world-name")).getSpawnLocation());
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isRaiding() && fPlayer.getFaction().getUpgrade(UpgradeType.RAIDING).getLevel() > 2) {
            fPlayer.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        fPlayer.getFaction().getData().get().set("last-active", LocalDate.now().toString());
        fPlayer.getFaction().getData().save();
        removeFPlayer(event.getPlayer().getUniqueId());
    }
}