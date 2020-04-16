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
    ONLY_PLAYERS_CAN_RUN_COMMAND("only-players-can-invite"),
    // debug messages,
    INCORRECT_ARGUMENTS("incorrect-arguments"),
    FACTION_DOES_NOT_EXIST("faction-does-not-exist"),
    CAN_NOT_ACCEPT_INVITE("can-not-accept-invite"),
    NO_INVITE_PENDING("no-invite-pending"),
    INVALID_CHAT_CHANNEL("invalid-chat-channel"),
    CHAT_NO_FACTION("chat-no-faction"),
    CREATE_ALREADY_IN_FACTION("create-already-in-faction"),
    FACTION_ALREADY_EXISTS("faction-already-exists"),
    MINIMUM_TAG_LENGTH("minimum-tag-length", "{length}"),
    MAXIMUM_TAG_LENGTH("maximum-tag-length", "{length}"),
    PLAYER_NOT_FACTION_MEMBER("player-not-faction-member"),
    TARGET_NOT_ONLINE("target-not-online"),
    TARGET_CAN_NOT_BE_SELF("target-can-not-be-self"),
    TARGET_NOT_FACTION_MEMBER("target-not-faction-member"),
    DEMOTED_SAME_OR_HIGHER_RANK("demoted-same-or-higher-rank"),
    DEMOTED_ALREADY_MEMBER("demoted-already-member"),
    TARGET_ALREADY_MEMBER("target-already-member"),
    INVITE_ALREADY_PENDING("invite-already-pending"),
    KICK_SAME_OR_HIGHER_RANK("kick-same-or-higher-rank"),
    PROMOTE_SAME_OR_HIGHER_RANK("promote-same-or-higher-rank"),
    OWNER_LEAVE("owner-leave"),
    RELATION_TARGET_CAN_NOT_BE_SELF("relation-target-can-not-be-self"),
    RELATION_TARGET_WILDERNESS("relation-target-wilderness"),
    RELATION_ALREADY_SET("relation-already-set"),
    PENDING_ALLY_REQUEST("pending-ally-request"),
    UN_ALLY_REQUIRED("un-ally-required"),
    NOT_ALLIES("not-allies"),
    SET_HOME_NOT_IN_TERRITORY("set-home-not-in-territory"),
    INSUFFICIENT_XP("insufficient-xp"),
    PERMISSION_SAME_OR_HIGHER_RANK("permission-same-or-higher-rank"),
    ALREADY_FLYING("already-flying"),
    // raid debug
    FACTION_NOT_RAIDING("faction-not-raiding"),
    ALREADY_TELEPORTED_TO_RAID("already-teleported-to-raid"),
    DISBAND_DURING_RAID("disband-during-raid"),
    ATTEMPT_RAID_OWN("attempt-raid-own"),
    ATTEMPT_RAID_ALLY("attempt-raid-ally"),
    ATTEMPT_RAID_ON_COOLDOWN("attempt-raid-on-cooldown"),
    ATTEMPT_RAID_ON_RAIDING("attempt-raid-on-raiding"),
    NO_TIER_3_FACTIONS("no-tier-3-factions");

    private final String path;
    private List<String> placeholders;

    CommandDebug(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : FileManager.get("debug").getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(FPlayer receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : FileManager.get("debug").getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.getPlayer().sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : FileManager.get("debug").getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }
}
