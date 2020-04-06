package gg.steve.anthem.core;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.create.FactionCreation;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FactionManager {
    private static Map<String, Faction> factions;

    public static void init() {
        factions = new HashMap<>();
        File dataFolder = new File("plugins" + File.separator + AnthemFactions.get().getDataFolder().getName() + File.separator + "faction-data");
        if (dataFolder.exists()) {
            for (File faction : dataFolder.listFiles()) {
                String[] name = faction.getName().split(".yml");
                LogUtil.info("Loading files for faction: " + name[0]);
                factions.put(name[0], new Faction(name[0]));
            }
        }
    }

    public static void createFaction(String name, Player owner) {
        factions.put(name, FactionCreation.create(name, owner));
    }

    public static void disbandFaction(String name) {
        factions.get(name).disband();
        factions.remove(name);
    }

    public static Faction getFaction(Player player) {
        for (Faction faction : factions.values()) {
            if (faction.isMember(player)) return faction;
        }
        return null;
    }
}
