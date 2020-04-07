package gg.steve.anthem.core;

import gg.steve.anthem.disband.FactionDeletion;
import gg.steve.anthem.exception.PlayerAlreadyMemberException;
import gg.steve.anthem.exception.PlayerAlreadyModeratorException;
import gg.steve.anthem.exception.PlayerNotMemberException;
import gg.steve.anthem.exception.PlayerNotModeratorException;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.world.FWorld;
import gg.steve.anthem.world.FWorldGeneration;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Faction {
    private UUID owner;
    private List<UUID> moderators;
    private List<UUID> members;
    private String name;
    private FWorld fWorld;
    private FactionDataFileUtil data;
    private Location home;

    public Faction(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        if (name.equalsIgnoreCase("wilderness")) {
            this.fWorld = new FWorld(Bukkit.getWorld("world"));
        } else {
            this.fWorld = new FWorld(FWorldGeneration.generate(name));
        }
        this.data = new FactionDataFileUtil(name);
        this.data.get().set("owner-uuid", owner.toString());
        this.data.save();
        this.home = fWorld.getSpawnLocation();
        moderators = new ArrayList<>();
        members = new ArrayList<>();
    }

    public Faction(String name) {
        this.name = name;
        if (name.equalsIgnoreCase("wilderness")) {
            this.fWorld = new FWorld(Bukkit.getWorld("world"));
        } else {
            this.fWorld = new FWorld(Bukkit.getWorld("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + name));
        }
        this.data = new FactionDataFileUtil(name);
        this.owner = UUID.fromString(data.get().getString("owner-uuid"));
        loadModerators();
        loadMembers();
        this.home = fWorld.getBlockAt(data.get().getInt("home-location.x"), data.get().getInt("home-location.y"), data.get().getInt("home-location.z")).getLocation();
    }

    public boolean isMember(FPlayer fPlayer) {
        UUID uuid = fPlayer.getUUID();
        if (uuid.equals(owner)) return true;
        if (this.moderators.contains(uuid)) return true;
        return this.members.contains(uuid);
    }

    public Role getRole(FPlayer fPlayer) {
        UUID uuid = fPlayer.getUUID();
        if (uuid.equals(this.owner)) return Role.OWNER;
        if (this.moderators.contains(uuid)) return Role.MODERATOR;
        if (this.members.contains(uuid)) return Role.MEMBER;
        return Role.WILDERNESS;
    }

    public void disband() {
        FactionDeletion.disband(this);
    }

    public List<UUID> getPlayers() {
        List<UUID> players = new ArrayList<>();
        players.add(owner);
        players.addAll(moderators);
        players.addAll(members);
        return players;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<UUID> getModerators() {
        return this.moderators;
    }

    public void loadModerators() {
        moderators = new ArrayList<>();
        for (String moderator : data.get().getStringList("moderators")) {
            this.moderators.add(UUID.fromString(moderator));
        }
    }

    public void addModerator(UUID moderator) throws PlayerAlreadyModeratorException {
        if (this.data.get().getStringList("moderators").contains(moderator.toString()))
            throw new PlayerAlreadyModeratorException();
        this.data.get().getStringList("moderators").add(moderator.toString());
        this.data.save();
        this.moderators.add(moderator);
    }

    public void removeModerator(UUID moderator) throws PlayerNotModeratorException {
        if (!this.data.get().getStringList("moderators").contains(moderator.toString()))
            throw new PlayerNotModeratorException();
        this.data.get().getStringList("moderators").remove(moderator.toString());
        this.data.save();
        this.moderators.remove(moderator);
    }

    public List<UUID> getMembers() {
        return this.members;
    }

    public void loadMembers() {
        members = new ArrayList<>();
        for (String member : this.data.get().getStringList("members")) {
            this.members.add(UUID.fromString(member));
        }
    }

    public void addMember(UUID member) throws PlayerAlreadyMemberException {
        if (this.data.get().getStringList("members").contains(member.toString()))
            throw new PlayerAlreadyMemberException();
        this.data.get().getStringList("members").add(member.toString());
        this.data.save();
        this.members.add(member);
    }

    public void removeMember(UUID member) throws PlayerNotMemberException {
        if (!this.data.get().getStringList("members").contains(member.toString()))
            throw new PlayerNotMemberException();
        this.data.get().getStringList("members").remove(member.toString());
        this.data.save();
        this.members.remove(member);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FWorld getFWorld() {
        return this.fWorld;
    }

    public void setFFWorld(FWorld fWorld) {
        this.fWorld = fWorld;
    }

    public FactionDataFileUtil getData() {
        return data;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        data.get().set("location.x", home.getX());
        data.get().set("location.y", home.getY());
        data.get().set("location.z", home.getZ());
        data.save();
        this.home = home;
    }
}
