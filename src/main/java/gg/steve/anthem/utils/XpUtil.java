package gg.steve.anthem.utils;

import gg.steve.anthem.player.FPlayer;
import org.bukkit.entity.Player;

public class XpUtil {

    public static double getXpForLevel(double level) {
        if (level <= 15) {
            return level  * 17;
        }else if (level > 16 && level < 31) {
            return 1.5 * Math.pow(level, 2) - 29.5 * level + 360;
        }else if (level > 30) {
            return 3.5 * Math.pow(level, 2) - 151.5 * level + 2220;
        }
        return 0;
    }

    public static double getLevelForXP(double xp) {
        if (xp <= 255) {
            return xp  / 17;
        }else if (xp > 272 && xp < 887) {
            return (Math.sqrt(24 * xp - 5159) + 59) / 6;
        }else if (xp > 825) {
            return (Math.sqrt(56 * xp - 32511) + 303) / 14;
        }
        return 0;
    }

    public static int getTotalExperience(int level) {
        int xp = 0;
        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }

    public static int getTotalExperience(FPlayer player) {
        return Math.round(player.getPlayer().getExp() * player.getPlayer().getExpToLevel()) + getTotalExperience(player.getPlayer().getLevel());
    }

    public static void setTotalExperience(FPlayer player, int amount) {
        int level = 0;
        int xp = 0;
        float a = 0;
        float b = 0;
        float c = -amount;
        if (amount > getTotalExperience(0) && amount <= getTotalExperience(15)) {
            a = 1;
            b = 6;
        } else if (amount > getTotalExperience(15) && amount <= getTotalExperience(30)) {
            a = 2.5f;
            b = -40.5f;
            c += 360;
        } else if (amount > getTotalExperience(30)) {
            a = 4.5f;
            b = -162.5f;
            c += 2220;
        }
        level = (int) Math.floor((-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a));
        xp = amount - getTotalExperience(level);
        player.getPlayer().setLevel(level);
        player.getPlayer().setExp(0);
        player.getPlayer().giveExp(xp);
    }
}