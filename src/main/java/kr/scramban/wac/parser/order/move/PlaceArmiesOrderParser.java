package kr.scramban.wac.parser.order.move;

import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.container.PlaceArmiesContainer;
import kr.scramban.wac.parser.order.OrderParser;

public class PlaceArmiesOrderParser implements OrderParser {

    private final GameContext context;
    private PlaceArmiesContainer container;

    public PlaceArmiesOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        container = new PlaceArmiesContainer(context.getPlayerList().getMyName());
        divideReinforcement(context.getArmyCount().getReinforcement(), context.getWorld().getMyBorderRegions());
        return container.printReinforcement();
    }

    private void divideReinforcement(final int fullReinforcement, final List<Region> myBorderRegions) {
        int restOfReinforcement = sendOffensiveArmy(myBorderRegions, fullReinforcement + 0);
        setPriorityBySuperBorder(myBorderRegions);
        setPriorityByBalance(myBorderRegions);
        setPriorityByThreat(myBorderRegions);
        container.divideByPriority(restOfReinforcement);
    }

    private int sendOffensiveArmy(final List<Region> regions, final int fullReinforcement) {
        int averageArmyOnBorder = calculateAverageArmy(regions);
        int restOfReinforcement = fullReinforcement;
        if (averageArmyOnBorder < 5) {
            for (Region region : regions) {
                if (restOfReinforcement > regions.size() && region.getArmy() < 5) {
                    int army = 5 - region.getArmy();
                    container.addReinforcement(region, army);
                    restOfReinforcement -= army;
                }
            }
        }
        return restOfReinforcement;
    }

    private void setPriorityBySuperBorder(final List<Region> regions) {
        for (Region region : regions) {
            if (region.isSuperBorder()) {
                container.addPriority(region, 5);
            }
        }
    }

    private void setPriorityByBalance(final List<Region> regions) {
        int averageArmyOnBorder = calculateAverageArmy(regions);
        for (Region region : regions) {
            if (region.getArmy() < averageArmyOnBorder * 1.5) {
                container.addPriority(region, (int) (averageArmyOnBorder * 1.5 - region.getArmy()) / 2);
            }
        }
    }

    private void setPriorityByThreat(final List<Region> regions) {
        for (Region region : regions) {
            if (region.getNeighborEnemyArmy() > 0) {
                container.addPriority(region, region.getNeighborEnemyArmy());
            }
        }
    }

    private int calculateAverageArmy(final List<Region> regions) {
        int sumArmy = 0;
        for (Region region : regions) {
            sumArmy += region.getArmy();
        }
        return sumArmy / regions.size();
    }
}