package kr.scramban.wac.domain.map;

import java.util.List;

import kr.scramban.wac.domain.player.Player;

public interface Region {

    int getId();

    List<? extends Region> getNeighbors();

    Player getOwner();

    int getArmy();

    void addArmy(long reinforcement);

    boolean isMy();

    boolean isHinterland();

    int getHinterlandCount();

    Region getNeighborWithLowestHinterlandCount();

    int getCountOfEnemyNeighbor();

    List<Region> getEnemyNeighbors();
}
