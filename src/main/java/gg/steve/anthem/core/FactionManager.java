package gg.steve.anthem.core;

import gg.steve.anthem.create.FactionCreation;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.LogUtil;
import gg.steve.anthem.world.FWorld;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

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
        MessageType.CREATE.message(owner, name);
    }

    public static void disbandFaction(Faction faction) {
        factions.remove(faction.getId());
        faction.messageAllOnlinePlayers(MessageType.DISBAND, faction.getName());
        List<UUID> onlinePlayers = faction.getOnlinePlayers();
        //remove all relations with other factions
        faction.disband();
        for (UUID uuid : onlinePlayers) {
            FPlayerManager.updateFPlayer(uuid);
        }
    }

    public static Faction getFaction(FPlayer fPlayer) {
        for (Faction faction : factions.values()) {
            if (faction.isMember(fPlayer)) return faction;
        }
        return getWilderness();
    }

    public static Faction getFaction(String name) {
        for (Faction faction : factions.values()) {
            if (faction.getName().equalsIgnoreCase(name)) return faction;
        }
        return null;
    }

    public static Faction getFaction(UUID factionUUID) {
        return factions.get(factionUUID);
    }

    public static UUID getId(String name) {
        for (Faction faction : factions.values()) {
            if (faction.getName().equalsIgnoreCase(name)) return faction.getId();
        }
        return null;
    }

    public static boolean changeTag(Faction faction, String newName) {
        if (factionAlreadyExists(newName)) return false;
        faction.setName(newName);
        return true;
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

    public static Set<UUID> getFactions() {
        return factions.keySet();
    }

    public static List<Faction> getFactionsAsList() {
        List<Faction> facs = new ArrayList<>();
        for (UUID uuid : getFactions()) {
            facs.add(FactionManager.getFaction(uuid));
        }
        return facs;
    }

    public static int getTotalFactions() {
        return factions.size() - 1;
    }

    public static FWorld getFWorld(UUID uuid) {
        return factions.get(uuid).getFWorld();
    }

    public static boolean factionIsWilderness(Faction faction) {
        return faction.getId().equals(getWildernessId());
    }
}
