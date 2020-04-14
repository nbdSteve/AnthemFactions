package gg.steve.anthem.upgrade.fchest;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FChestManager {

    public static FChest loadFChest(Faction faction) {
        ConfigurationSection section = faction.getData().get().getConfigurationSection("f-chest");
        Map<Integer, ItemStack> items = new HashMap<>();
        for (String slot : section.getKeys(false)) {
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.loadFromString(section.getString(slot));
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
            items.put(Integer.parseInt(slot), config.getItemStack(slot));
        }
        return new FChest(faction, items);
    }

    public static void saveFChests() {
        for (Faction faction : FactionManager.getFactionsAsList()) {
            ConfigurationSection section = faction.getData().get().getConfigurationSection("f-chest");
            Inventory fChest = faction.getfChest().getInventory();
            for (int i = 0; i < fChest.getSize(); i++) {
                YamlConfiguration config = new YamlConfiguration();
                config.set(String.valueOf(i), fChest.getItem(i));
                section.set(String.valueOf(i), config.saveToString());
            }
            faction.getData().save();
        }
    }
}
