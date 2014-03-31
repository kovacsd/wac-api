package kr.scramban.wac.parser.order;

import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;

public class PlaceArmiesOrderParser implements OrderParser {

    @Override
    public String parse(final GameContext context, final String[][] args) {
        StringBuilder response = new StringBuilder();
        int fullReinforcement = context.getArmyCount().getReinforcement();
        List<Region> regions = context.getWorld().getMyBorderRegions();
        String myName = context.getPlayerList().getMyName();
        int portionOfReinforcement = fullReinforcement / regions.size();
        int restOfReinforcement = fullReinforcement % regions.size();
        for (Region region : regions) {
            int reinforcement = portionOfReinforcement;
            if (restOfReinforcement > 0) {
                reinforcement++;
                restOfReinforcement--;
            }
            region.addArmy(reinforcement);
            if (response.length() > 0) {
                response.append(",");
            }
            response.append(myName);
            response.append(" place_armies ");
            response.append(region.getId());
            response.append(" ");
            response.append(reinforcement);
        }
        return response.toString();
    }
}