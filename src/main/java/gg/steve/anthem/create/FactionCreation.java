package gg.steve.anthem.create;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.message.BroadcastType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FactionCreation {

    public static Faction create(String name, Player owner, UUID id) {
        Faction faction = new Faction(owner.getUniqueId(), name, id);
        BroadcastType.CREATION.broadcast(faction.getName(), owner.getName());
//        owner.teleport(faction.getFWorld().getSpawnLocation());
        return faction;
    }
}
