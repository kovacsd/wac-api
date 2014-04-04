package kr.scramban.wac.domain.map;

import java.util.List;

public interface SuperRegion {

    int getId();

    boolean isMy();

    List<Region> getRegions();

    boolean contains(Region region);

    int getRegionsCount();

    int getOwnedCount();

    List<Region> getNotOwnedRegions();

    int getBonus();
}
