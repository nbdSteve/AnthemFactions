package gg.steve.anthem.wealth;

import gg.steve.anthem.core.Faction;

import java.util.Comparator;

public class WealthComparator implements Comparator<Faction> {
    @Override
    public int compare(Faction o1, Faction o2) {
        return (int) (o2.getWealth() - o1.getWealth());
    }
}
