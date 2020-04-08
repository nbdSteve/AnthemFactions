package gg.steve.anthem.core;

import gg.steve.anthem.managers.FileManager;
import gg.steve.anthem.role.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Wilderness extends Faction {
    private List<String> permissions;

    public Wilderness() {
        super(UUID.fromString(FileManager.get("config").getString("wilderness-uuid")), "Wilderness", UUID.fromString(FileManager.get("config").getString("wilderness-uuid")));
    }

    @Override
    public void loadRolePermissionMap() {
        if (permissions == null) permissions = new ArrayList<>();
        for (String node : getData().get().getStringList("permissions.wilderness")) {
            permissions.add(node);
        }
    }

    @Override
    public boolean roleHasPermission(Role role, String node) {
        return permissions.contains(node);
    }
}
