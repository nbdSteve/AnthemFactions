package gg.steve.anthem.core;

import gg.steve.anthem.create.FactionCreation;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FactionManager {
    private static Map<String, Faction> factions;

    public static void init() {
        factions = new HashMap<>();
    }

    public static void loadFaction(String name) {

    }

    public static void createFaction(String name, Player owner) {
        factions.put(name, FactionCreation.create(name, owner));
    }

    public static void disbandFaction(String name) {
        factions.get(name).disband();
        factions.remove(name);
    }
}
