package kr.scramban.wac.domain.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.scramban.wac.domain.player.Player;

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

    public void setRegion(final int regionId, final Player owner, final int army) {
        SetupableRegion region = regions.get(regionId);
        region.setOwner(owner);
        region.setArmy(army);
    }

    public List<Region> getMyRegions() {
        List<Region> myRegions = new ArrayList<Region>();
        for (Region region : regions.values()) {
            if (region.isMy()) {
                myRegions.add(region);
            }
        }
        return myRegions;
    }

    public List<Region> getMyBorderRegions() {
        List<Region> myRegions = new ArrayList<Region>();
        for (Region region : regions.values()) {
            if (region.isMy() && !region.isHinterland()) {
                myRegions.add(region);
            }
        }
        return myRegions;
    }

    public List<Region> getMyHinterlandRegions() {
        List<Region> myRegions = new ArrayList<Region>();
        for (Region region : regions.values()) {
            if (region.isMy() && region.isHinterland()) {
                myRegions.add(region);
            }
        }
        return myRegions;
    }
}
