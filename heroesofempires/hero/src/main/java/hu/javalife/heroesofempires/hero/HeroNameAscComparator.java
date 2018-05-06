package hu.javalife.heroesofempires.hero;

import java.util.Comparator;

/**
 * @author krisztian
 */
public class HeroNameAscComparator implements Comparator<HeroDataModel>{

    @Override
    public int compare(HeroDataModel o1, HeroDataModel o2) {
        return o2.getName().compareToIgnoreCase(o1.getName());
    }

    
}
