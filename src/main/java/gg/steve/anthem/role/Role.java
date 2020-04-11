package gg.steve.anthem.role;

import gg.steve.anthem.managers.FileManager;

public enum Role {
    OWNER(5, FileManager.get("config").getString("role-prefix.owner")),
    CO_OWNER(4, FileManager.get("config").getString("role-prefix.co-owner")),
    MODERATOR(3, FileManager.get("config").getString("role-prefix.moderator")),
    MEMBER(2, FileManager.get("config").getString("role-prefix.member")),
    WILDERNESS(1, "");

    private final int weight;
    private final String prefix;

    Role(int weight, String prefix) {
        this.weight = weight;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getWeight() {
        return this.weight;
    }

    public static boolean higherRole(Role role1, Role role2) {
        return role1.getWeight() >= role2.getWeight();
    }

    public static Role getRoleByWeight(int weight) {
        for (Role role : Role.values()) {
            if (role.getWeight() == weight) return role;
        }
        return WILDERNESS;
    }
}
