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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isMy() {
        for (Region region : regions) {
            if (!region.isMy()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(final Region region) {
        return regions.contains(region);
    }

    @Override
    public int getRegionsCount() {
        return regions.size();
    }

    @Override
    public int getOwnedCount() {
        int ownedCount = 0;
        for (Region region : regions) {
            if (region.isMy()) {
                ownedCount++;
            }
        }
        return ownedCount;
    }

    @Override
    public List<Region> getNotOwnedRegions() {
        List<Region> notOwnedRegions = new ArrayList<Region>();
        for (Region region : regions) {
            if (!region.isMy()) {
                notOwnedRegions.add(region);
            }
        }
        return notOwnedRegions;
    }

    @Override
    public int getBonus() {
        return bonus;
    }
}
