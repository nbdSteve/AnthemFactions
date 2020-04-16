package gg.steve.anthem.raid;

import gg.steve.anthem.cooldown.CooldownType;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.delay.DelayManager;
import gg.steve.anthem.delay.exception.DelayAlreadyActiveException;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.BroadcastType;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.raid.events.RaidCompletionEvent;
import gg.steve.anthem.raid.events.RaidStartEvent;
import gg.steve.anthem.upgrade.UpgradeType;
import gg.steve.anthem.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FRaidEventListener implements Listener {

    @EventHandler
    public void raidStart(RaidStartEvent event) {
        if (event.isCancelled()) return;
        // add the raid to the raid manager
        FRaidManager.addRaid(event.getRaid());
        event.getDefendingFaction().setfRaid(event.getRaid());
        event.getDefendingFaction().setRaidActive(true);
        event.getRaidingFaction().setfRaid(event.getRaid());
        event.getRaidingFaction().setRaidActive(true);
        // start the countdown
        event.getRaid().start();
        event.getRaid().saveToFile();
        // teleport all online players to the factions world
        if (event.getStarter() != null) {
            try {
                DelayManager.addDelay(event.getStarter().getUUID(), CooldownType.RAID_TELEPORT, event.getDefendingFaction().getFWorld().raiderTeleportLocation(), event.getStarter());
            } catch (DelayAlreadyActiveException e) {
                e.printStackTrace();
            }
        }
        // broadcast that the raid has started
        BroadcastType.RAID_START.broadcast(event.getDefendingFaction().getName(), event.getRaidingFaction().getName(), event.getTier().toString());
    }

    @EventHandler
    public void raidComplete(RaidCompletionEvent event) {
        if (event.isCancelled()) return;
        event.getRaid().cancelCounter();
        for (UUID uuid : event.getRaidingFaction().getOnlinePlayers()) {
            FPlayer fPlayer = FPlayerManager.getFPlayer(uuid);
            LogUtil.info("running 1");
            if (fPlayer.getFaction().getUpgrade(UpgradeType.RAIDING).getLevel() > 2) {
                LogUtil.info("running 2");
                fPlayer.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
            if (!fPlayer.inRaidWorld()) continue;
            fPlayer.getPlayer().teleport(Bukkit.getWorld(FileManager.get("config").getString("main-world-name")).getSpawnLocation());
        }
        event.getRaid().setActive(false);
        event.getDefendingFaction().setRaidActive(false);
        event.getRaidingFaction().setRaidActive(false);
        // remove the active raid
        FRaidManager.removeActiveRaid(event.getRaid());
        // teleport all of the raiders back to the spawn location
        // reset the raid data for both factions
        event.getRaid().resetRaidData();
        // set defending faction on raid cooldown
        event.getDefendingFaction().setRaidCooldown(event.getRaid().getTier().getCooldown());
        FRaidManager.addFactionOnRaidCooldown(event.getDefendingFaction().getId());
        // broadcast that the raid is complete
        BroadcastType.RAID_COMPLETE.broadcast(event.getDefendingFaction().getName(), event.getRaidingFaction().getName(), event.getTier().toString());
    }
}
