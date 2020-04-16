package gg.steve.anthem.raid.gui;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.raid.FRaid;
import gg.steve.anthem.raid.Tier;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.ConfigurationSection;

public class ConfirmationGui extends AbstractGui {

    public ConfirmationGui(Faction faction, Tier tier) {
        super(FileManager.get("confirmation-gui"), FileManager.get("confirmation-gui").getString("type"), faction, FileManager.get("confirmation-gui").getInt("size"));
        ConfigurationSection section = FileManager.get("confirmation-gui").getConfigurationSection("gui");
        for (String entry : section.getKeys(false)) {
            setItemInSlot(section.getInt(entry + ".slot"),
                    GuiUtil.createFactionItem(section, entry, faction, tier), player -> {
                        FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
                        if (section.getString(entry + ".action").equalsIgnoreCase("accept")) {
                            if (fPlayer.getFaction().getXp() < tier.getCost()) {
                                CommandDebug.INSUFFICIENT_XP.message(fPlayer);
                            } else {
                                fPlayer.getFaction().withdrawXp(tier.getCost());
                                new FRaid(faction, fPlayer.getFaction(), tier, FPlayerManager.getFPlayer(player.getUniqueId()));
                            }
                            player.closeInventory();
                        } else {
                            player.closeInventory();
                        }
                    });
        }
    }
}
