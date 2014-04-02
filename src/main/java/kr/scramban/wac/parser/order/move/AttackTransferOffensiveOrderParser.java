package kr.scramban.wac.parser.order.move;

import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.OutputOrder;
import kr.scramban.wac.parser.order.OrderParser;

public class AttackTransferOffensiveOrderParser implements OrderParser {

    private final GameContext context;

    public AttackTransferOffensiveOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        StringBuilder response = new StringBuilder();
        createMoves(response);
        createAttacks(response);
        return response.length() > 0 ? response.toString() : "No moves";
    }

    private void createMoves(final StringBuilder response) {
        List<Region> regions = context.getWorld().getMyHinterlandRegions();
        for (Region region : regions) {
            if (region.getArmy() > 1) {
                int army = region.getArmy() - 1;
                Region neighbor = region.getNeighborWithLowestHinterlandCount();
                if (neighbor != null) {
                    generateResponse(response, region, neighbor, army);
                }
            }
        }
    }

    private void createAttacks(final StringBuilder response) {
        List<Region> regions = context.getWorld().getOutOfBorderRegions();
        for (Region region : regions) {
            if (region.getNeighborMyArmy() > region.getArmy() * 1.3 + 2) {
                List<Region> myNeighbors = region.getMyNeighbors();
                for (Region myNeighbor : myNeighbors) {
                    int neededArmy = (int) (myNeighbor.getArmy() * 1.3 + 2);
                    if (neededArmy < region.getArmy()) {
                        int army;
                        if (myNeighbors.size() == 1) {
                            army = region.getArmy() - 1;
                        } else if (neededArmy * 1.5 < region.getArmy()) {
                            army = (int) (neededArmy * 1.3);
                        } else {
                            army = neededArmy;
                        }
                        region.addArmy(-army);
                        generateResponse(response, region, myNeighbor, army);
                    }
                }
            }
        }
    }

    private void generateResponse(final StringBuilder response, final Region region, final Region neighbor, final int army) {
        if (response.length() > 0) {
            response.append(",");
        }
        response.append(OutputOrder.ATTACK_TRANSFER.printOrder(context.getPlayerList().getMyName(), region.getId(), neighbor.getId(), army));
    }
}