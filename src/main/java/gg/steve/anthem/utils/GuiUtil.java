package gg.steve.anthem.utils;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.upgrade.UpgradeType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.inventory.ItemStack;

public class GuiUtil {

    public static ItemStack createUpgradeItem(YamlConfiguration config, int id, Faction faction) {
        ItemBuilderUtil builder = new ItemBuilderUtil(config.getString(id + ".item"), config.getString(id + ".data"));
        builder.addName(config.getString(id + ".name"));
        if (config.getString(id + ".upgrade").equalsIgnoreCase("none")) {
            builder.addLore(config.getStringList(id + ".lore"));
            builder.addEnchantments(config.getStringList(id + ".enchantments"));
            builder.addItemFlags(config.getStringList(id + ".item-flags"));
            return builder.getItem();
        }
        UpgradeType type = UpgradeType.valueOf(config.getString(id + ".upgrade"));
        builder.setPlaceholders("{current-level}", "{max-level}", "{next-level}", "{cost}", "{xp-to-level}", "{next-level-desc}");
        String upgradeCost;
        String nextLevel;
        String xpToLevel;
        String nextLevelDesc;
        if (faction.getUpgrade(type).alreadyMaxLevel()) {
            upgradeCost = "0";
            nextLevel = String.valueOf(type.getMaxLevel());
            xpToLevel = "0";
            nextLevelDesc = FileManager.get("upgrade-config").getString(type.toString().toLowerCase() + ".max-level-desc");
        } else {
            upgradeCost = String.valueOf(faction.getUpgrade(type).getNextLevelCost());
            xpToLevel = String.valueOf(faction.getUpgrade(type).getNextLevelCost() - faction.getXp());
            nextLevel = String.valueOf(faction.getUpgrade(type).getLevel() + 1);
            nextLevelDesc = FileManager.get("upgrade-config").getString(type.toString().toLowerCase() + ".level-" + (faction.getUpgrade(type).getLevel() + 1) + ".desc");
        }
        builder.addLore(config.getStringList(id + ".lore"),
                String.valueOf(faction.getUpgrade(type).getLevel()),
                String.valueOf(type.getMaxLevel()),
                nextLevel,
                upgradeCost,
                xpToLevel,
                nextLevelDesc);
        builder.addEnchantments(config.getStringList(id + ".enchantments"));
        builder.addItemFlags(config.getStringList(id + ".item-flags"));
        return builder.getItem();
    }

    public static ItemStack createRaidItem() {
        return null;
    }

    public static ItemStack createBasicItem(YamlConfiguration config, int id) {
        ItemBuilderUtil builder = new ItemBuilderUtil(config.getString(id + ".item"), config.getString(id + ".data"));
        builder.addName(config.getString(id + ".name"));
        builder.addLore(config.getStringList(id + ".lore"));
        builder.addEnchantments(config.getStringList(id + ".enchantments"));
        builder.addItemFlags(config.getStringList(id + ".item-flags"));
        return builder.getItem();
    }

    public static ItemStack createNodeItem(YamlConfiguration config, PermissionNode node, Faction faction, Role role) {
        ItemBuilderUtil builder;
        if (node.isEditable()) {
            if (node.isEnabled(faction, role)) {
                builder = new ItemBuilderUtil(config.getString("node-enabled.item"), config.getString("node-enabled.data"));
                builder.setPlaceholders("{page}");
                builder.addLore(config.getStringList("node-enabled.lore"), role.toString());
            } else {
                builder = new ItemBuilderUtil(config.getString("node-disabled.item"), config.getString("node-disabled.data"));
                builder.setPlaceholders("{page}");
                builder.addLore(config.getStringList("node-disabled.lore"), role.toString());
            }
        } else {
            builder = new ItemBuilderUtil(config.getString("node-un-editable.item"), config.getString("node-un-editable.data"));
            builder.setPlaceholders("{page}");
            builder.addLore(config.getStringList("node-un-editable.lore"), role.toString());
        }
        builder.addName(config.getString("node-config.name").replace("{node-type}", node.toString()));
        builder.addEnchantments(config.getStringList("node-config.enchantments"));
        builder.addItemFlags(config.getStringList("node-config.item-flags"));
        return builder.getItem();
    }
}
