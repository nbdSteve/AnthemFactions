package gg.steve.anthem.message;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum MessageType {
    // misc
    PERMISSION_DEBUG("misc", "permission-debug", "{node}"),
    RELOAD("misc", "reload"),
    HELP("misc", "help"),
    TELEPORT("misc", "teleport", "{command-type}"),
    WHO("misc", "faction-who", "{faction}", "{founded}", "{allies-number}", "{max-allies}",
            "{allies}", "{online-number}", "{online}", "{offline-number}", "{offline}", "{total-members}",
            "{relation-color}", "{relation}", "{worth}", "{xp}", "{raiding-level}", "{raiding-max-level}",
            "{farming-level}", "{farming-max-level}", "{world-level}", "{world-max-level}", "{time}"),
    TOP_HEADER("misc", "faction-top.header", "{page}"),
    TOP_ENTRY("misc", "faction-top.entry","{faction}", "{relation-color}", "{rank}", "{wealth}"),
    TOP_FOOTER("misc", "faction-top.footer"),
    LIST_HEADER("misc", "faction-list.header", "{page}"),
    LIST_ENTRY("misc", "faction-list.entry","{faction}", "{relation-color}", "{relation}", "{online-number}", "{total-members}", "{owner}"),
    LIST_FOOTER("misc", "faction-list.footer"),

    // relational
    NEUTRAL_ALERT_SENDER("relational", "neutral-alert-sender", "{faction}"),
    NEUTRAL_ALERT_RECEIVER("relational", "neutral-alert-receiver", "{faction}", "{player}"),
    ALLY_REQUEST_SEND("relational", "ally-request-send", "{faction}", "{player}"),
    ALLY_REQUEST_RECEIVE("relational", "ally-request-receive", "{faction}", "{player}"),
    ALLY_ALERT("relational", "ally-alert", "{faction}"),
    ALLY_DECLINED("relational", "ally-declined", "{faction}"),
    UN_ALLY_CONFIRMATION("relational", "un-ally-confirmation", "{faction}"),
    UN_ALLY_ALERT_SENDER("relational", "un-ally-alert-sender", "{faction}"),
    UN_ALLY_ALERT_RECEIVER("relational", "un-ally-alert-receiver", "{faction}", "{player}"),
    ENEMY_ALERT_SENDER("relational", "enemy-alert-sender", "{faction}"),
    ENEMY_ALERT_RECEIVER("relational", "enemy-alert-receiver", "{faction}", "{player}"),

    // faction misc
    TAG_CHANGE("faction", "tag-change", "{new-name}", "{player}"),
    LEAVE("faction", "leave", "{faction}"),
    LEAVE_ALERT("faction", "leave-alert", "{player}"),
    PROMOTION("faction", "promotion", "{player}", "{promoted}", "{role}"),
    DEMOTION("faction", "demotion", "{player}", "{demoted}", "{role}"),
    SET_HOME("faction", "set-home"),
    INSUFFICIENT_ROLE_PERMISSION("faction", "insufficient-role-permission", "{node}"),
    INVITE_RECEIVER("faction", "invite-receiver", "{faction}", "{player}"),
    INVITE_SENDER("faction", "invite-inviter", "{player}", "{invited}"),
    MEMBER_JOIN_ALERT("faction", "member-join-alert", "{player}"),
    KICK_RECEIVER("faction", "kick-receiver", "{faction}", "{player}"),
    KICK_KICKER("faction", "kick-kicker", "{player}", "{kicked}"),
    JOIN("faction", "join", "{faction}"),
    CREATE("faction", "create", "{faction}"),
    DISBAND("faction", "disband", "{faction}"),
    DISBAND_CONFIRMATION("faction", "disband-confirmation"),
    BUILD_BLOCKED_FACTION("faction", "build-blocked-faction", "{faction}"),
    BUILD_BLOCKED_RAID("faction", "build-blocked-raid"),
    COMMAND_BLOCKED_FACTION_TERRITORY("faction", "command-blocked-faction-territory", "{faction}"),
    COMMAND_BLOCKED_RAID("faction", "command-blocked-raid"),
    CHANGE_CHANNEL("faction", "change-channel", "{channel}"),
    FACTION_ID("faction", "faction-id", "{uuid}"),
    CAN_NOT_HURT_FACTION_MEMBER("faction", "can-not-hurt-faction-member", "{player}"),
    CAN_NOT_HURT_ALLY_FACTION_MEMBER("faction", "can-not-hurt-ally-faction-member", "{faction}", "{player}"),

    // raid
    JOINED_RAID("faction", "joined-raid", "{defender}", "{raider}", "{tier}"),
    RAID_TIME_UPDATE("faction", "raid-time-update", "{days}", "{hours}", "{minutes}", "{seconds}"),
    BREAK_SPAWNER_DURING_RAID("faction", "break-spawner-during-raid"),

    // faction perms

    // upgrades
    XP_DEPOSIT_SUCCESSFUL("faction", "xp-deposit-successful", "{amount}"),
    XP_WITHDRAWAL_SUCCESSFUL("faction", "xp-withdrawal-successful", "{amount}"),
    XP_QUERY("faction", "xp-query", "{xp}"),
    UPGRADE_ALREADY_MAX("faction", "upgrade-already-max"),
    UPGRADE_SUCCESS("faction", "upgrade-success", "{type}", "{level}", "{max-level}", "{player}"),
    INSUFFICIENT_UPGRADE_LEVEL("faction", "insufficient-upgrade-level", "{upgrade}", "{level-required}"),
    FLY_ENABLED("faction", "fly-enabled"),
    FLY_DISABLED_LEAVE("faction", "fly-disabled-leave"),
    FLY_DISABLED_RAID("faction", "fly-disabled-raid"),
    FLY_DISABLED_REGION("faction", "fly-disabled-region");

    private final String directory;
    private final String path;
    private List<String> placeholders;

    MessageType(String directory, String path, String... placeholders) {
        this.directory = directory;
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : FileManager.get(this.directory).getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(FPlayer receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : FileManager.get(this.directory).getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.message(ColorUtil.colorize(line));
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : FileManager.get(this.directory).getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }
}
