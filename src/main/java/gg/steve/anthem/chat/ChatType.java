package gg.steve.anthem.chat;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;
import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.player.FPlayer;
import gg.steve.anthem.player.FPlayerManager;
import gg.steve.anthem.relation.RelationType;
import gg.steve.anthem.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public enum ChatType {
    FACTION(FileManager.get("config").getString("chat-prefix.faction")),
    ALLY(FileManager.get("config").getString("chat-prefix.ally")),
    PUBLIC(FileManager.get("config").getString("chat-prefix.public"));

    private final String prefix;

    ChatType(String prefix) {
        this.prefix = prefix;
    }

    public void message(FPlayer fPlayer, String message) {
        if (fPlayer.getChatChannel().equals(ChatType.PUBLIC)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                FPlayer fPlayer1 = FPlayerManager.getFPlayer(player.getUniqueId());
                player.sendMessage(ColorUtil.colorize(prefix.replace("{relation-color}", fPlayer1.getFaction().getRelationManager().getRelationType(fPlayer.getFaction()).getPrefix()).replace("{role}", fPlayer.getRole().getPrefix()).replace("{faction}", fPlayer.getFaction().getName()).replace("{player}", fPlayer.getPlayer().getDisplayName()) + message));
            }
            return;
        }
        if (fPlayer.getChatChannel().equals(ChatType.ALLY)) {
            for (UUID uuid : fPlayer.getFaction().getRelationManager().getRelations(RelationType.ALLY)) {
                Faction ally = FactionManager.getFaction(uuid);
                ally.messageAllOnlinePlayers(ColorUtil.colorize(prefix.replace("{role}", fPlayer.getRole().getPrefix()).replace("{faction}", fPlayer.getFaction().getName()).replace("{player}", fPlayer.getPlayer().getDisplayName()) + message));
            }
            fPlayer.getFaction().messageAllOnlinePlayers(ColorUtil.colorize(prefix.replace("{role}", fPlayer.getRole().getPrefix()).replace("{faction}", fPlayer.getFaction().getName()).replace("{player}", fPlayer.getPlayer().getDisplayName()) + message));
            return;
        }
        fPlayer.getFaction().messageAllOnlinePlayers(ColorUtil.colorize(prefix.replace("{role}", fPlayer.getRole().getPrefix()).replace("{player}", fPlayer.getPlayer().getDisplayName()) + message));
    }
}
