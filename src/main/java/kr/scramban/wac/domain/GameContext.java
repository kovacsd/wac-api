package kr.scramban.wac.domain;

import kr.scramban.wac.domain.army.ArmyCount;
import kr.scramban.wac.domain.map.World;
import kr.scramban.wac.domain.player.PlayerList;

public class GameContext {

    private final PlayerList playerList = new PlayerList();
    private final World world = new World();
    private final ArmyCount armyCount = new ArmyCount();

    public PlayerList getPlayerList() {
        return playerList;
    }

    public World getWorld() {
        return world;
    }

    public ArmyCount getArmyCount() {
        return armyCount;
    }
}
