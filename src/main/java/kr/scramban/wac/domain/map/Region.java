package kr.scramban.wac.domain.map;

import java.util.List;

import kr.scramban.wac.domain.player.Player;

public interface Region {

    int getId();

    SuperRegion getSuperRegion();

    List<? extends Region> getNeighbors();

    List<Region> getElseNeighbors();

    List<Region> getMyNeighbors();

    Player getOwner();

    int getArmy();

    void addArmy(long reinforcement);

    int getNeighborEnemyArmy();

    int getNeighborMyArmy();

    boolean isMy();

    boolean isHinterland();

    Integer getHinterlandCount();

    Region getNeighborWithLowestHinterlandCount();

    boolean isSuperBorder();
}
