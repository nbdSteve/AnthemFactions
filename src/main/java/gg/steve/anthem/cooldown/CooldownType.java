package gg.steve.anthem.cooldown;

import gg.steve.anthem.managers.FileManager;

public enum CooldownType {
    // cooldowns
    CREATE(FileManager.get("cooldowns").getInt("CREATE")),
    PLANT_XP_DELAY(FileManager.get("cooldowns").getInt("PLANT_XP_DELAY")),
    FLY_FALL_DAMAGE(FileManager.get("cooldowns").getInt("FLY_FALL_DAMAGE")),
    // timeouts
    INVITE(FileManager.get("cooldowns").getInt("INVITE")),
    DISBAND(FileManager.get("cooldowns").getInt("DISBAND")),
    UN_ALLY(FileManager.get("cooldowns").getInt("UN_ALLY")),
    // delays,
    HOME_TELEPORT(FileManager.get("cooldowns").getInt("HOME_TELEPORT")),
    CREATE_TELEPORT(FileManager.get("cooldowns").getInt("CREATE_TELEPORT")),
    RAID_TELEPORT(FileManager.get("cooldowns").getInt("RAID_TELEPORT")),
    LEAVE_TELEPORT(FileManager.get("cooldowns").getInt("LEAVE_TELEPORT")),
    KICK_TELEPORT(FileManager.get("cooldowns").getInt("KICK_TELEPORT"));

    private final int duration;

    CooldownType(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
