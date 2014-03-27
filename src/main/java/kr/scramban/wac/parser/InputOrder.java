package kr.scramban.wac.parser;

public enum InputOrder {

    SETTINGS_YOUR_BOT(1, "settings", "your_bot"),
    SETTINGS_OPPONENT_BOT(1, "settings", "opponent_bot"),
    SETTINGS_STARTING_ARMIES(1, "settings", "starting_armies"),
    SETUP_MAP_SUPER_REGIONS(2, "setup_map", "super_regions"),
    SETUP_MAP_REGIONS(2, "setup_map", "regions"),
    SETUP_MAP_NEIGHBORS(2, "setup_map", "neighbors"),
    PICK_STARTING_REGIONS(0, "pick_starting_regions"),
    UPDATE_MAP(3, "update_map");

    private final String[] orderKeys;
    private final int offset;
    private final int size;

    private InputOrder(final int size, final String... orderKeys) {
        this.orderKeys = orderKeys;
        offset = orderKeys.length;
        this.size = size;
    }

    public String[][] paramParser(final String[] param) {
        String[][] result;
        if (size > 0) {
            result = new String[(param.length - offset) / size][];
            for (int i = 0; i < result.length; i++) {
                result[i] = new String[size];
                for (int j = 0; j < size; j++) {
                    result[i][j] = param[offset + i * size + j];
                }
            }
        } else {
            result = new String[1][];
            result[0] = new String[param.length - offset];
            for (int i = offset; i < result[0].length; i++) {
                result[0][i] = param[offset + i];
            }
        }
        return result;
    }

    private boolean verifyOrder(final String... orderKeys) {
        for (int i = 0; i < orderKeys.length && i < this.orderKeys.length; i++) {
            if (orderKeys[i] != this.orderKeys[i]) {
                return false;
            }
        }
        return true;
    }

    public static InputOrder getByOrderKeys(final String... orderKeys) {
        for (InputOrder order : values()) {
            if (order.verifyOrder(orderKeys)) {
                return order;
            }
        }
        throw new IllegalArgumentException("No order like this!\n" + orderKeys);
    }
}
