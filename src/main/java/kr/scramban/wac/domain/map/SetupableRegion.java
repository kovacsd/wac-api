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

    @Override
    public void addArmy(final long reinforcement) {
        army += reinforcement;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<Region> getNeighbor() {
        return neighbor;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public long getArmy() {
        return army;
    }

    @Override
    public boolean isHinterland() {
        if (!owner.isMe()) {
            return false;
        }
        for (Region region : neighbor) {
            if (!region.getOwner().isMe()) {
                return false;
            }
        }
        return true;
    }
}
