package gg.steve.anthem.message;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.utils.ColorUtil;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

public enum BroadcastType {
    CREATION("broadcasts", "faction-creation", "{faction}", "{player}"),
    RAID_START("broadcasts", "raid-start", "{defender}", "{raider}", "{tier}"),
    RAID_COMPLETE("broadcasts", "raid-completion", "{defender}", "{raider}", "{tier}");

    private final String directory;
    private final String path;
    private List<String> placeholders;

    BroadcastType(String directory, String path, String... placeholders) {
        this.directory = directory;
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void broadcast(String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : FileManager.get(this.directory).getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            Bukkit.broadcastMessage(ColorUtil.colorize(line));
        }
    }
}
