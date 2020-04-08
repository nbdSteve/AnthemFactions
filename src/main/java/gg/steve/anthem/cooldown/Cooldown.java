package gg.steve.anthem.cooldown;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.utils.MessageUtil;
import org.bukkit.Bukkit;

import java.util.UUID;

public class Cooldown {
    private CooldownType type;
    private int duration;
    private Long end;
    private Faction faction;

    public Cooldown(CooldownType type, int duration) {
        this.type = type;
        this.duration = duration;
        this.end = System.currentTimeMillis() + (duration * 1000);
    }

    public Cooldown(CooldownType type, int duration, Faction faction) {
        this.type = type;
        this.duration = duration;
        this.end = System.currentTimeMillis() + (duration * 1000);
        this.faction = faction;
    }

    public boolean isActive() {
        return end > System.currentTimeMillis();
    }

    public CooldownType getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public Long getRemaining() {
        return (end - System.currentTimeMillis()) / 1000;
    }

    public void message(UUID uuid) {
        MessageUtil.message("lang", "cooldown", Bukkit.getPlayer(uuid), "{seconds-remaining}", getRemaining().toString());
    }

    public Faction getFaction() {
        return faction;
    }
}
