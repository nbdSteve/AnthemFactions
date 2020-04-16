package gg.steve.anthem.utils;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.permission.PermissionNode;
import gg.steve.anthem.raid.Tier;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.role.Role;
import gg.steve.anthem.upgrade.UpgradeType;
import gg.steve.anthem.wealth.AsyncWealthCalculation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GuiUtil {

    public static ItemStack createUpgradeItem(ConfigurationSection section, String entry, Faction faction) {
        if (section.getString(entry + ".upgrade").equalsIgnoreCase("none")) {
            return createBasicItem(section, entry);
        }
        ItemBuilderUtil builder = new ItemBuilderUtil(section.getString(entry + ".item"), section.getString(entry + ".data"));
        builder.addName(section.getString(entry + ".name"));
        UpgradeType type = UpgradeType.valueOf(section.getString(entry + ".upgrade"));
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
        builder.addLore(section.getStringList(entry + ".lore"),
                String.valueOf(faction.getUpgrade(type).getLevel()),
                String.valueOf(type.getMaxLevel()),
                nextLevel,
                upgradeCost,
                xpToLevel,
                nextLevelDesc);
        builder.addEnchantments(section.getStringList(entry + ".enchantments"));
        builder.addItemFlags(section.getStringList(entry + ".item-flags"));
        return builder.getItem();
    }

    public static ItemStack createFactionItem(ConfigurationSection section, String entry, Faction faction, Tier tier) {
        if (section.getInt(entry + ".faction") == -1) {
            return createBasicItem(section, entry);
        }
        if (faction == null) {
            try {
                faction = AsyncWealthCalculation.getFactionsInWealthOrder().get(section.getInt(entry + ".faction"));
            } catch (Exception e) {
                return new ItemStack(Material.AIR);
            }
        }
        YamlConfiguration itemConfig = FileManager.get("raid-config");
        ItemBuilderUtil builder = new ItemBuilderUtil(itemConfig.getString("faction-item.item"), itemConfig.getString("faction-item.data"));
        if (builder.getMaterial().toString().equalsIgnoreCase("SKULL_ITEM")) {
            SkullMeta meta = (SkullMeta) builder.getItemMeta();
            meta.setOwner(Bukkit.getOfflinePlayer(faction.getOwner()).getName());
            builder.setItemMeta(meta);
        }
        builder.addName(itemConfig.getString("faction-item.name").replace("{faction}", faction.getName()));
        builder.setPlaceholders("{status}", "{online-number}", "{total-members}", "{raiding-level}", "{raiding-max-level}", "{farming-level}", "{farming-max-level}", "{world-level}", "{world-max-level}", "{worth}", "{allies-number}", "{max-allies}", "{cost}");
        builder.addLore(itemConfig.getStringList("faction-item.lore"),
                faction.getRaidStatus(),
                faction.getNumberOnline(),
                faction.getTotalPlayers(),
                String.valueOf(faction.getUpgrade(UpgradeType.RAIDING).getLevel()),
                String.valueOf(UpgradeType.RAIDING.getMaxLevel()),
                String.valueOf(faction.getUpgrade(UpgradeType.FARMING).getLevel()),
                String.valueOf(UpgradeType.FARMING.getMaxLevel()),
                String.valueOf(faction.getUpgrade(UpgradeType.WORLD).getLevel()),
                String.valueOf(UpgradeType.WORLD.getMaxLevel()),
                AnthemFactions.getNumberFormat().format(faction.getWealth()),
                String.valueOf(faction.getRelationManager().getRelationCount(RelationType.ALLY)),
                String.valueOf(RelationType.ALLY.getMaxAmount()),
                String.valueOf(tier.getCost()));
        builder.addEnchantments(itemConfig.getStringList("faction-item.enchantments"));
        builder.addEnchantments(itemConfig.getStringList("faction-item.item-flags"));
        return builder.getItem();
    }

    public static ItemStack createBasicItem(ConfigurationSection section, String entry) {
        ItemBuilderUtil builder = new ItemBuilderUtil(section.getString(entry + ".item"), section.getString(entry + ".data"));
        builder.addName(section.getString(entry + ".name"));
        builder.addLore(section.getStringList(entry + ".lore"));
        builder.addEnchantments(section.getStringList(entry + ".enchantments"));
        builder.addItemFlags(section.getStringList(entry + ".item-flags"));
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
