package gg.steve.anthem.relation;

import gg.steve.anthem.core.Faction;
import gg.steve.anthem.core.FactionManager;

import java.util.*;

public class RelationManager {
    private Map<UUID, RelationType> relations;
    private List<UUID> outGoingAllyRequests;
    private Faction faction;

    public RelationManager(Faction faction) {
        this.relations = new HashMap<>();
        this.outGoingAllyRequests = new ArrayList<>();
        this.faction = faction;
        loadRelations();
        loadOutGoingAllyRequests();
    }

    public void loadRelations() {
        for (String uuid : faction.getData().get().getStringList("relations.ally")) {
            this.relations.put(UUID.fromString(uuid), RelationType.ALLY);
        }
        for (String uuid : faction.getData().get().getStringList("relations.enemy")) {
            this.relations.put(UUID.fromString(uuid), RelationType.ENEMY);
        }
    }

    public void loadOutGoingAllyRequests() {
        for (String uuid : faction.getData().get().getStringList("relations.outgoing-ally-requests")) {
            this.outGoingAllyRequests.add(UUID.fromString(uuid));
        }
    }

    public void setAllyRequest(Faction relation) {
        List<String> allyRequests = faction.getData().get().getStringList("relations.outgoing-ally-requests");
        allyRequests.add(relation.getId().toString());
        this.faction.getData().get().set("relations.outgoing-ally-requests", allyRequests);
        this.faction.getData().save();
        this.outGoingAllyRequests.add(relation.getId());
    }

    public void removeAllyRequestIfExists(Faction relation) {
        if (!hasOutgoingAllyRequest(relation)) return;
        List<String> allyRequests = faction.getData().get().getStringList("relations.outgoing-ally-requests");
        allyRequests.remove(relation.getId().toString());
        this.faction.getData().get().set("relations.outgoing-ally-requests", allyRequests);
        this.faction.getData().save();
        this.outGoingAllyRequests.remove(relation.getId());
    }

    public boolean hasRelation(Faction relation) {
        return relations.get(relation.getId()) != null;
    }

    public boolean areEnemied(Faction relation) {
        return relation.getRelationManager().isEnemy(faction) || isEnemy(relation);
    }

    public void updateRelation(Faction relation, RelationType type) {
        if (faction.hasRelation(relation)) {
            List<String> currentRelation = this.faction.getData().get().getStringList("relations." + this.relations.get(relation.getId()).toString().toLowerCase());
            currentRelation.remove(relation.getId().toString());
            this.faction.getData().get().set("relations." + this.relations.get(relation.getId()).toString().toLowerCase(), currentRelation);
            this.faction.getData().save();
            this.relations.remove(relation.getId());
            if (type.equals(RelationType.NEUTRAL)) return;
        }
        if (type.equals(RelationType.NEUTRAL)) return;
        if (type.equals(RelationType.ALLY)) removeAllyRequestIfExists(relation);
        List<String> newRelation = this.faction.getData().get().getStringList("relations." + type.toString().toLowerCase());
        newRelation.add(relation.getId().toString());
        this.faction.getData().get().set("relations." + type.toString().toLowerCase(), newRelation);
        this.faction.getData().save();
        this.relations.put(relation.getId(), type);
    }

    public boolean hasOutgoingAllyRequest(Faction relation) {
        return this.outGoingAllyRequests.contains(relation.getId());
    }

    public boolean hasIncomingAllyRequest(Faction relation) {
        return relation.getRelationManager().hasOutgoingAllyRequest(this.faction);
    }

    public boolean isAlly(Faction relation) {
        if (!hasRelation(relation)) return false;
        return this.relations.get(relation.getId()).equals(RelationType.ALLY);
    }

    public boolean isEnemy(Faction relation) {
        if (!hasRelation(relation)) return false;
        return this.relations.get(relation.getId()).equals(RelationType.ENEMY);
    }

    public boolean isNeutral(Faction relation) {
        if (!hasRelation(relation)) return true;
        return false;
    }

    public int getRelationCount(RelationType type) {
        int count = 0;
        for (Map.Entry relation : relations.entrySet()) {
            if (relation.getValue().equals(type)) count++;
        }
        return count;
    }

    public List<UUID> getRelations(RelationType type) {
        List<UUID> factions = new ArrayList<>();
        for (UUID uuid : relations.keySet()) {
            if (relations.get(uuid).equals(type)) factions.add(uuid);
        }
        return factions;
    }

    public RelationType getRelationType(Faction relation) {
        if (relation.getId().equals(FactionManager.getWildernessId())) return RelationType.WILDERNESS;
        if (relation.getId().equals(faction.getId())) return RelationType.FACTION;
        if (!hasRelation(relation)) return RelationType.NEUTRAL;
        return this.relations.get(relation.getId());
    }
}