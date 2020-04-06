package gg.steve.anthem.utils;

import gg.steve.anthem.AnthemFactions;

public class LogUtil {

    public static void info(String message) {
        AnthemFactions.get().getLogger().info(message);
    }

    public static void warning(String message) {
        AnthemFactions.get().getLogger().warning(message);
    }
}
