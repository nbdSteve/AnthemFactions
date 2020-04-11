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

    // faction
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
    CHANGE_CHANNEL("faction", "change-channel", "{channel}");

    // raid

    // faction perms

    // upgrades

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
            receiver.getPlayer().sendMessage(ColorUtil.colorize(line));
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
