package gg.steve.anthem.cooldown;

import gg.steve.anthem.managers.FileManager;

public enum CooldownType {
    INVITE(FileManager.get("cooldowns").getInt("INVITE")),
    CREATE(FileManager.get("cooldowns").getInt("CREATE")),
    DISBAND(FileManager.get("cooldowns").getInt("DISBAND"));

    private final int duration;

    CooldownType(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
