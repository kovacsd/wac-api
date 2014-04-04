package kr.scramban.wac.parser.order.move;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.order.OrderParser;
import kr.scramban.wac.parser.order.OutputOrder;

public class AttackTransferOffensiveOrderParser implements OrderParser {

    private final static Comparator<Region> ATTACK_PRIORITY_COMPARATOR = new Comparator<Region>() {
        @Override
        public int compare(final Region region1, final Region region2) {
            boolean oneWayAttack1 = region1.getElseNeighbors().size() == 1;
            boolean oneWayAttack2 = region2.getElseNeighbors().size() == 1;
            if (oneWayAttack1 ^ oneWayAttack2) {
                return oneWayAttack1 ? -1 : 1;
            } else if (region1.getArmy() == region2.getArmy()) {
                return 0;
            } else {
                return region1.getArmy() > region2.getArmy() ? -1 : 1;
            }
        }
    };

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
        List<Region> targetRegions = context.getWorld().getOutOfBorderRegions();
        for (Region targetRegion : targetRegions) {
            int neededArmy = (int) (targetRegion.getArmy() * 1.5 + 1);
            if (targetRegion.getMyNeighborArmy() > neededArmy) {
                int usedArmy = 0;
                for (Region attackPossition : getMyAttackPossitionsByPriority(targetRegion)) {
                    int army;
                    if (attackPossition.getElseNeighbors().size() == 1) {
                        army = attackPossition.getArmy() - 1;
                    } else if ((neededArmy - usedArmy) <= 0) {
                        break;
                    } else if ((neededArmy - usedArmy) * 1.5 < attackPossition.getArmy()) {
                        army = (int) ((neededArmy - usedArmy) * 1.3);
                    } else if (neededArmy - usedArmy < attackPossition.getArmy()) {
                        army = neededArmy - usedArmy;
                    } else {
                        army = attackPossition.getArmy() - 1;
                    }
                    usedArmy += army;
                    attackPossition.addArmy(-army);
                    generateResponse(response, attackPossition, targetRegion, army);
                }
            }
        }
    }

    private List<Region> getMyAttackPossitionsByPriority(final Region region) {
        List<Region> myNeighbors = region.getMyNeighbors();
        Collections.sort(myNeighbors, ATTACK_PRIORITY_COMPARATOR);
        return myNeighbors;
    }

    private void generateResponse(final StringBuilder response, final Region source, final Region target, final int army) {
        if (response.length() > 0) {
            response.append(",");
        }
        response.append(OutputOrder.ATTACK_TRANSFER.printOrder(context.getPlayerList().getMyName(), source.getId(), target.getId(), army));
    }
}