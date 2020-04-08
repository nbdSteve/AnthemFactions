package gg.steve.anthem.core;

import gg.steve.anthem.managers.FileManager;

import java.util.UUID;

public class Wilderness extends Faction {

    public Wilderness() {
        super(UUID.fromString(FileManager.get("config").getString("wilderness-uuid")), "Wilderness", UUID.fromString(FileManager.get("config").getString("wilderness-uuid")));
    }
}
