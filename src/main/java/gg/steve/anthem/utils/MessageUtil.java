package gg.steve.anthem.utils;

import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Class that handles sending messages with a range of placeholders
 */
public class MessageUtil {

    public static void message(String directory, String path, CommandSender sender, String... placeholders) {
        List<String> data = Arrays.asList(placeholders);
        for (String line : FileManager.get(directory).getStringList(path)) {
            for (int i = 0; i < data.size(); i += 2) {
                line = line.replace(data.get(i), data.get(i + 1));
            }
            sender.sendMessage(ColorUtil.colorize(line));
        }
    }

    public static void message(String directory, String path, Player player, String... placeholders) {
        List<String> data = Arrays.asList(placeholders);
        for (String line : FileManager.get(directory).getStringList(path)) {
            for (int i = 0; i < data.size(); i += 2) {
                line = line.replace(data.get(i), data.get(i + 1));
            }
            player.sendMessage(ColorUtil.colorize(line));
        }
    }

    public static void message(String directory, String path, FPlayer fPlayer, String... placeholders) {
        List<String> data = Arrays.asList(placeholders);
        for (String line : FileManager.get(directory).getStringList(path)) {
            for (int i = 0; i < data.size(); i += 2) {
                line = line.replace(data.get(i), data.get(i + 1));
            }
            fPlayer.getPlayer().sendMessage(ColorUtil.colorize(line));
        }
    }

    public static void listMessage(FPlayer fPlayer) {
        for (String line : FileManager.get("lang").getStringList("list")) {
            fPlayer.message(ColorUtil.colorize(line).replace("{total-factions}", String.valueOf(FactionManager.getTotalFactions())));
        }
    }
}