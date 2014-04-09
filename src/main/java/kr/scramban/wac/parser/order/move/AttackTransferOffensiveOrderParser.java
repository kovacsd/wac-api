package kr.scramban.wac.parser.order.move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.domain.map.SuperRegion;
import kr.scramban.wac.domain.player.PlayerType;
import kr.scramban.wac.parser.container.AttackTransferContainer;
import kr.scramban.wac.parser.container.AttackTransferContainer.AttackTransferEvent;
import kr.scramban.wac.parser.order.OrderParser;

public class AttackTransferOffensiveOrderParser implements OrderParser {

    private final Comparator<Region> attackPriorityComparator = new Comparator<Region>() {
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

    private final Comparator<Region> targetPriorityComparator = new Comparator<Region>() {
        @Override
        public int compare(final Region region1, final Region region2) {
            SuperRegion closestSuperRegion = context.getWorld().getClosestSuperRegion();
            boolean inClosestSuperRegion1 = closestSuperRegion.contains(region1);
            boolean inClosestSuperRegion2 = closestSuperRegion.contains(region2);
            if (inClosestSuperRegion1 ^ inClosestSuperRegion2) {
                return inClosestSuperRegion1 ? -1 : 1;
            } else {
                boolean ownedByEnemy1 = region1.getOwner().getType() == PlayerType.ENEMY;
                boolean ownedByEnemy2 = region2.getElseNeighbors().size() == 1;
                if (ownedByEnemy1 ^ ownedByEnemy2) {
                    return ownedByEnemy1 ? -1 : 1;
                } else if (region1.getArmy() == region2.getArmy()) {
                    return 0;
                } else {
                    return region1.getArmy() > region2.getArmy() ? -1 : 1;
                }
            }
        }
    };

    private final GameContext context;
    private AttackTransferContainer container;

    public AttackTransferOffensiveOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        container = new AttackTransferContainer(context.getPlayerList().getMyName());
        createMoves();
        createAttacks();
        return container.printAttackTransfer();
    }

    private void createMoves() {
        List<Region> regions = context.getWorld().getMyHinterlandRegions();
        for (Region region : regions) {
            if (region.getArmy() > 1) {
                int army = region.getArmy() - 1;
                Region neighbor = region.getNeighborWithLowestHinterlandCount();
                if (neighbor != null) {
                    container.addEvent(region, neighbor, army);
                }
            }
        }
    }

    private void createAttacks() {
        for (Region targetRegion : getTargetPossitionsByPriority()) {
            int neededArmy = (int) (targetRegion.getArmy() * 1.5 + 1);
            if (targetRegion.getMyNeighborArmy() > neededArmy) {
                List<AttackTransferEvent> events = new ArrayList<AttackTransferEvent>();
                int usedArmy = 0;
                for (Region attackPossition : getMyAttackPossitionsByPriority(targetRegion)) {
                    int enemyArmy = attackPossition.getNeighborEnemyArmy() - targetRegion.getArmy() + 1;
                    int usableArmy = attackPossition.getArmy() - (int) (enemyArmy * 0.9);
                    int army = 0;
                    if (usableArmy > 0) {
                        if (attackPossition.getElseNeighbors().size() == 1) {
                            army = attackPossition.getArmy() - 1;
                        } else if ((neededArmy - usedArmy) <= 0) {
                            break;
                        } else if ((neededArmy - usedArmy) * 1.7 < usableArmy) {
                            army = (int) ((neededArmy - usedArmy) * 1.5);
                        } else if (neededArmy - usedArmy < usableArmy) {
                            army = neededArmy - usedArmy;
                        } else {
                            army = usableArmy;
                        }
                    } else if (neededArmy - usedArmy < attackPossition.getArmy() / 10) {
                        army = neededArmy - usedArmy;
                    }
                    if (army > 0) {
                        usedArmy += army;
                        attackPossition.addArmy(-army);
                        events.add(container.generateEvent(attackPossition, targetRegion, army));
                    }
                }
                if (neededArmy <= usedArmy) {
                    container.addEvents(events);
                }
            }
        }
    }

    private List<Region> getTargetPossitionsByPriority() {
        List<Region> targetRegions = context.getWorld().getOutOfBorderRegions();
        Collections.sort(targetRegions, targetPriorityComparator);
        return targetRegions;
    }

    private List<Region> getMyAttackPossitionsByPriority(final Region region) {
        List<Region> myNeighbors = region.getMyNeighbors();
        Collections.sort(myNeighbors, attackPriorityComparator);
        return myNeighbors;
    }
}