package gg.steve.anthem.disband;

import gg.steve.anthem.AnthemFactions;
import gg.steve.anthem.core.Faction;
import gg.steve.anthem.managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

public class FactionDeletion {

    public static void disband(Faction faction) {
        for (Player player : faction.getFWorld().getPlayers()) {
            player.teleport(Bukkit.getServer().getWorld(FileManager.get("config").getString("main-world-name")).getSpawnLocation());
        }
        String worldName = "plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + faction.getName();
        Bukkit.getServer().unloadWorld(worldName, false);
        deleteFile(new File(worldName));
        faction.getData().delete();
    }

    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                String[] files = file.list();
                for (String temp : files) {
                    File fileToDelete = new File(file, temp);
                    deleteFile(fileToDelete);
                }
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        } else {
            file.delete();
        }
    }
}
