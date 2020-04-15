package gg.steve.anthem.raid;

import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class RaidGui extends AbstractGui {
    private YamlConfiguration config = FileManager.get("raid-gui");

    public RaidGui() {
        super(FileManager.get("raid-gui"), FileManager.get("raid-gui").getString("type"), FileManager.get("raid-gui").getInt("size"));
        for (int i = 1; i <= config.getInt("size"); i++) {
            int id = i;
            try {
                setItemInSlot(config.getInt(id + ".slot"),
                        GuiUtil.createBasicItem(config, id), player -> {
                            FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
                            switch (config.getString(id + ".page")) {
                                case "TIER_1":
                                    break;
                                case "TIER_2":
                                    break;
                                case "TIER_3":
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
