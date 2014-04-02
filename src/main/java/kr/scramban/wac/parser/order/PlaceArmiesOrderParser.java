package kr.scramban.wac.parser.order;

import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.container.PlaceArmiesContainer;

public class PlaceArmiesOrderParser implements OrderParser {

    private final GameContext context;
    private PlaceArmiesContainer container;

    public PlaceArmiesOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        container = new PlaceArmiesContainer(context.getPlayerList().getMyName());
        sendArmy(context.getArmyCount().getReinforcement(), context.getWorld().getMyBorderRegions());
        return container.printReinforcement();
    }

    private void sendArmy(final int fullReinforcement, final List<Region> myBorderRegions) {
        int restOfReinforcement = sendOffensiveArmy(myBorderRegions, fullReinforcement);
        restOfReinforcement = sendInSuperBorder(myBorderRegions, restOfReinforcement);
        restOfReinforcement = sendInBalance(myBorderRegions, restOfReinforcement);
        restOfReinforcement = sendInBalance(myBorderRegions, restOfReinforcement);
        sendAll(myBorderRegions, restOfReinforcement);
    }

    private int sendOffensiveArmy(final List<Region> regions, final int fullReinforcement) {
        int averageArmyOnBorder = calculateAverageArmy(regions);
        int restOfReinforcement = fullReinforcement;
        if (averageArmyOnBorder < 5) {
            for (Region region : regions) {
                if (restOfReinforcement > 4 && region.getArmy() < 5) {
                    int army = 5 - region.getArmy();
                    container.addReinforcement(region, army);
                    restOfReinforcement -= army;
                }
            }
        }
        return restOfReinforcement;
    }

    private int sendInSuperBorder(final List<Region> regions, final int fullReinforcement) {
        int portionOfReinforcement = fullReinforcement / regions.size() / 2;
        if (portionOfReinforcement == 0 && fullReinforcement > regions.size()) {
            portionOfReinforcement = 1;
        }
        int restOfReinforcement = fullReinforcement;
        if (portionOfReinforcement > 0) {
            for (Region region : regions) {
                if (region.isSuperBorder()) {
                    container.addReinforcement(region, portionOfReinforcement);
                    restOfReinforcement -= portionOfReinforcement;
                }
            }
        }
        return restOfReinforcement;
    }

    private int sendInBalance(final List<Region> regions, final int fullReinforcement) {
        int portionOfReinforcement = fullReinforcement / regions.size();
        int restOfReinforcement = fullReinforcement % regions.size();
        if (portionOfReinforcement > 0) {
            int averageArmyOnBorder = calculateAverageArmy(regions);
            for (Region region : regions) {
                if (region.getArmy() < averageArmyOnBorder * 1.5) {
                    container.addReinforcement(region, portionOfReinforcement);
                } else {
                    restOfReinforcement += portionOfReinforcement;
                }
            }
        }
        return restOfReinforcement;
    }

    private void sendAll(final List<Region> regions, final int fullReinforcement) {
        int portionOfReinforcement = fullReinforcement / regions.size();
        int restOfReinforcement = fullReinforcement % regions.size();
        for (Region region : regions) {
            int reinforcement = portionOfReinforcement;
            if (restOfReinforcement > 0) {
                reinforcement++;
                restOfReinforcement--;
            }
            if (reinforcement > 0) {
                container.addReinforcement(region, reinforcement);
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