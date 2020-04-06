package gg.steve.anthem.core;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.create.WorldGeneration;
import gg.steve.anthem.disband.FactionDeletion;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class Faction {
    private UUID owner;
    private List<UUID> moderators;
    private List<UUID> recruits;
    private String name;
    private World world;

    public Faction(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.world = WorldGeneration.generate(name);
    }

    public void disband() {
        FactionDeletion.disband(this);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<UUID> getModerators() {
        return moderators;
    }

    public void setModerators(List<UUID> moderators) {
        this.moderators = moderators;
    }

    public List<UUID> getRecruits() {
        return recruits;
    }

    public void setRecruits(List<UUID> recruits) {
        this.recruits = recruits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
