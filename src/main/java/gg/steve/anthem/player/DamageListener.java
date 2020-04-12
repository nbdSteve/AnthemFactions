package gg.steve.anthem.player;

import gg.steve.anthem.message.MessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void damageEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) return;
        FPlayer fPlayer = FPlayerManager.getFPlayer(event.getEntity().getUniqueId());
        FPlayer damager = FPlayerManager.getFPlayer(event.getDamager().getUniqueId());
        if (!fPlayer.hasFaction() || !damager.hasFaction()) return;
        if (fPlayer.getFaction().getId().equals(damager.getFaction().getId())) {
            event.setCancelled(true);
            MessageType.CAN_NOT_HURT_FACTION_MEMBER.message(damager, fPlayer.getName());
        }
        if (fPlayer.getFaction().getRelationManager().isAlly(damager.getFaction())) {
            MessageType.CAN_NOT_HURT_ALLY_FACTION_MEMBER.message(damager, fPlayer.getFaction().getName(), fPlayer.getName());
            event.setCancelled(true);
        }
    }
}