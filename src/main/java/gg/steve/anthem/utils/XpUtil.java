package gg.steve.anthem.utils;

public class XpUtil {

    public static double levelsToXp(double levels) {
        if (levels <= 16) {
            return Math.pow(levels, 2) + 6 * levels;
        } else if (levels >= 17 && levels <= 31) {
            return 2.5 * Math.pow(levels, 2) - 40.5 * levels + 360;
        } else if (levels >= 32) {
            return 4.5 * Math.pow(levels, 2) - 162.5 * levels + 2220;
        }
        return -1;
    }

    public static double xpToLevels(double xp) {
        if (xp <= 352) {
            return Math.sqrt(xp + 9) - 3;
        } else if (xp >= 394 && xp <= 1507) {
            return (Math.sqrt(40 * xp - 7839) + 81) * 0.1;
        } else if (xp >= 1628) {
            return (Math.sqrt(72 * xp - 54215) + 325) / 18;
        }
        return -1;
    }
}
