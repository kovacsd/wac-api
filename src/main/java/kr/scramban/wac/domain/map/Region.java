package kr.scramban.wac.domain.map;

import java.util.List;

import kr.scramban.wac.domain.player.Player;

public interface Region {

    int getId();

    SuperRegion getSuperRegion();

    List<? extends Region> getNeighbors();

    Player getOwner();

    int getArmy();

    int getEnemyArmy();

    void addArmy(long reinforcement);

    boolean isMy();

    boolean isHinterland();

    Integer getHinterlandCount();

    Region getNeighborWithLowestHinterlandCount();

    int getCountOfEnemyNeighbor();

    List<Region> getEnemyNeighbors();

    boolean isSuperBorder();
}
