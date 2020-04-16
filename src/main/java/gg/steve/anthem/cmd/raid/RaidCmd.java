package gg.steve.anthem.cmd.raid;

import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.delay.exception.DelayAlreadyActiveException;
import gg.steve.anthem.delay.exception.NotOnDelayException;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.raid.FRaidManager;
import gg.steve.anthem.upgrade.UpgradeType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaidCmd {

    public static void raid(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_CAN_RUN_COMMAND.message(sender);
            return;
        }
        FPlayer fPlayer = FPlayerManager.getFPlayer(((Player) sender).getUniqueId());
        if (!fPlayer.hasFaction()) {
            CommandDebug.PLAYER_NOT_FACTION_MEMBER.message(fPlayer);
            return;
        }
        if (fPlayer.getFaction().getUpgrade(UpgradeType.RAIDING).getLevel() < 1) {
            MessageType.INSUFFICIENT_UPGRADE_LEVEL.message(fPlayer, UpgradeType.RAIDING.toString(), "1");
            return;
        }
        if (args.length == 2 && (args[1].equalsIgnoreCase("j") || args[1].equalsIgnoreCase("join"))) {
            raidJoin(fPlayer);
        } else {
            raidGui(fPlayer);
        }
    }

    public static void raidGui(FPlayer fPlayer) {
        if (!fPlayer.hasFactionPermission(PermissionNode.RAID)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.RAID.get());
            return;
        }
        FRaidManager.getfRaidGui().open(fPlayer.getPlayer());
    }

    public static void raidJoin(FPlayer fPlayer) {
        if (!fPlayer.hasFactionPermission(PermissionNode.RAID_JOIN)) {
            MessageType.INSUFFICIENT_ROLE_PERMISSION.message(fPlayer, PermissionNode.RAID_JOIN.get());
            return;
        }
        if (!fPlayer.isRaiding()) {
            CommandDebug.FACTION_NOT_RAIDING.message(fPlayer);
            return;
        }
        if (fPlayer.isRaider()) {
            CommandDebug.ALREADY_TELEPORTED_TO_RAID.message(fPlayer);
            return;
        }
        if (DelayManager.onDelay(fPlayer.getUUID(), CooldownType.RAID_TELEPORT)) {
            try {
                DelayManager.getDelay(fPlayer.getUUID(), CooldownType.RAID_TELEPORT).sendActionBar(fPlayer.getUUID());
            } catch (NotOnDelayException e) {
                e.printStackTrace();
            }
        }
        try {
            DelayManager.addDelay(fPlayer.getUUID(), CooldownType.RAID_TELEPORT, FactionManager.getFaction(fPlayer.getFaction().getfRaid().getDefendingFaction()).getFWorld().raiderTeleportLocation(), fPlayer);
        } catch (DelayAlreadyActiveException e) {
            e.printStackTrace();
        }
    }
}
