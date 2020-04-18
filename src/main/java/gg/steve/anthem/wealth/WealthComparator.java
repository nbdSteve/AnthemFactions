package gg.steve.anthem.wealth;

import gg.steve.anthem.core.Faction;

import java.util.Comparator;

/**
 * Custom comparator class for determining which faction is wealthier
 */
public class WealthComparator implements Comparator<Faction> {

    /**
     * Compares 2 factions wealth
     *
     * @param f1 Faction
     * @param f2 Faction
     * @return int
     */
    @Override
    public int compare(Faction f1, Faction f2) {
        return (int) (f2.getWealth() - f1.getWealth());
    }
}
