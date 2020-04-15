package gg.steve.anthem.raid;

import gg.steve.anthem.gui.AbstractGui;
import gg.steve.anthem.managers.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;

public class TierGui extends AbstractGui {
    private YamlConfiguration config;
    private Tier tier;

    public TierGui(Tier tier) {
        super(FileManager.get(tier.toString()), FileManager.get(tier.toString()).getString("type"), FileManager.get(tier.toString()).getInt("size"));
        this.config = FileManager.get(tier.toString());
    }
}
