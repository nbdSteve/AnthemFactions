package gg.steve.anthem.core;

import gg.steve.anthem.disband.FactionDeletion;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionGui;
import gg.steve.anthem.permission.PermissionPageGui;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.raid.FRaid;
import gg.steve.anthem.relation.RelationManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.upgrade.Upgrade;
import gg.steve.anthem.upgrade.UpgradeGui;
import gg.steve.anthem.upgrade.UpgradeType;
import gg.steve.anthem.upgrade.fchest.FChest;
import gg.steve.anthem.upgrade.fchest.FChestManager;
import gg.steve.anthem.utils.TimeUtil;
import gg.steve.anthem.world.FWorld;
import gg.steve.anthem.world.FWorldGeneration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Faction {
    private UUID id;
    private String name;
    private FWorld fWorld;
    private FactionDataFileUtil data;
    private Location home;
    private Map<UUID, Role> playerMap;
    private Map<Role, List<String>> rolePermissionMap;
    private RelationManager relationManager;
    private String founded;
    private double wealth;
    private int xp;
    private Map<UpgradeType, Upgrade> upgrades;
    private UpgradeGui upgradeGui;
    private FChest fChest;
    private Map<Role, PermissionPageGui> permissionsPageGuiMap;
    private PermissionGui permsMenuGui;
    private int raidCooldown;
    private boolean raidActive;
    private FRaid fRaid;
    private LocalDate lastActive;

    public Faction(UUID owner, String name, UUID id) {
        this.id = id;
        this.name = name;
        if (isWilderness()) {
            this.fWorld = new FWorld(Bukkit.createWorld(new WorldCreator(FileManager.get("config").getString("main-world-name"))), 0, 0);
        } else {
            FWorldGeneration.generate(String.valueOf(id));
            this.fWorld = new FWorld(Bukkit.createWorld(new WorldCreator("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + id.toString())), 128, 64);
        }
        this.data = new FactionDataFileUtil(String.valueOf(id), name);
        addPlayer(owner, Role.OWNER);
        loadRolePermissionMap();
        this.home = fWorld.getSpawnLocation();
        this.relationManager = new RelationManager(this);
        this.founded = this.data.get().getString("founded");
        this.wealth = this.data.get().getDouble("wealth");
        this.xp = this.data.get().getInt("xp-bank");
        this.raidCooldown = this.data.get().getInt("raid.cooldown");
        this.raidActive = this.data.get().getBoolean("raid.active-raid.active");
        this.lastActive = LocalDate.parse(this.data.get().getString("last-active"));
        this.upgrades = new HashMap<>();
        for (UpgradeType type : UpgradeType.values()) {
            upgrades.put(type, new Upgrade(type, this));
        }
        this.permsMenuGui = new PermissionGui(this);
    }

    public Faction(UUID id) {
        this.id = id;
        if (isWilderness()) {
            this.fWorld = new FWorld(Bukkit.createWorld(new WorldCreator(FileManager.get("config").getString("main-world-name"))), 0, 0);
        } else {
            this.fWorld = new FWorld(Bukkit.createWorld(new WorldCreator("plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + id.toString())), 128, 64);
        }
        this.data = new FactionDataFileUtil(String.valueOf(id));
        this.name = this.data.get().getString("name");
        loadPlayerMap();
        loadRolePermissionMap();
        this.home = fWorld.getBlockAt(data.get().getInt("home-location.x"), data.get().getInt("home-location.y"), data.get().getInt("home-location.z")).getLocation();
        this.relationManager = new RelationManager(this);
        this.founded = this.data.get().getString("founded");
        this.wealth = this.data.get().getDouble("wealth");
        this.xp = this.data.get().getInt("xp-bank");
        this.raidCooldown = this.data.get().getInt("raid.cooldown");
        this.raidActive = this.data.get().getBoolean("raid.active-raid.active");
        this.lastActive = LocalDate.parse(this.data.get().getString("last-active"));
        this.upgrades = new HashMap<>();
        for (UpgradeType type : UpgradeType.values()) {
            upgrades.put(type, new Upgrade(type, this));
        }
        this.permsMenuGui = new PermissionGui(this);
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
            this.data.get().set("faction-members." + uuid, Role.OWNER.toString());
            this.data.get().set("faction-members." + previousOwner, Role.CO_OWNER.toString());
            this.data.save();
            playerMap.remove(uuid);
            playerMap.remove(getOwner());
            playerMap.put(uuid, Role.OWNER);
            playerMap.put(previousOwner, Role.CO_OWNER);
            FPlayerManager.updateFPlayer(previousOwner);
        }
        if (role.equals(Role.MODERATOR)) {
            this.data.get().set("faction-members." + uuid, Role.CO_OWNER.toString());
            this.data.save();
            playerMap.remove(uuid);
            playerMap.put(uuid, Role.CO_OWNER);
            return true;
        }
        if (role.equals(Role.MEMBER)) {
            this.data.get().set("faction-members." + uuid, Role.MODERATOR.toString());
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
            this.data.get().set("faction-members." + uuid, Role.MODERATOR.toString());
            this.data.save();
            playerMap.remove(uuid);
            playerMap.put(uuid, Role.MODERATOR);
            return true;
        }
        if (role.equals(Role.MODERATOR)) {
            this.data.get().set("faction-members." + uuid, Role.MEMBER.toString());
            this.data.save();
            playerMap.remove(uuid);
            playerMap.put(uuid, Role.MEMBER);
            return true;
        }
        return false;
    }

    public void messageAllOnlinePlayers(MessageType type, String... replacements) {
        for (UUID uuid : getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            type.message(player, replacements);
        }
    }

    public void messageAllOnlinePlayers(String message) {
        for (UUID uuid : getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            player.sendMessage(message);
        }
    }

    public void loadRolePermissionMap() {
        if (rolePermissionMap == null) rolePermissionMap = new HashMap<>();
        List<String> member = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.member")) {
            member.add(node);
        }
        rolePermissionMap.put(Role.MEMBER, member);
        List<String> moderator = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.moderator")) {
            moderator.add(node);
        }
        rolePermissionMap.put(Role.MODERATOR, moderator);
        List<String> coOwner = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.co_owner")) {
            coOwner.add(node);
        }
        rolePermissionMap.put(Role.CO_OWNER, coOwner);
        List<String> owner = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.owner")) {
            owner.add(node);
        }
        rolePermissionMap.put(Role.OWNER, owner);
    }

    public void refreshRolePermissionMap() {
        this.rolePermissionMap = null;
        loadRolePermissionMap();
    }

    public boolean roleHasPermission(Role role, String node) {
        return rolePermissionMap.get(role).contains(node);
    }

    public List<String> getRolePermissions(Role role) {
        return rolePermissionMap.get(role);
    }

    public boolean hasRelation(Faction faction) {
        return relationManager.hasRelation(faction);
    }

    public RelationManager getRelationManager() {
        return relationManager;
    }

    public UUID getOwner() {
        for (Map.Entry player : playerMap.entrySet()) {
            if (player.getValue().equals(Role.OWNER)) return (UUID) player.getKey();
        }
        return null;
    }

    public OfflinePlayer getOwnerAsPlayer() {
        return Bukkit.getOfflinePlayer(getOwner());
    }

    public List<UUID> getOnlinePlayers() {
        List<UUID> onlinePlayers = new ArrayList<>();
        for (UUID uuid : getPlayers()) {
            if (Bukkit.getPlayer(uuid) != null) onlinePlayers.add(uuid);
        }
        return onlinePlayers;
    }

    public List<UUID> getOfflinePlayers() {
        List<UUID> offlinePlayers = new ArrayList<>();
        for (UUID uuid : getPlayers()) {
            if (Bukkit.getPlayer(uuid) == null) offlinePlayers.add(uuid);
        }
        return offlinePlayers;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.getData().get().set("name", name);
        this.getData().save();
        for (UUID uuid : getPlayers()) {
            FPlayerManager.updateFPlayer(uuid);
        }
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

    public String getFounded() {
        return founded;
    }

    public String getNumberOnline() {
        return String.valueOf(getOnlinePlayers().size());
    }

    public String getOnlinePlayersAsString() {
        List<UUID> players = getOnlinePlayers();
        String online = "";
        int count = 0;
        for (UUID uuid : players) {
            online += (FileManager.get("config").getString("who-cmd.online-format.name-color") + getRole(FPlayerManager.getFPlayer(uuid)).getPrefix() + Bukkit.getPlayer(uuid).getName());
            if (count != players.size() - 1) {
                online += FileManager.get("config").getString("who-cmd.online-format.separator");
            }
            count++;
        }
        return online;
    }

    public String getNumberOffline() {
        return String.valueOf(getOfflinePlayers().size());
    }

    public String getOfflinePlayersAsString() {
        List<UUID> players = getOfflinePlayers();
        String online = "";
        int count = 0;
        for (UUID uuid : players) {
            online += (FileManager.get("config").getString("who-cmd.offline-format.name-color") + getRole(FPlayerManager.getFPlayer(uuid)).getPrefix() + Bukkit.getOfflinePlayer(uuid).getName());
            if (count != players.size() - 1) {
                online += FileManager.get("config").getString("who-cmd.offline-format.separator");
            }
            count++;
        }
        return online;
    }

    public String getTotalPlayers() {
        return String.valueOf(getPlayers().size());
    }

    public double getWealth() {
        return this.wealth;
    }

    public void setWealth(double wealth) {
        this.data.get().set("wealth", wealth);
        this.data.save();
        this.wealth = wealth;
    }

    public int getXp() {
        return this.xp;
    }

    public void depositXp(int amount) {
        this.data.get().set("xp-bank", this.xp + amount);
        this.data.save();
        this.xp += amount;
    }

    public void withdrawXp(double amount) {
        this.data.get().set("xp-bank", this.xp - amount);
        this.data.save();
        this.xp -= amount;
    }

    public Upgrade getUpgrade(UpgradeType type) {
        return this.upgrades.get(type);
    }

    public void openUpgradeGui(FPlayer fPlayer) {
        if (upgradeGui == null) this.upgradeGui = new UpgradeGui(this);
        upgradeGui.setfPlayer(fPlayer);
        upgradeGui.open(fPlayer.getPlayer());
    }

    public UpgradeGui getUpgradeGui() {
        if (upgradeGui == null) this.upgradeGui = new UpgradeGui(this);
        return this.upgradeGui;
    }

    public void openPermissionGui(FPlayer fPlayer, Role role) {
        if (permissionsPageGuiMap == null) {
            this.permissionsPageGuiMap = new HashMap<>();
            permissionsPageGuiMap.put(role, new PermissionPageGui(this, role));
        } else if (permissionsPageGuiMap.get(role) == null) {
            permissionsPageGuiMap.put(role, new PermissionPageGui(this, role));
        }
        permissionsPageGuiMap.get(role).open(fPlayer.getPlayer());
    }

    public PermissionPageGui getPermissionGui(Role role) {
        if (permissionsPageGuiMap == null) {
            this.permissionsPageGuiMap = new HashMap<>();
            permissionsPageGuiMap.put(role, new PermissionPageGui(this, role));
        } else if (permissionsPageGuiMap.get(role) == null) {
            permissionsPageGuiMap.put(role, new PermissionPageGui(this, role));
        }
        return permissionsPageGuiMap.get(role);
    }

    public void openfChest(FPlayer fPlayer) {
        if (this.fChest == null) this.fChest = FChestManager.loadFChest(this);
        fChest.open(fPlayer);
    }

    public FChest getfChest() {
        if (this.fChest == null) this.fChest = FChestManager.loadFChest(this);
        return this.fChest;
    }

    public PermissionGui getPermsMenuGui() {
        return permsMenuGui;
    }

    public boolean isWilderness() {
        return FactionManager.factionIsWilderness(this);
    }

    public boolean isInactive() {
        return LocalDate.now().isAfter(this.lastActive.plus(1, ChronoUnit.WEEKS));
    }

    // raid shit
    public FRaid getfRaid() {
        return this.fRaid;
    }

    public boolean isRaidActive() {
        return this.raidActive;
    }

    public void setRaidActive(boolean raidActive) {
        this.raidActive = raidActive;
    }

    public boolean isRaiding() {
        if (this.fRaid == null) return false;
        if (!this.fRaid.isActive()) return false;
        return this.fRaid.getRaidingFaction().equals(this.id);
    }

    public boolean isBeingRaided() {
        if (this.fRaid == null) return false;
        if (!this.fRaid.isActive()) return false;
        return this.fRaid.getDefendingFaction().equals(this.id);
    }

    public boolean isOnRaidCooldown() {
        return this.raidCooldown > 0;
    }

    public int getRaidCooldown() {
        return this.raidCooldown;
    }

    public void setRaidCooldown(int remaining) {
        this.raidCooldown = remaining;
    }

    public void saveRaidCooldown() {
        this.data.get().set("raid.cooldown", this.raidCooldown);
        this.data.save();
    }

    public void setfRaid(FRaid fRaid) {
        this.fRaid = fRaid;
    }

    public String getRaidStatus() {
        if (!isOnRaidCooldown() && !isRaidActive()) {
            return FileManager.get("raid-config").getString("raid-status.raidable");
        } else if (!isOnRaidCooldown() && isRaidActive()) {
            TimeUtil timeUtil = new TimeUtil(this.getfRaid().getRemaining());
            return FileManager.get("raid-config").getString("raid-status.raid-active")
                    .replace("{days}", timeUtil.getDays())
                    .replace("{hours}", timeUtil.getHours())
                    .replace("{minutes}", timeUtil.getMinutes())
                    .replace("{seconds}", timeUtil.getSeconds());
        } else {
            TimeUtil timeUtil = new TimeUtil(this.raidCooldown);
            return FileManager.get("raid-config").getString("raid-status.on-cooldown")
                    .replace("{days}", timeUtil.getDays())
                    .replace("{hours}", timeUtil.getHours())
                    .replace("{minutes}", timeUtil.getMinutes())
                    .replace("{seconds}", timeUtil.getSeconds());
        }
    }
}