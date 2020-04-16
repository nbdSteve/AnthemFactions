package gg.steve.anthem.permission;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class PermissionPageGui extends AbstractGui {
    private YamlConfiguration config = FileManager.get("fperms-page-gui");
    private Faction faction;
    private Role role;

    /**
     * Constructor the create a new Gui
     */
    public PermissionPageGui(Faction faction, Role role) {
        super(FileManager.get("fperms-page-gui"), FileManager.get("fperms-page-gui").getString("type"), role, FileManager.get("fperms-page-gui").getInt("size"));
        this.faction = faction;
        this.role = role;
        refresh();
    }

    public void refresh() {
        int slot = 0;
        for (String node : FileManager.get("fperms-config").getStringList("nodes-in-gui")) {
            setItemInSlot(slot, GuiUtil.createNodeItem(config, PermissionNode.valueOf(node), faction, role), player -> {
                PermissionNode.valueOf(node).onClick(faction, role);
            });
            slot++;
        }
    }
}
