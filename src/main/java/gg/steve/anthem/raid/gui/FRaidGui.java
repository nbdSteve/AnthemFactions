package gg.steve.anthem.raid.gui;

import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.raid.Tier;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.ConfigurationSection;

public class FRaidGui extends AbstractGui {
    ConfigurationSection section = FileManager.get("raid-gui").getConfigurationSection("gui");

    public FRaidGui() {
        super(FileManager.get("raid-gui"), FileManager.get("raid-gui").getString("type"), FileManager.get("raid-gui").getInt("size"));
        for (String entry : section.getKeys(false)) {
            setItemInSlot(section.getInt(entry + ".slot"),
                    GuiUtil.createBasicItem(section, entry), player -> {
                        switch (section.getString(entry + ".page")) {
                            case "TIER_1":
                                Tier.TIER_1.getTierGui().refresh();
                                Tier.TIER_1.getTierGui().open(player);
                                break;
                            case "TIER_2":
                                Tier.TIER_2.getTierGui().refresh();
                                Tier.TIER_2.getTierGui().open(player);
                                break;
                            case "TIER_3":
                                if (Tier.TIER_3.isTier3Factions()) {
                                    new ConfirmationGui(Tier.TIER_3.getRandomFaction(), Tier.TIER_3).open(player);
                                } else {
                                    CommandDebug.NO_TIER_3_FACTIONS.message(player);
                                    player.closeInventory();
                                }
                                break;
                            default:
                                player.closeInventory();
                        }
                    });
        }
    }
}
