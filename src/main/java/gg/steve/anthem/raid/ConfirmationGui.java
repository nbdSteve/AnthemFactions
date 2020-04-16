package gg.steve.anthem.raid;

import gg.steve.anthem.gui.AbstractGui;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfirmationGui extends AbstractGui {

    public ConfirmationGui(YamlConfiguration config, String type, Integer... size) {
        super(config, type, size);
    }
}
