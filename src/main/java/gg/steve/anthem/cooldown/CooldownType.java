package gg.steve.anthem.cooldown;

import gg.steve.anthem.managers.FileManager;

public enum CooldownType {
    // cooldowns
    CREATE(FileManager.get("cooldowns").getInt("CREATE")),
    // timeouts
    INVITE(FileManager.get("cooldowns").getInt("INVITE")),
    DISBAND(FileManager.get("cooldowns").getInt("DISBAND")),
    UN_ALLY(FileManager.get("cooldowns").getInt("UN_ALLY")),
    // delays
    HOME_TELEPORT(FileManager.get("cooldowns").getInt("HOME_TELEPORT")),
    CREATE_TELEPORT(FileManager.get("cooldowns").getInt("CREATE_TELEPORT")),
    LEAVE_TELEPORT(FileManager.get("cooldowns").getInt("LEAVE_TELEPORT")),
    RAID_TELEPORT(FileManager.get("cooldowns").getInt("LEAVE_TELEPORT")),
    KICK_TELEPORT(FileManager.get("cooldowns").getInt("KICK_TELEPORT")),
    PLANT_XP_DELAY(FileManager.get("cooldowns").getInt("PLANT_XP_DELAY"));

    private final int duration;

    CooldownType(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
