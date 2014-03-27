package kr.scramban.wac.domain.map;

import java.util.ArrayList;
import java.util.List;

public class SetupableSuperRegion implements SuperRegion {

    private final int id;
    private final List<Region> regions = new ArrayList<Region>();
    private final int bonus;

    public SetupableSuperRegion(final int id, final int bonus) {
        this.id = id;
        this.bonus = bonus;
    }

    public void addRegion(final Region region) {
        regions.add(region);
    }

    public int getId() {
        return id;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public int getBonus() {
        return bonus;
    }
}
