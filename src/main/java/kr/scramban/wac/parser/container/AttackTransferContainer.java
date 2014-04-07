package kr.scramban.wac.parser.container;

import java.util.ArrayList;
import java.util.List;

import kr.scramban.wac.domain.map.Region;
import kr.scramban.wac.parser.order.OutputOrder;

public class AttackTransferContainer {

    private final List<AttackTransferEvent> attackTransferEvents = new ArrayList<AttackTransferEvent>();
    private final String name;

    public AttackTransferContainer(final String name) {
        this.name = name;
    }

    public AttackTransferEvent generateEvent(final Region source, final Region target, final int army) {
        return new AttackTransferEvent(source, target, army);
    }

    public void addEvent(final Region source, final Region target, final int army) {
        attackTransferEvents.add(generateEvent(source, target, army));
    }

    public void addEvents(final List<AttackTransferEvent> events) {
        attackTransferEvents.addAll(events);
    }

    public String printAttackTransfer() {
        StringBuilder response = new StringBuilder();
        for (AttackTransferEvent event : attackTransferEvents) {
            if (response.length() > 0) {
                response.append(",");
            }
            response.append(OutputOrder.ATTACK_TRANSFER.printOrder(name, event));
        }
        return response.length() > 0 ? response.toString() : "No moves";
    }

    public final static class AttackTransferEvent {

        private final Region source;
        private final Region target;
        private final int army;

        private AttackTransferEvent(final Region source, final Region target, final int army) {
            this.source = source;
            this.target = target;
            this.army = army;
        }

        public Region getSource() {
            return source;
        }

        public Region getTarget() {
            return target;
        }

        public int getArmy() {
            return army;
        }
    }
}
