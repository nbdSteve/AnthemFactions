package gg.steve.anthem.core;

import gg.steve.anthem.create.FactionCreation;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.LogUtil;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FactionManager {
    private static Map<UUID, Faction> factions;
    private static Wilderness wilderness;

    public static void init() {
        factions = new HashMap<>();
        wilderness = new Wilderness();
        File dataFolder = new File("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-data");
        if (dataFolder.exists()) {
            for (File faction : dataFolder.listFiles()) {
                UUID id = UUID.fromString(faction.getName().split(".yml")[0]);
                LogUtil.info("Loading the faction with id: " + id);
                factions.put(id, new Faction(id));
            }
        }
    }

    public static void createFaction(String name, Player owner, UUID id) {
        factions.put(id, FactionCreation.create(name, owner, id));
        FPlayerManager.updateFPlayer(owner.getUniqueId());
        MessageUtil.message("lang", "create-faction", owner, "{faction-name}", name);
    }

    public static void disbandFaction(Faction faction) {
        Set<UUID> factionMembers = faction.getPlayers();
        String name = faction.getName();
        factions.remove(faction.getId());
        faction.disband();
        for (UUID uuid : factionMembers) {
            FPlayerManager.updateFPlayer(uuid);
            MessageUtil.message("lang", "disband-faction", Bukkit.getPlayer(uuid), "{faction-name}", name);
        }
    }

    public static Faction getFaction(FPlayer fPlayer) {
        for (Faction faction : factions.values()) {
            if (faction.isMember(fPlayer)) return faction;
        }
        return getWilderness();
    }

    public static UUID getId(String name) {
        for (Faction faction : factions.values()) {
            if (faction.getName().equalsIgnoreCase(name)) return faction.getId();
        }
        return null;
    }

    public static boolean factionAlreadyExists(String name) {
        for (Faction faction : factions.values()) {
            if (faction.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static Wilderness getWilderness() {
        return wilderness;
    }

    public static UUID getWildernessId() {
        if (wilderness == null) {
            return UUID.fromString(FileManager.get("config").getString("wilderness-uuid"));
        }
        return wilderness.getId();
    }
}
