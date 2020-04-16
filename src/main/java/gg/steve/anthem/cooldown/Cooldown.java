package gg.steve.anthem.cooldown;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.utils.actionbarapi.ActionBarType;
import org.bukkit.Bukkit;

import java.util.UUID;

public class Cooldown {
    private CooldownType type;
    private int duration;
    private Long end;
    private Faction faction;

    public Cooldown(CooldownType type) {
        this.type = type;
        this.duration = type.getDuration();
        this.end = System.currentTimeMillis() + (duration * 1000);
    }

    public Cooldown(CooldownType type, Faction faction) {
        this.type = type;
        this.duration = type.getDuration();
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

    public void sendActionBar(UUID uuid) {
        ActionBarType.COOLDOWN.send(Bukkit.getPlayer(uuid), getRemaining().toString());
    }

    public Faction getFaction() {
        return faction;
    }
}
