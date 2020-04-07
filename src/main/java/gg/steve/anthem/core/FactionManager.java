package gg.steve.anthem.core;

import gg.steve.anthem.create.FactionCreation;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.LogUtil;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FactionManager {
    private static Map<String, Faction> factions;
    private static Wilderness wilderness;

    public static void init() {
        factions = new HashMap<>();
        File dataFolder = new File("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-data");
        if (dataFolder.exists()) {
            for (File faction : dataFolder.listFiles()) {
                String[] name = faction.getName().split(".yml");
                LogUtil.info("Loading files for faction: " + name[0]);
                factions.put(name[0], new Faction(name[0]));
            }
        }
        wilderness = new Wilderness();
    }

    public static void createFaction(String name, Player owner) {
        factions.put(name, FactionCreation.create(name, owner));
        FPlayerManager.updateFPlayer(owner.getUniqueId());
        MessageUtil.message("lang", "create-faction", owner, "{faction-name}", name);
    }

    public static void disbandFaction(String name) {
        List<UUID> factionMembers = factions.get(name).getPlayers();
        factions.get(name).disband();
        factions.remove(name);
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

    public static Wilderness getWilderness() {
        return wilderness;
    }
}
