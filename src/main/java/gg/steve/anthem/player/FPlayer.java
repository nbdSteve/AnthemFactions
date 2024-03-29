package gg.steve.anthem.player;

import gg.steve.anthem.chat.ChatType;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.world.FWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class FPlayer {
    private Faction faction;
    private OfflinePlayer player;
    private Role role;
    private boolean bypassed;
    private ChatType channel;
    private boolean flying;

    public FPlayer(UUID uuid) {
        this.player = Bukkit.getOfflinePlayer(uuid);
        this.faction = FactionManager.getFaction(this);
        this.role = faction.getRole(this);
        this.bypassed = false;
        this.channel = ChatType.PUBLIC;
        this.flying = false;
    }

    public Role getRole() {
        return this.role;
    }

    public Faction getFaction() {
        return this.faction;
    }

    public void teleportHome() {
        this.player.getPlayer().teleport(getFaction().getHome());
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    public Player getPlayer() {
        return this.player.getPlayer();
    }

    public Location getLocation() {
        return this.player.getPlayer().getLocation();
    }

    public void message(String message) {
        this.player.getPlayer().sendMessage(message);
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

    public boolean hasFactionPermission(PermissionNode node) {
        return this.faction.roleHasPermission(this.role, node.get());
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

    public String getName() {
        return player.getName();
    }

    public boolean isInHomeWorld() {
        try {
            return getFWorldUUID().equals(faction.getId());
        } catch (NullPointerException e) {
            return false;
        }
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
        return this.player.getPlayer().getWorld();
    }

    public void setBypassed(boolean value) {
        this.bypassed = value;
    }

    public boolean isBypassed() {
        return this.bypassed;
    }

    public boolean isFlying() {
        return this.flying;
    }

    public void setFlying(boolean flying) {
        this.player.getPlayer().setAllowFlight(flying);
        this.player.getPlayer().setFlying(flying);
        this.flying = flying;
    }

    // raid shit
    public boolean isRaiding() {
        return this.faction.isRaiding();
    }

    public boolean isBeingRaided() {
        return this.faction.isBeingRaided();
    }

    public boolean isRaider() {
        return this.faction.getfRaid().isRaider(getUUID());
    }

    public void teleportToRaid() {
        this.faction.getfRaid().teleportToRaid(getUUID(), FactionManager.getFaction(this.faction.getfRaid().getDefendingFaction()));
    }

    public boolean inRaidWorld() {
        if (!isRaiding()) return false;
        return player.getPlayer().getWorld().getName().equals(FactionManager.getFaction(faction.getfRaid().getDefendingFaction()).getFWorld().getName());
    }
}