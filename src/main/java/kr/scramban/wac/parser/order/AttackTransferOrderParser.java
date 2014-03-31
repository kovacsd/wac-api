package kr.scramban.wac.parser.order;

import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;

public class AttackTransferOrderParser implements OrderParser {

    @Override
    public String parse(final GameContext context, final String[][] args) {
        StringBuilder response = new StringBuilder();
        response.append(createMoves(context));
        response.append(createAttacks(context));
        return response.length() > 0 ? response.toString() : "No moves";
    }

    private String createMoves(final GameContext context) {
        StringBuilder response = new StringBuilder();
        List<Region> regions = context.getWorld().getMyHinterlandRegions();
        String myName = context.getPlayerList().getMyName();
        for (Region region : regions) {
            if (region.getArmy() > 1) {
                long army = region.getArmy() - 1;
                for (Region neighbor : region.getNeighbor()) {
                    if (!neighbor.isHinterland()) {
                        region.addArmy(-army);
                        neighbor.addArmy(army);
                        if (response.length() > 0) {
                            response.append(",");
                        }
                        response.append(myName);
                        response.append(" attack/transfer ");
                        response.append(region.getId());
                        response.append(" ");
                        response.append(neighbor.getId());
                        response.append(" ");
                        response.append(army);
                        break;
                    }
                }
                if (region.getArmy() > 1) {
                    for (Region neighbor : region.getNeighbor()) {
                        boolean longWay = false;
                        for (Region neighborOfNeighbor : neighbor.getNeighbor()) {
                            if (!neighborOfNeighbor.isHinterland()) {
                                longWay = true;
                            }
                        }
                        if (longWay) {
                            region.addArmy(-army);
                            neighbor.addArmy(army);
                            if (response.length() > 0) {
                                response.append(",");
                            }
                            response.append(myName);
                            response.append(" attack/transfer ");
                            response.append(region.getId());
                            response.append(" ");
                            response.append(neighbor.getId());
                            response.append(" ");
                            response.append(army);
                        }
                    }
                }
            }
        }
        return response.toString();
    }

    private String createAttacks(final GameContext context) {
        StringBuilder response = new StringBuilder();
        List<Region> regions = context.getWorld().getMyBorderRegions();
        String myName = context.getPlayerList().getMyName();
        for (Region region : regions) {
            if (region.getArmy() > 1) {
                for (Region neighbor : region.getNeighbor()) {
                    long army = (long) (neighbor.getArmy() * 1.3 + 1);
                    if (!neighbor.getOwner().isMe() && army < region.getArmy() - 1) {
                        if (army * 1.1 + 1 < region.getArmy() - 1) {
                            army = (long) (army * 1.1 + 1);
                        }
                        region.addArmy(-army);
                        if (response.length() > 0) {
                            response.append(",");
                        }
                        response.append(myName);
                        response.append(" attack/transfer ");
                        response.append(region.getId());
                        response.append(" ");
                        response.append(neighbor.getId());
                        response.append(" ");
                        response.append(army);
                    }
                }
            }
        }
        return response.toString();
    }
}