package gg.steve.anthem.upgrade;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;

public class UpgradeGui extends AbstractGui {
    private ConfigurationSection section = FileManager.get("upgrade-gui").getConfigurationSection("gui");
    private FPlayer fPlayer;
    private Faction faction;

    /**
     * Constructor the create a new Gui
     */
    public UpgradeGui(Faction faction) {
        super(FileManager.get("upgrade-gui"), FileManager.get("upgrade-gui").getString("type"), FileManager.get("upgrade-gui").getInt("size"));
        this.faction = faction;
        refresh();
    }

    public void setfPlayer(FPlayer fPlayer) {
        this.fPlayer = fPlayer;
    }

    public void refresh() {
        for (String entry : section.getKeys(false)) {
            setItemInSlot(section.getInt(entry + ".slot"),
                    GuiUtil.createUpgradeItem(section, entry, faction), player -> {
                        switch (section.getString(entry + ".upgrade")) {
                            case "WORLD":
                                UpgradeType.WORLD.onClick(faction.getUpgrade(UpgradeType.WORLD), fPlayer);
                                break;
                            case "FARMING":
                                UpgradeType.FARMING.onClick(faction.getUpgrade(UpgradeType.FARMING), fPlayer);
                                break;
                            case "RAIDING":
                                UpgradeType.RAIDING.onClick(faction.getUpgrade(UpgradeType.RAIDING), fPlayer);
                                break;
                            default:
                                player.closeInventory();
                                break;
                        }
                    });
        }
    }
}