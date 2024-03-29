package gg.steve.anthem.disband;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.raid.FRaidManager;
import gg.steve.anthem.relation.RelationType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class FactionDeletion {

    public static void disband(Faction faction) {
        Location spawn = Bukkit.getServer().getWorld(FileManager.get("config").getString("main-world-name")).getSpawnLocation();
        for (UUID uuid : faction.getOfflinePlayers()) {
            FPlayerManager.addDisbandedPlayer(uuid);
        }
        for (Player player : faction.getFWorld().getPlayers()) {
            player.teleport(spawn);
        }
        for (UUID uuid : faction.getRelationManager().getRelations(RelationType.ALLY)) {
            Faction ally = FactionManager.getFaction(uuid);
            ally.getRelationManager().updateRelation(faction, RelationType.NEUTRAL);
        }
        for (UUID uuid : faction.getRelationManager().getRelations(RelationType.ENEMY)) {
            Faction enemy = FactionManager.getFaction(uuid);
            enemy.getRelationManager().updateRelation(faction, RelationType.NEUTRAL);
        }
        if (faction.isOnRaidCooldown()) {
            FRaidManager.removeFactionOnRaidCooldown(faction.getId());
        }
        String worldName = "plugins" + File.separator + "AnthemFactions" + File.separator + "faction-worlds" + File.separator + faction.getId();
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
