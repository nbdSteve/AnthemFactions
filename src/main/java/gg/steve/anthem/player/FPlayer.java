package gg.steve.anthem.player;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.role.Role;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FPlayer {
    private Faction faction;
    private Player player;
    private Role role;

    public FPlayer(Player player) {
        this.player = player;
        this.faction = FactionManager.getFaction(player);
        this.role = faction.getRole(player);
    }

    public Role getRole() {
        return role;
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public Faction getFaction() {
        return faction;
    }
}
