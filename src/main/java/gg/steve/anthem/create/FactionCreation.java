package gg.steve.anthem.create;

import gg.steve.anthem.core.Faction;
import org.bukkit.entity.Player;

public class FactionCreation {

    public static Faction create(String name, Player owner) {
        Faction faction = new Faction(owner.getUniqueId(), name);
        owner.teleport(faction.getWorld().getSpawnLocation());
        return faction;
    }
}
