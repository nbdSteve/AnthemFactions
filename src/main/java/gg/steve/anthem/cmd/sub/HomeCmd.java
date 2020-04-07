package gg.steve.anthem.cmd.sub;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.MessageUtil;

public class HomeCmd {

    public static void teleportHome(FPlayer fPlayer) {
        if (fPlayer.getFaction().equals(FactionManager.getWilderness())) {
            MessageUtil.commandDebug(fPlayer, "You are not a member of any faction");
            return;
        }
        fPlayer.teleportHome();
        MessageUtil.message("lang", "home-teleport", fPlayer.getPlayer());
    }

    public static void setHome(FPlayer fPlayer) {
        if (FactionManager.getFaction(fPlayer).equals(FactionManager.getWilderness())) {
            MessageUtil.commandDebug(fPlayer, "You are not a member of any faction");
            return;
        }
        if (fPlayer.getRole().equals(Role.MEMBER)) {
            MessageUtil.commandDebug(fPlayer, "You do not have permission to set the faction home");
            return;
        }
        fPlayer.getFaction().setHome(fPlayer.getPlayer().getLocation());
        MessageUtil.message("lang", "set-home", fPlayer.getPlayer());
    }
}
