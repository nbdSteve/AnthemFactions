package gg.steve.anthem.relation;

import gg.steve.anthem.core.Faction;

public class Relation {

    public static void updateRelation(Faction faction, Faction relation, RelationType type) {

    }

    public static boolean relationAlreadySet(Faction faction, Faction relation) {
        return false;
    }

    public static void setEnemy(Faction faction, Faction enemy) {
        updateRelation(faction, enemy, RelationType.ENEMY);
    }

    public static void setAlly(Faction faction, Faction ally) {
        updateRelation(faction, ally, RelationType.ALLY);
    }

    public static void setNeutral(Faction faction, Faction neutral) {
        updateRelation(faction, neutral, RelationType.NEUTRAL);
    }
}
