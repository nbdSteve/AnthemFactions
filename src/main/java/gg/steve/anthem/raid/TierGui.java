package gg.steve.anthem.raid;

import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.utils.GuiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

public class TierGui extends AbstractGui {
    private YamlConfiguration config;
    private Tier tier;

    public TierGui(Tier tier) {
        super(FileManager.get(tier.toString()), FileManager.get(tier.toString()).getString("type"), FileManager.get(tier.toString()).getInt("size"));
        this.config = FileManager.get(tier.toString());
        this.tier = tier;
        for (int i = 1; i <= config.getInt("size"); i++) {
            int id = i;
            try {
                setItemInSlot(config.getInt(id + ".slot"),
                        GuiUtil.createFactionItem(config, id), player -> {
                            FPlayer fPlayer = FPlayerManager.getFPlayer(player.getUniqueId());
                            if (config.getBoolean(id + ".faction-item")) {

                            } else {
                                player.closeInventory();
                            }
                        });
            } catch (NullPointerException e) {

            }
        }
    }
}
