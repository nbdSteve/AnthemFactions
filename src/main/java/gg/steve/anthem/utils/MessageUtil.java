package gg.steve.anthem.utils;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class that handles sending messages with a range of placeholders
 */
public class MessageUtil {

    /**
     * Send a default message to the player
     *
     * @param directory String, the directory file for the messages
     * @param path      String, the internal path for the message
     * @param player    Player, the player to send the message to
     */
    public static void message(String directory, String path, Player player) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line));
        }
    }

    /**
     * Send a message to the player which contains 1 placeholder
     *
     * @param directory   String, the directory file for the messages
     * @param path        String, the internal path for the message
     * @param player      Player, the player to send the message to
     * @param placeholder String, the first placeholder
     * @param replacement String, the first replacement
     */
    public static void message(String directory, String path, Player player, String placeholder, String replacement) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line).replace(placeholder, replacement));
        }
    }

    public static void message(String directory, String path, CommandSender player, String placeholder, String replacement) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line).replace(placeholder, replacement));
        }
    }

    /**
     * Send a message to the player which contains 2 placeholders
     *
     * @param directory     String, the directory file for the messages
     * @param path          String, the internal path for the message
     * @param player        Player, the player to send the message to
     * @param placeholder   String, the first placeholder
     * @param replacement   String, the first replacement
     * @param placeholder_2 String, the second placeholder
     * @param replacement_2 String, the second replacement
     */
    public static void message(String directory, String path, Player player,
                               String placeholder, String replacement,
                               String placeholder_2, String replacement_2) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line)
                    .replace(placeholder, replacement)
                    .replace(placeholder_2, replacement_2));
        }
    }

    public static void message(String directory, String path, CommandSender player,
                               String placeholder, String replacement,
                               String placeholder_2, String replacement_2) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line)
                    .replace(placeholder, replacement)
                    .replace(placeholder_2, replacement_2));
        }
    }

    /**
     * Send a message to the player which contains 3 placeholders
     *
     * @param directory     String, the directory file for the messages
     * @param path          String, the internal path for the message
     * @param player        Player, the player to send the message to
     * @param placeholder   String, the first placeholder
     * @param replacement   String, the first replacement
     * @param placeholder_2 String, the second placeholder
     * @param replacement_2 String, the second replacement
     * @param placeholder_3 String, the third placeholder
     * @param replacement_3 String, the third replacement
     */
    public static void message(String directory, String path, Player player,
                               String placeholder, String replacement,
                               String placeholder_2, String replacement_2,
                               String placeholder_3, String replacement_3) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line)
                    .replace(placeholder, replacement)
                    .replace(placeholder_2, replacement_2)
                    .replace(placeholder_3, replacement_3));
        }
    }

    /**
     * Send a message to the player which contains 3 placeholders
     *
     * @param directory     String, the directory file for the messages
     * @param path          String, the internal path for the message
     * @param player        Player, the player to send the message to
     * @param placeholder   String, the first placeholder
     * @param replacement   String, the first replacement
     * @param placeholder_2 String, the second placeholder
     * @param replacement_2 String, the second replacement
     * @param placeholder_3 String, the third placeholder
     * @param replacement_3 String, the third replacement
     * @param placeholder_4 String, the fourth placeholder
     * @param replacement_4 String, the fourth replacement
     */
    public static void message(String directory, String path, Player player,
                               String placeholder, String replacement,
                               String placeholder_2, String replacement_2,
                               String placeholder_3, String replacement_3,
                               String placeholder_4, String replacement_4) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line)
                    .replace(placeholder, replacement)
                    .replace(placeholder_2, replacement_2)
                    .replace(placeholder_3, replacement_3)
                    .replace(placeholder_4, replacement_4));
        }
    }

    /**
     * Send a message to the player which contains 3 placeholders
     *
     * @param directory     String, the directory file for the messages
     * @param path          String, the internal path for the message
     * @param player        Player, the player to send the message to
     * @param placeholder   String, the first placeholder
     * @param replacement   String, the first replacement
     * @param placeholder_2 String, the second placeholder
     * @param replacement_2 String, the second replacement
     * @param placeholder_3 String, the third placeholder
     * @param replacement_3 String, the third replacement
     * @param placeholder_4 String, the fourth placeholder
     * @param replacement_4 String, the fourth replacement
     * @param placeholder_5 String, the fifth placeholder
     * @param replacement_5 String, the fifth replacement
     */
    public static void message(String directory, String path, Player player,
                               String placeholder, String replacement,
                               String placeholder_2, String replacement_2,
                               String placeholder_3, String replacement_3,
                               String placeholder_4, String replacement_4,
                               String placeholder_5, String replacement_5) {
        for (String line : FileManager.get(directory).getStringList(path)) {
            player.sendMessage(ColorUtil.colorize(line)
                    .replace(placeholder, replacement)
                    .replace(placeholder_2, replacement_2)
                    .replace(placeholder_3, replacement_3)
                    .replace(placeholder_4, replacement_4)
                    .replace(placeholder_5, replacement_5));
        }
    }

    public static void permissionDebug(CommandSender sender, String node) {
        for (String line : FileManager.get("lang").getStringList("permission-debug")) {
            sender.sendMessage(ColorUtil.colorize(line).replace("{permission-node}", node));
        }
    }

    public static void permissionDebug(FPlayer fPlayer, String node) {
        for (String line : FileManager.get("lang").getStringList("permission-debug")) {
            fPlayer.message(ColorUtil.colorize(line).replace("{permission-node}", node));
        }
    }

    public static void commandDebug(CommandSender sender, String reason) {
        for (String line : FileManager.get("lang").getStringList("command-debug")) {
            sender.sendMessage(ColorUtil.colorize(line).replace("{reason}", reason));
        }
    }

    public static void commandDebug(FPlayer fPlayer, String reason) {
        for (String line : FileManager.get("lang").getStringList("command-debug")) {
            fPlayer.message(ColorUtil.colorize(line).replace("{reason}", reason));
        }
    }

    public static void helpMessage(CommandSender sender) {
        for (String line : FileManager.get("lang").getStringList("help")) {
            sender.sendMessage(ColorUtil.colorize(line));
        }
    }

    public static void reloadMessage(CommandSender sender) {
        for (String line : FileManager.get("lang").getStringList("reload")) {
            sender.sendMessage(ColorUtil.colorize(line));
        }
    }
}