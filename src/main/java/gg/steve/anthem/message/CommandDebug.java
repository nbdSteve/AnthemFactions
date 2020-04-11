package gg.steve.anthem.message;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum CommandDebug {
    // console debug
    ONLY_PLAYERS_CAN_INVITE("debug", "only-players-can-invite"),
    ONLY_PLAYERS_CAN_CHAT("debug", "only-players-can-chat"),
    ONLY_PLAYERS_CAN_CREATE("debug", "only-players-can-create"),
    ONLY_PLAYERS_CAN_DEMOTE("debug", "only-players-can-demote"),
    // debug messages,
    INCORRECT_ARGUMENTS("debug", "incorrect-arguments"),
    FACTION_DOES_NOT_EXIST("debug", "faction-does-not-exist"),
    CAN_NOT_ACCEPT_INVITE("debug", "can-not-accept-invite"),
    NO_INVITE_PENDING("debug", "no-invite-pending"),
    INVALID_CHAT_CHANNEL("debug", "invalid-chat-channel"),
    CHAT_NO_FACTION("debug", "chat-no-faction"),
    CREATE_ALREADY_IN_FACTION("debug", "create-already-in-faction"),
    FACTION_ALREADY_EXISTS("debug", "faction-already-exists"),
    MINIMUM_TAG_LENGTH("debug", "minimum-tag-length", "{length}"),
    MAXIMUM_TAG_LENGTH("debug", "maximum-tag-length", "{length}"),
    PLAYER_NOT_FACTION_MEMBER("debug", "player-not-faction-member"),
    TARGET_NOT_ONLINE("debug", "target-not-online"),
    TARGET_CAN_NOT_BE_SELF("debug", "target-can-not-be-self"),
    DEMOTED_NOT_FACTION_MEMBER("debug", "demoted-not-faction-member"),
    DEMOTED_SAME_OR_HIGHER_RANK("debug", "demoted-same-or-higher-rank"),
    DEMOTED_ALREADY_MEMBER("debug", "demoted-already-member"),
    ;

    private final String directory;
    private final String path;
    private List<String> placeholders;

    CommandDebug(String directory, String path, String... placeholders) {
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
