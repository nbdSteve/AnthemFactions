package gg.steve.anthem.upgrade.ffly;

import gg.steve.anthem.cooldown.Cooldown;
import gg.steve.anthem.cooldown.CooldownManager;
import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.cooldown.exception.CooldownActiveException;
import gg.steve.anthem.cooldown.exception.NotOnCooldownException;
import gg.steve.anthem.message.MessageType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.upgrade.UpgradeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class FFlyListener implements Listener {

    @EventHandler
    public void damageListener(EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        if (!(event.getEntity() instanceof Player)) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getEntity().getUniqueId());
        if (!CooldownManager.isOnCooldown(fPlayer.getUUID(), CooldownType.FLY_FALL_DAMAGE)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (event.getTo() == null) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getPlayer().getUniqueId());
        if (fPlayer.isBeingRaided()) return;
        if (!fPlayer.isInHomeWorld() || !fPlayer.getFWorld().inFactionLand(event.getTo())) {
            if (!fPlayer.isFlying()) return;
            try {
                CooldownManager.addCooldown(fPlayer.getUUID(), new Cooldown(CooldownType.FLY_FALL_DAMAGE));
            } catch (CooldownActiveException e) {
                e.printStackTrace();
            }
            fPlayer.setFlying(false);
            MessageType.FLY_DISABLED_LEAVE.message(fPlayer);
            return;
        }
        if (fPlayer.getFaction().getUpgrade(UpgradeType.WORLD).getLevel() < 1) return;
        if (fPlayer.isFlying()) return;
        if (fPlayer.isInHomeWorld() && fPlayer.getFWorld().inFactionLand(event.getTo())) {
            if (CooldownManager.isOnCooldown(fPlayer.getUUID(), CooldownType.FLY_FALL_DAMAGE)) {
                try {
                    CooldownManager.removeCooldown(fPlayer.getUUID(), CooldownType.FLY_FALL_DAMAGE);
                } catch (NotOnCooldownException e) {
                    e.printStackTrace();
                }
            }
            fPlayer.setFlying(true);
            MessageType.FLY_ENABLED.message(fPlayer);
        }
    }
}
