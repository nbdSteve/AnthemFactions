package gg.steve.anthem.core;

import gg.steve.anthem.disband.FactionDeletion;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.LogUtil;
import gg.steve.anthem.world.FWorld;
import gg.steve.anthem.world.FWorldGeneration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.*;

public class Faction {
    private UUID id;
    private String name;
    private FWorld fWorld;
    private FactionDataFileUtil data;
    private Location home;
    private Map<UUID, Role> playerMap;

    public Faction(UUID owner, String name, UUID id) {
        this.id = id;
        this.name = name;
        if (id.equals(FactionManager.getWildernessId())) {
            this.fWorld = new FWorld(Bukkit.createWorld(new WorldCreator(FileManager.get("config").getString("main-world-name"))));
        } else {
            this.fWorld = new FWorld(FWorldGeneration.generate(String.valueOf(id)));
        }
        this.data = new FactionDataFileUtil(String.valueOf(id), name);
        addPlayer(owner, Role.OWNER);
        this.home = fWorld.getSpawnLocation();
    }

    public Faction(UUID id) {
        this.id = id;
        if (id.equals(FactionManager.getWildernessId())) {
            this.fWorld = new FWorld(Bukkit.createWorld(new WorldCreator(FileManager.get("config").getString("main-world-name"))));
        } else {
            this.fWorld = new FWorld(Bukkit.createWorld(new WorldCreator("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + id.toString())));
            LogUtil.info("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + id);
        }
        this.data = new FactionDataFileUtil(String.valueOf(id));
        this.name = this.data.get().getString("name");
        loadPlayerMap();
        this.home = fWorld.getBlockAt(data.get().getInt("home-location.x"), data.get().getInt("home-location.y"), data.get().getInt("home-location.z")).getLocation();
    }

    public boolean isMember(FPlayer fPlayer) {
        return playerMap.containsKey(fPlayer.getUUID());
    }

    public Role getRole(FPlayer fPlayer) {
        if (playerMap.containsKey(fPlayer.getUUID())) return playerMap.get(fPlayer.getUUID());
        return Role.WILDERNESS;
    }

    public void disband() {
        FactionDeletion.disband(this);
    }

    public Set<UUID> getPlayers() {
        return playerMap.keySet();
    }

    public void loadPlayerMap() {
        if (playerMap == null) playerMap = new HashMap<>();
        for (String uuid : this.data.get().getConfigurationSection("faction-members").getKeys(true)) {
            LogUtil.info(uuid);
            this.playerMap.put(UUID.fromString(uuid), Role.valueOf(this.data.get().getString("faction-members." + uuid)));
        }
    }

    public boolean addPlayer(UUID uuid, Role role) {
        if (playerMap == null) playerMap = new HashMap<>();
        if (playerMap.containsKey(uuid)) return false;
        this.data.get().set("faction-members." + uuid, role.toString());
        this.data.save();
        playerMap.put(uuid, role);
        return true;
    }

    public boolean removePlayer(UUID uuid) {
        if (!playerMap.containsKey(uuid)) return false;
        this.data.get().set("faction-members." + uuid, null);
        this.data.save();
        playerMap.remove(uuid);
        return true;
    }

    public boolean promote(UUID uuid) {
        Role role = playerMap.get(uuid);
        if (role.equals(Role.OWNER)) return false;
        if (role.equals(Role.CO_OWNER)) {
            UUID previousOwner = getOwner();
            this.data.get().set("faction-members." + uuid, Role.OWNER);
            this.data.get().set("faction-members." + previousOwner, Role.CO_OWNER);
            this.data.save();
            playerMap.remove(uuid);
            playerMap.remove(getOwner());
            playerMap.put(uuid, Role.OWNER);
            playerMap.put(previousOwner, Role.CO_OWNER);
        }
        if (role.equals(Role.MODERATOR)) {
            this.data.get().set("faction-members." + uuid, Role.CO_OWNER);
            this.data.save();
            playerMap.remove(uuid);
            playerMap.put(uuid, Role.CO_OWNER);
            return true;
        }
        if (role.equals(Role.MEMBER)) {
            this.data.get().set("faction-members." + uuid, Role.MODERATOR);
            this.data.save();
            playerMap.remove(uuid);
            playerMap.put(uuid, Role.MODERATOR);
            return true;
        }
        return false;
    }

    public boolean demote(UUID uuid) {
        Role role = playerMap.get(uuid);
        if (role.equals(Role.OWNER) || role.equals(Role.MEMBER)) return false;
        if (role.equals(Role.CO_OWNER)) {
            this.data.get().set("faction-members." + uuid, Role.MODERATOR);
            this.data.save();
            playerMap.remove(uuid);
            playerMap.put(uuid, Role.MODERATOR);
            return true;
        }
        if (role.equals(Role.MODERATOR)) {
            this.data.get().set("faction-members." + uuid, Role.MEMBER);
            this.data.save();
            playerMap.remove(uuid);
            playerMap.put(uuid, Role.MEMBER);
            return true;
        }
        return false;
    }

    public UUID getOwner() {
        for (Map.Entry player : playerMap.entrySet()) {
            if (player.getValue().equals(Role.OWNER)) return (UUID) player.getKey();
        }
        return null;
    }

    public List<UUID> getCoOwners() {
        List<UUID> coOwners = new ArrayList<>();
        for (Map.Entry player : playerMap.entrySet()) {
            if (player.getValue().equals(Role.CO_OWNER)) coOwners.add((UUID) player.getKey());
        }
        return coOwners;
    }

    public List<UUID> getModerators() {
        List<UUID> moderators = new ArrayList<>();
        for (Map.Entry player : playerMap.entrySet()) {
            if (player.getValue().equals(Role.MODERATOR)) moderators.add((UUID) player.getKey());
        }
        return moderators;
    }

    public List<UUID> getMembers() {
        List<UUID> members = new ArrayList<>();
        for (Map.Entry player : playerMap.entrySet()) {
            if (player.getValue().equals(Role.MEMBER)) members.add((UUID) player.getKey());
        }
        return members;
    }

    public UUID getId() {
        return id;
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

    public FactionDataFileUtil getData() {
        return data;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        data.get().set("home-location.x", home.getX());
        data.get().set("home-location.y", home.getY());
        data.get().set("home-location.z", home.getZ());
        data.save();
        this.home = home;
    }
}
