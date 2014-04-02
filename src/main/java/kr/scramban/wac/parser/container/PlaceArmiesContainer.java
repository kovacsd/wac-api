package kr.scramban.wac.parser.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.OutputOrder;

public class PlaceArmiesContainer {

    private final Map<Region, Integer> reinforcementMap = new HashMap<Region, Integer>();
    private final Map<Region, Integer> priorityMap = new HashMap<Region, Integer>();

    private final String name;

    public PlaceArmiesContainer(final String name) {
        this.name = name;
    }

    public void addReinforcement(final Region region, final int reinforcement) {
        int currentReinforcement = 0;
        if (reinforcementMap.containsKey(region)) {
            currentReinforcement = reinforcementMap.get(region);
        }
        reinforcementMap.put(region, currentReinforcement + reinforcement);
        region.addArmy(reinforcement);
    }

    public void addPriority(final Region region, final int priority) {
        int currentPriority = 0;
        if (priorityMap.containsKey(region)) {
            currentPriority = priorityMap.get(region);
        }
        priorityMap.put(region, currentPriority + priority);
    }

    public void divideByPriority(final int reinforcement) {
        int sumOfPriority = 0;
        for (int priority : priorityMap.values()) {
            sumOfPriority += priority;
        }
        int actualReinforcement = reinforcement;
        for (Entry<Region, Integer> entry : priorityMap.entrySet()) {
            int reinforcementPart = reinforcement * entry.getValue() / sumOfPriority;
            addReinforcement(entry.getKey(), reinforcementPart);
            actualReinforcement -= reinforcement;
        }
        divideAll(priorityMap.keySet(), actualReinforcement);
    }

    private void divideAll(final Collection<Region> regions, final int reinforcement) {
        int portionOfReinforcement = reinforcement / regions.size();
        int restOfReinforcement = reinforcement % regions.size();
        for (Region region : regions) {
            int reinforcementPart = portionOfReinforcement;
            if (restOfReinforcement > 0) {
                reinforcementPart++;
                restOfReinforcement--;
            }
            if (reinforcementPart > 0) {
                addReinforcement(region, reinforcementPart);
            }
        }
    }

    public String printReinforcement() {
        StringBuilder response = new StringBuilder();
        for (Entry<Region, Integer> entry : reinforcementMap.entrySet()) {
            if (response.length() > 0) {
                response.append(",");
            }
            response.append(OutputOrder.PLACE_ARMIES.printOrder(name, entry.getKey().getId(), entry.getValue()));
        }
        return response.toString();
    }
}
