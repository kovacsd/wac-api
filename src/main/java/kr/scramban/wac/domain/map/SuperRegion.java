package kr.scramban.wac.domain.map;

import java.util.List;

public interface SuperRegion {

    int getId();

    List<Region> getRegions();

    int getBonus();
}
