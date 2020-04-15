package gg.steve.anthem.permission;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;

public class PermissionGui extends AbstractGui {
    private YamlConfiguration config = FileManager.get("fperms-gui");
    private Faction faction;

    /**
     * Constructor the create a new Gui
     */
    public PermissionGui() {
        super(FileManager.get("fperms-gui"), FileManager.get("fperms-gui").getString("type"), FileManager.get("fperms-gui").getInt("size"));

        for (int i = 1; i <= config.getInt("size"); i++) {
            int id = i;
            try {
                setItemInSlot(config.getInt(id + ".slot"),
                        GuiUtil.createPermissionItem(config, id), player -> {
                            switch (config.getString(id + ".page")) {
                                case "CO_OWNER":
                                    faction.getPermissionGui(Role.CO_OWNER).open(player);
                                    break;
                                case "MODERATOR":
                                    faction.getPermissionGui(Role.MODERATOR).open(player);
                                    break;
                                case "MEMBER":
                                    faction.getPermissionGui(Role.MEMBER).open(player);
                                    break;
                                default:
                                    player.closeInventory();
                            }
                        });
            } catch (NullPointerException e) {

            }
        }
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
