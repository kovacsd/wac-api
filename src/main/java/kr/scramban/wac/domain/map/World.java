package kr.scramban.wac.domain.map;

import java.util.HashMap;
import java.util.Map;

public class World {

    private final Map<Integer, SetupableRegion> regions = new HashMap<Integer, SetupableRegion>();
    private final Map<Integer, SetupableSuperRegion> superRegions = new HashMap<Integer, SetupableSuperRegion>();

    public void addSuperRegion(final int superRegionId, final int bonus) {
        superRegions.put(superRegionId, new SetupableSuperRegion(superRegionId, bonus));
    }

    public void addRegion(final int regionId, final int superRegionId) {
        SetupableRegion region = new SetupableRegion(regionId);
        superRegions.get(superRegionId).addRegion(region);
        regions.put(regionId, region);
    }

    public void addNeighbors(final int regionId, final int... neighborIds) {
        SetupableRegion region = regions.get(regionId);
        for (int neighborId : neighborIds) {
            SetupableRegion neighbor = regions.get(neighborId);
            region.addNeighbor(neighbor);
            neighbor.addNeighbor(region);
        }
    }
}
