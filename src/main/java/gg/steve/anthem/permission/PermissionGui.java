package gg.steve.anthem.permission;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.message.CommandDebug;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class PermissionGui extends AbstractGui {
    private YamlConfiguration config = FileManager.get("fperms-gui");

    /**
     * Constructor the create a new Gui
     */
    public PermissionGui(Faction faction) {
        super(FileManager.get("fperms-gui"), FileManager.get("fperms-gui").getString("type"), FileManager.get("fperms-gui").getInt("size"));

        for (int i = 1; i <= config.getInt("size"); i++) {
            int id = i;
            try {
                setItemInSlot(config.getInt(id + ".slot"),
                        GuiUtil.createPermissionItem(config, id), player -> {
                            FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
                            switch (config.getString(id + ".page")) {
                                case "CO_OWNER":
                                    if (Role.higherRole(Role.CO_OWNER, fPlayer.getRole())) {
                                        CommandDebug.PERMISSION_SAME_OR_HIGHER_RANK.message(fPlayer);
                                        fPlayer.getPlayer().closeInventory();
                                        return;
                                    }
                                    faction.getPermissionGui(Role.CO_OWNER).open(player);
                                    break;
                                case "MODERATOR":
                                    if (Role.higherRole(Role.MODERATOR, fPlayer.getRole())) {
                                        CommandDebug.PERMISSION_SAME_OR_HIGHER_RANK.message(fPlayer);
                                        fPlayer.getPlayer().closeInventory();
                                        return;
                                    }
                                    faction.getPermissionGui(Role.MODERATOR).open(player);
                                    break;
                                case "MEMBER":
                                    if (Role.higherRole(Role.MEMBER, fPlayer.getRole())) {
                                        CommandDebug.PERMISSION_SAME_OR_HIGHER_RANK.message(fPlayer);
                                        fPlayer.getPlayer().closeInventory();
                                        return;
                                    }
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
}
