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
        int restOfReinforcement = sendInBalance(response, regions, fullReinforcement);
        restOfReinforcement = sendInBalance(response, regions, restOfReinforcement);
        sendAll(response, regions, restOfReinforcement);
        return response.toString();
    }

    private int sendInBalance(final StringBuilder response, final List<Region> regions, final int fullReinforcement) {
        int portionOfReinforcement = fullReinforcement / regions.size();
        int restOfReinforcement = fullReinforcement % regions.size();
        if (portionOfReinforcement > 0) {
            int averageArmyOnBorder = calculateAverageArmy(regions);
            for (Region region : regions) {
                if (region.getArmy() < averageArmyOnBorder * 1.3) {
                    region.addArmy(portionOfReinforcement);
                    generateResponse(response, region, portionOfReinforcement);
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
                generateResponse(response, region, reinforcement);
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

    private void generateResponse(final StringBuilder response, final Region region, final int reinforcement) {
        if (response.length() > 0) {
            response.append(",");
        }
        response.append(OutputOrder.PLACE_ARMIES.printOrder(context.getPlayerList().getMyName(), region.getId(), reinforcement));
    }
}