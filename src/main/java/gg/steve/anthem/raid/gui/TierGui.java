package gg.steve.anthem.raid.gui;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.raid.Tier;
import gg.steve.anthem.utils.GuiUtil;
import gg.steve.anthem.wealth.AsyncWealthCalculation;
import org.bukkit.configuration.ConfigurationSection;

public class TierGui extends AbstractGui {
    private ConfigurationSection section;
    private Tier tier;

    public TierGui(Tier tier) {
        super(FileManager.get(tier.toString()), FileManager.get(tier.toString()).getString("type"), FileManager.get(tier.toString()).getInt("size"));
        this.section = FileManager.get(tier.toString()).getConfigurationSection("gui");
        this.tier = tier;
        refresh();
    }

    public void refresh() {
        for (String entry : section.getKeys(false)) {
            setItemInSlot(section.getInt(entry + ".slot"),
                    GuiUtil.createFactionItem(section, entry, null, this.tier), player -> {
                        if (section.getBoolean(entry + ".faction-item")) {
                            Faction faction = AsyncWealthCalculation.getFactionsInWealthOrder().get(section.getInt(entry + ".faction"));
                            FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
                            player.closeInventory();
                            if (fPlayer.getFaction().getId().equals(faction.getId())) {
                                CommandDebug.ATTEMPT_RAID_OWN.message(fPlayer);
                            } else if (fPlayer.getFaction().getRelationManager().isAlly(faction)) {
                                CommandDebug.ATTEMPT_RAID_ALLY.message(fPlayer);
                            } else if (faction.isOnRaidCooldown()) {
                                CommandDebug.ATTEMPT_RAID_ON_COOLDOWN.message(fPlayer);
                            } else if (faction.isRaidActive()) {
                                CommandDebug.ATTEMPT_RAID_ON_RAIDING.message(fPlayer);
                            } else {
                                new ConfirmationGui(faction, tier).open(player);
                            }
                        } else {
                            player.closeInventory();
                        }
                    });
        }
    }
}
