package kr.scramban.wac.parser;

public enum OutputOrder {

    PLACE_ARMIES("place_armies"),
    ATTACK_TRANSFER("attack/transfer");

    private final String[] orderKeys;

    private OutputOrder(final String... orderKeys) {
        this.orderKeys = orderKeys;
    }
}
