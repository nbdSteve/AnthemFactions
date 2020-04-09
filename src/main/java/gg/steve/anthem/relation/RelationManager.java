package gg.steve.anthem.relation;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;

import java.util.HashMap;
import java.util.Map;

public class RelationManager {
    private Map<Faction, RelationType> relations;
    private Faction faction;

    public RelationManager(Faction faction) {
        this.relations = new HashMap<>();
        this.faction = faction;
        loadRelations();
    }

    public void loadRelations() {
        for (String uuid : faction.getData().get().getStringList("relations.ally")) {
            relations.put(FactionManager.getFaction(uuid), RelationType.ALLY);
        }
        for (String uuid : faction.getData().get().getStringList("relations.neutral")) {
            relations.put(FactionManager.getFaction(uuid), RelationType.NEUTRAL);
        }
        for (String uuid : faction.getData().get().getStringList("relations.enemy")) {
            relations.put(FactionManager.getFaction(uuid), RelationType.ENEMY);
        }
    }
}
