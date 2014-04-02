package kr.scramban.wac.domain.map;

import java.util.List;

public interface SuperRegion {

    int getId();

    boolean isMy();

    List<Region> getRegions();

    int getRegionsCount();

    int getOwnedCount();

    List<Region> getNotOwnedRegions();

    int getBonus();
}
