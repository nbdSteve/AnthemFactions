package gg.steve.anthem.utils;

import gg.steve.anthem.nbt.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Class that handles creating and item
 */
public class ItemBuilderUtil {
    private Material material;
    private Short dataValue;
    private ItemStack item;
    private ItemMeta itemMeta;
    private List<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Set<ItemFlag> flags = new HashSet<>();
    private NBTItem nbtItem;

    public ItemBuilderUtil(ItemStack item) {
        this.item = item;
        this.material = item.getType();
        this.dataValue = item.getDurability();
        this.itemMeta = item.getItemMeta();
        this.lore = item.getItemMeta().getLore();
        this.enchantments = item.getEnchantments();
        this.flags = item.getItemMeta().getItemFlags();
    }

    public ItemBuilderUtil(String material, String dataValue) {
        this.material = Material.getMaterial(material.toUpperCase());
        this.dataValue = Short.parseShort(dataValue);
        this.item = new ItemStack(this.material, 1, this.dataValue);
        this.itemMeta = item.getItemMeta();
    }

    public void addName(String name) {
        itemMeta.setDisplayName(ColorUtil.colorize(name));
        item.setItemMeta(itemMeta);
    }

    public void addName(String name, String placeholder, String replacement) {
        itemMeta.setDisplayName(ColorUtil.colorize(name.replace(placeholder, ColorUtil.colorize(replacement))));
        item.setItemMeta(itemMeta);
    }

    public void addLore(List<String> lore) {
        for (String line : lore) {
            this.lore.add(ColorUtil.colorize(line));
        }
        itemMeta.setLore(this.lore);
        item.setItemMeta(itemMeta);
    }

    public void replaceLorePlaceholder(String placeholder, String replacement) {
        for (int i = 0; i < this.lore.size(); i++) {
            this.lore.set(i, lore.get(i).replace(placeholder, replacement));
        }
        this.itemMeta.setLore(this.lore);
    }

    public void addEnchantments(List<String> enchants) {
        for (String enchantment : enchants) {
            String[] enchantmentParts = enchantment.split(":");
            itemMeta.addEnchant(Enchantment.getByName(enchantmentParts[0].toUpperCase()),
                    Integer.parseInt(enchantmentParts[1]), true);
            this.enchantments.put(Enchantment.getByName(enchantmentParts[0].toUpperCase()),
                    Integer.parseInt(enchantmentParts[1]));
        }
        item.setItemMeta(itemMeta);
    }

    public void addItemFlags(List<String> itemFlags) {
        for (String flag : itemFlags) {
            itemMeta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
            this.flags.add(ItemFlag.valueOf(flag.toUpperCase()));
        }
        item.setItemMeta(itemMeta);
    }

    public void addData(String type, YamlConfiguration config) {
        List<String> data = new ArrayList<>();
        if (type.equalsIgnoreCase("trench")) {
            data.add("trench");
            data.add(config.getString("radius"));
        } else if (type.equalsIgnoreCase("tray")) {
            data.add("tray");
            data.add(config.getString("radius"));
        }
        nbtItem.setObject("data", data);
    }

    /**
     * Void method to update the item with the new lore & meta
     *
     * @param itemLore List<String>, the lore that should be set
     * @param itemMeta ItemMeta, the item meta that should be set
     * @param item     ItemStack, the item being updated
     */
    public static void updateItem(List<String> itemLore, ItemMeta itemMeta, ItemStack item) {
        short durability = item.getDurability();
        item.setDurability(durability);
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
    }

    public void give(Player player) {
        player.getInventory().addItem(nbtItem.getItem());
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public Set<ItemFlag> getFlags() {
        return flags;
    }
}