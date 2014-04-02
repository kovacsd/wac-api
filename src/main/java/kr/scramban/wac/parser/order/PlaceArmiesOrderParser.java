package kr.scramban.wac.parser.order;

import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.OutputOrder;

public class PlaceArmiesOrderParser implements OrderParser {

    private final GameContext context;

    public PlaceArmiesOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        StringBuilder response = new StringBuilder();
        int fullReinforcement = context.getArmyCount().getReinforcement();
        List<Region> regions = context.getWorld().getMyBorderRegions();
        int restOfReinforcement = sendOffensiveArmy(response, regions, fullReinforcement);
        restOfReinforcement = sendInSuperBorder(response, regions, restOfReinforcement);
        restOfReinforcement = sendInBalance(response, regions, restOfReinforcement);
        restOfReinforcement = sendInBalance(response, regions, restOfReinforcement);
        sendAll(response, regions, restOfReinforcement);
        return response.toString();
    }

    private int sendOffensiveArmy(final StringBuilder response, final List<Region> regions, final int fullReinforcement) {
        int averageArmyOnBorder = calculateAverageArmy(regions);
        int restOfReinforcement = fullReinforcement;
        if (averageArmyOnBorder < 5) {
            for (Region region : regions) {
                if (region.getArmy() < 5) {
                    int army = 5 - region.getArmy();
                    addReinforcement(response, region, army);
                    restOfReinforcement -= army;
                }
            }
        }
        return restOfReinforcement;
    }

    private int sendInSuperBorder(final StringBuilder response, final List<Region> regions, final int fullReinforcement) {
        int portionOfReinforcement = fullReinforcement / regions.size() / 2;
        if (portionOfReinforcement == 0 && fullReinforcement > regions.size()) {
            portionOfReinforcement = 1;
        }
        int restOfReinforcement = fullReinforcement;
        if (portionOfReinforcement > 0) {
            for (Region region : regions) {
                if (region.isSuperBorder()) {
                    addReinforcement(response, region, portionOfReinforcement);
                    restOfReinforcement -= portionOfReinforcement;
                }
            }
        }
        return restOfReinforcement;
    }

    private int sendInBalance(final StringBuilder response, final List<Region> regions, final int fullReinforcement) {
        int portionOfReinforcement = fullReinforcement / regions.size();
        int restOfReinforcement = fullReinforcement % regions.size();
        if (portionOfReinforcement > 0) {
            int averageArmyOnBorder = calculateAverageArmy(regions);
            for (Region region : regions) {
                if (region.getArmy() < averageArmyOnBorder * 1.5) {
                    addReinforcement(response, region, portionOfReinforcement);
                } else {
                    restOfReinforcement += portionOfReinforcement;
                }
            }
        }
        return restOfReinforcement;
    }

    private void sendAll(final StringBuilder response, final List<Region> regions, final int fullReinforcement) {
        int portionOfReinforcement = fullReinforcement / regions.size();
        int restOfReinforcement = fullReinforcement % regions.size();
        for (Region region : regions) {
            int reinforcement = portionOfReinforcement;
            if (restOfReinforcement > 0) {
                reinforcement++;
                restOfReinforcement--;
            }
            if (reinforcement > 0) {
                addReinforcement(response, region, reinforcement);
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

    private void addReinforcement(final StringBuilder response, final Region region, final int reinforcement) {
        region.addArmy(reinforcement);
        if (response.length() > 0) {
            response.append(",");
        }
        response.append(OutputOrder.PLACE_ARMIES.printOrder(context.getPlayerList().getMyName(), region.getId(), reinforcement));
    }
}