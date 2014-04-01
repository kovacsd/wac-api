package kr.scramban.wac.parser;

public enum OutputOrder {

    PLACE_ARMIES("place_armies"),
    ATTACK_TRANSFER("attack/transfer");

    private final String[] orderKeys;

    private OutputOrder(final String... orderKeys) {
        this.orderKeys = orderKeys;
    }

    public String printOrder(final String player, final int... args) {
        StringBuilder response = new StringBuilder();
        response.append(player);
        for (String orderKey : orderKeys) {
            response.append(" ");
            response.append(orderKey);
        }
        for (int arg : args) {
            response.append(" ");
            response.append(arg);
        }
        return response.toString();
    }
}
