package kr.scramban.wac.domain.map;

import java.util.ArrayList;
import java.util.Collection;
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
        SetupableSuperRegion superRegion = superRegions.get(superRegionId);
        SetupableRegion region = new SetupableRegion(regionId, superRegion);
        superRegion.addRegion(region);
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

    public Collection<? extends SuperRegion> getSuperRegions() {
        return superRegions.values();
    }

    public Region getRegion(final int regionId) {
        return regions.get(regionId);
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

    public List<Region> getOutOfBorderRegions() {
        List<Region> outOfBorderRegions = new ArrayList<Region>();
        for (Region region : regions.values()) {
            if (!region.isMy()) {
                boolean myNeighbor = false;
                for (Region neighbor : region.getNeighbors()) {
                    if (neighbor.isMy()) {
                        myNeighbor = true;
                        break;
                    }
                }
                if (myNeighbor) {
                    outOfBorderRegions.add(region);
                }
            }
        }
        return outOfBorderRegions;
    }

    public List<Region> getMyHinterlandRegions() {
        List<Region> myRegions = new ArrayList<Region>();
        for (Region region : regions.values()) {
            if (region.isHinterland()) {
                myRegions.add(region);
            }
        }
        return myRegions;
    }

    public void resetMap() {
        for (SetupableRegion region : regions.values()) {
            region.setOwner(null);
            region.setArmy(0);
        }
    }
}
