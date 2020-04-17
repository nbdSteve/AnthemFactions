package gg.steve.anthem.permission;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import sun.rmi.runtime.Log;

import java.util.ConcurrentModificationException;
import java.util.List;

public enum PermissionNode {
    // faction nodes
    CREATE("player.create"),
    DISBAND("player.disband"),
    HOME("player.home"),
    SET_HOME("player.set-home"),
    INVITE("player.invite"),
    ACCEPT("player.accept"),
    KICK("player.kick"),
    LEAVE("player.leave"),
    PROMOTE("player.promote"),
    DEMOTE("player.demote"),
    LIST("player.list"),
    WHO("player.who"),
    BUILD("player.build"),
    ALLY("player.ally"),
    UN_ALLY("player.un-ally"),
    ENEMY("player.enemy"),
    NEUTRAL("player.neutral"),
    CHAT_ALLY("player.chat-ally"),
    CHAT_FACTION("player.chat-faction"),
    CHAT_PUBLIC("player.chat-public"),
    TOP("player.top"),
    DEPOSIT("player.deposit"),
    WITHDRAW("player.withdraw"),
    XP_QUERY("player.xp-query"),
    UPGRADE("player.upgrade"),
    CHEST("player.chest"),
    FLY("player.fly"),
    TAG("player.tag"),
    RAID("player.raid"),
    RAID_JOIN("player.raid-join"),
    PERMS("player.perms"),

    // admin cmd nodes
    ID("admin.id-cmd"),
    RELOAD("admin.reload"),
    BYPASS("admin.bypass");

    private String path;

    PermissionNode(String path) {
        this.path = path;
    }

    public String get() {
        return FileManager.get("permissions").getString(this.path);
    }

    public boolean isEditable() {
        return !FileManager.get("fperms-config").getStringList("un-editable-nodes").contains(toString());
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(get());
    }

    public boolean isEnabled(Faction faction, Role role) {
        return faction.roleHasPermission(role, get());
    }

    public void onClick(Faction faction, Role role) {
        if (faction.roleHasPermission(role, get())) {
            faction.getRolePermissions(role).remove(get());
            faction.getData().get().set("permissions." + role.toString().toLowerCase(), faction.getRolePermissions(role));
        } else {
            faction.getRolePermissions(role).add(get());
            faction.getData().get().set("permissions." + role.toString().toLowerCase(), faction.getRolePermissions(role));
        }
        faction.getData().save();
        faction.refreshRolePermissionMap();
        try {
            for (HumanEntity player : faction.getPermissionGui(role).getInventory().getViewers()) {
                player.closeInventory();
                faction.getPermissionGui(role).refresh();
                faction.openPermissionGui(FPlayerManager.getFPlayer(player.getUniqueId()), role);
            }
        } catch (ConcurrentModificationException e) {
            // all good
        }
    }
}
