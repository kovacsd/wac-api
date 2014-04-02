package kr.scramban.wac.parser.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.OutputOrder;

public class PlaceArmiesContainer {

    private final Map<Region, Integer> reinforcement = new HashMap<Region, Integer>();
    private final String name;

    public PlaceArmiesContainer(final String name) {
        this.name = name;
    }

    public void addReinforcement(final Region region, final int army) {
        int currentReinforcement = 0;
        if (reinforcement.containsKey(region)) {
            currentReinforcement = reinforcement.get(region);
        }
        reinforcement.put(region, currentReinforcement + army);
        region.addArmy(army);
    }

    public String printReinforcement() {
        StringBuilder response = new StringBuilder();
        for (Entry<Region, Integer> entry : reinforcement.entrySet()) {
            if (response.length() > 0) {
                response.append(",");
            }
            response.append(OutputOrder.PLACE_ARMIES.printOrder(name, entry.getKey().getId(), entry.getValue()));
        }
        return response.toString();
    }
}
