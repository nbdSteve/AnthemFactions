package gg.steve.anthem.upgrade;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;

public class UpgradeGui extends AbstractGui {
    private YamlConfiguration config = FileManager.get("upgrade-gui");
    private FPlayer fPlayer;
    private Faction faction;

    /**
     * Constructor the create a new Gui
     */
    public UpgradeGui(Faction faction) {
        super(FileManager.get("upgrade-gui"), InventoryType.valueOf(FileManager.get("upgrade-gui").getString("type")), FileManager.get("upgrade-gui").getInt("size"));
        this.faction = faction;
        for (int i = 1; i <= config.getInt("size"); i++) {
            int id = i;
            try {
                setItemInSlot(config.getInt(id + ".slot"),
                        GuiUtil.createItem(config, id, faction), player -> {
                            switch (config.getString(id + ".upgrade")) {
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
                                    fPlayer.getPlayer().closeInventory();
                                    break;
                            }
                        });
            } catch (NullPointerException e) {

            }
        }
    }

    public void setfPlayer(FPlayer fPlayer) {
        this.fPlayer = fPlayer;
    }

    public void refresh() {
        for (int i = 1; i <= config.getInt("size"); i++) {
            int id = i;
            try {
                setItemInSlot(config.getInt(id + ".slot"),
                        GuiUtil.createItem(config, id, faction), player -> {
                            switch (config.getString(id + ".upgrade")) {
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
                                    fPlayer.getPlayer().closeInventory();
                                    break;
                            }
                        });
            } catch (NullPointerException e) {

            }
        }
    }
}