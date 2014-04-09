package kr.scramban.wac.parser.order.move;

import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.domain.map.SuperRegion;
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
        int restOfReinforcement = sendOffensiveArmy(myBorderRegions, fullReinforcement);
        restOfReinforcement = setBySuperOwn(restOfReinforcement);
        setPriorityBySuperBorder(myBorderRegions);
        setPriorityByBalance(myBorderRegions);
        setPriorityByThreat(myBorderRegions);
        container.divideByPriority(restOfReinforcement);
    }

    private int sendOffensiveArmy(final List<Region> regions, final int fullReinforcement) {
        int minArmyOnBorder = calculateMinArmy(regions);
        int restOfReinforcement = fullReinforcement;
        if (minArmyOnBorder <= 5) {
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

    private int setBySuperOwn(final int reinforcement) {
        int restOfReinforcement = reinforcement;
        SuperRegion closestSuperRegion = context.getWorld().getClosestSuperRegion();
        if (closestSuperRegion != null) {
            List<Region> notOwnedRegions = closestSuperRegion.getNotOwnedRegions();
            int notOwnedRegionCount = notOwnedRegions.size();
            if (notOwnedRegionCount == 1) {
                int army = reinforcement / 10 + 1;
                container.addReinforcement(notOwnedRegions.get(0).getMyNeighbors().get(0), army);
                restOfReinforcement -= army;
            } else if (notOwnedRegionCount < 5) {
                for (Region notOwnedRegion : notOwnedRegions) {
                    for (Region myNeighbors : notOwnedRegion.getMyNeighbors()) {
                        container.addPriority(myNeighbors, 5 - notOwnedRegionCount);
                    }
                }
            }
        }
        return restOfReinforcement;
    }

    private void setPriorityBySuperBorder(final List<Region> regions) {
        for (Region region : regions) {
            if (region.isSuperBorder()) {
                if (region.getSuperRegion().isMy()) {
                    int armyBenefit = region.getArmy() - region.getNeighborEnemyArmy();
                    container.addPriority(region, armyBenefit < 0 ? -armyBenefit : 2);
                } else {
                    container.addPriority(region, 1);
                }
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

    private int calculateMinArmy(final List<Region> regions) {
        int minArmy = -1;
        for (Region region : regions) {
            if (minArmy == -1 || region.getArmy() < minArmy) {
                minArmy = region.getArmy();
            }
        }
        return minArmy;
    }
}