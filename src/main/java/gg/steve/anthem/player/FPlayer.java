package gg.steve.anthem.player;

import gg.steve.anthem.chat.ChatType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.world.FWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class FPlayer {
    private Faction faction;
    private Player player;
    private Role role;
    private boolean bypassed;
    private ChatType channel;

    public FPlayer(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.faction = FactionManager.getFaction(this);
        this.role = faction.getRole(this);
        this.bypassed = false;
        this.channel = ChatType.PUBLIC;
    }

    public Role getRole() {
        return this.role;
    }

    public Faction getFaction() {
        return this.faction;
    }

    public void teleportHome() {
        this.player.teleport(getFaction().getHome());
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getLocation() {
        return this.player.getLocation();
    }

    public void message(String message) {
        this.player.sendMessage(message);
    }

    public boolean canBuild(Location location) {
        if (isBypassed()) return true;
        if (isInHomeWorld()) {
            return this.faction.getFWorld().inFactionLand(location);
        } else if (inFactionWorld()) {
            return !getFWorld().inFactionLand(location);
        }
        return false;
    }

    public boolean hasFaction() {
        return !role.equals(Role.WILDERNESS);
    }

    public boolean hasFactionPermission(String node) {
        return this.faction.roleHasPermission(this.role, node);
    }

    public ChatType getChatChannel() {
        return channel;
    }

    public void setChatChannel(ChatType channel) {
        this.channel = channel;
    }

    public boolean inFactionWorld() {
        return getFWorldUUID() != null;
    }

    public UUID getFWorldUUID() {
        File world = new File(getWorld().getName());
        try {
            return UUID.fromString(world.getName());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isInHomeWorld() {
        return getFWorldUUID().equals(faction.getId());
    }

    public FWorld getFWorld() {
        if (!inFactionWorld()) return null;
        return FactionManager.getFWorld(getFWorldUUID());
    }

    public Faction getFactionForCurrentFWorld() {
        if (!inFactionWorld()) return null;
        return FactionManager.getFaction(getFWorldUUID());
    }

    public World getWorld() {
        return this.player.getWorld();
    }

    public void setBypassed(boolean value) {
        this.bypassed = value;
    }

    public boolean isBypassed() {
        return bypassed;
    }
}