package gg.steve.anthem.role;

public enum Role {
    OWNER(5),
    CO_OWNER(4),
    MODERATOR(3),
    MEMBER(2),
    WILDERNESS(1);

    private final int weight;

    Role(int weight) {
        this.weight = weight;
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
