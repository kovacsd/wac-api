package kr.scramban.wac.domain.map;

import java.util.ArrayList;
import java.util.List;

import kr.scramban.wac.domain.player.Player;

public class SetupableRegion implements Region {

    private final int id;
    private final List<Region> neighbor = new ArrayList<Region>();
    private Player owner;
    private long army;

    public SetupableRegion(final int id) {
        this.id = id;
    }

    public void addNeighbor(final Region region) {
        neighbor.add(region);
    }

    public void setOwner(final Player owner) {
        this.owner = owner;
    }

    public void setArmy(final long army) {
        this.army = army;
    }

    public int getId() {
        return id;
    }

    public List<Region> getNeighbor() {
        return neighbor;
    }

    public Player getOwner() {
        return owner;
    }

    public long getArmy() {
        return army;
    }
}
