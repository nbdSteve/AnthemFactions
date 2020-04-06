package gg.steve.anthem.core;

import gg.steve.anthem.create.WorldGeneration;
import gg.steve.anthem.disband.FactionDeletion;
import gg.steve.anthem.exception.PlayerAlreadyModeratorException;
import gg.steve.anthem.exception.PlayerAlreadyRecruitException;
import gg.steve.anthem.exception.PlayerNotModeratorException;
import gg.steve.anthem.exception.PlayerNotRecruitException;
import gg.steve.anthem.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Faction {
    private UUID owner;
    private List<UUID> moderators;
    private List<UUID> recruits;
    private String name;
    private World world;
    private FactionDataFileUtil data;

    public Faction(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.world = WorldGeneration.generate(name);
        this.data = new FactionDataFileUtil(name, owner);
    }

    public Faction(String name) {
        this.name = name;
        this.world = Bukkit.getWorld(name);
        this.data = new FactionDataFileUtil(name, null);
        this.owner = UUID.fromString(data.get().getString("owner-uuid"));
        loadModerators();
        loadRecruits();
    }

    public boolean isMember(Player player) {
        UUID uuid = player.getUniqueId();
        if (uuid == this.owner) return true;
        if (this.moderators.contains(uuid)) return true;
        if (this.recruits.contains(uuid)) return true;
        return false;
    }

    public Role getRole(Player player) {
        UUID uuid = player.getUniqueId();
        if (uuid == this.owner) return Role.OWNER;
        if (this.moderators.contains(uuid)) return Role.MODERATOR;
        if (this.recruits.contains(uuid)) return Role.RECRUIT;
        return Role.WILDERNESS;
    }

    public void disband() {
        FactionDeletion.disband(this);
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

    public List<UUID> getRecruits() {
        return this.recruits;
    }

    public void loadRecruits() {
        for (String recruit : this.data.get().getStringList("recruits")) {
            this.recruits.add(UUID.fromString(recruit));
        }
    }

    public void addRecruit(UUID recruit) throws PlayerAlreadyRecruitException {
        if (this.data.get().getStringList("moderators").contains(recruit.toString()))
            throw new PlayerAlreadyRecruitException();
        this.data.get().getStringList("moderators").add(recruit.toString());
        this.data.save();
        this.recruits.add(recruit);
    }

    public void removeRecruit(UUID recruit) throws PlayerNotRecruitException {
        if (!this.data.get().getStringList("moderators").contains(recruit.toString()))
            throw new PlayerNotRecruitException();
        this.data.get().getStringList("moderators").remove(recruit.toString());
        this.data.save();
        this.recruits.remove(recruit);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public FactionDataFileUtil getData() {
        return data;
    }
}
