package hu.javalife.heroesofempires.hero;

import java.util.Comparator;

/**
 * @author krisztian
 */
public class HeroNameDescComparator implements Comparator<HeroDataModel>{

    @Override
    public int compare(HeroDataModel o1, HeroDataModel o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }

    
}
