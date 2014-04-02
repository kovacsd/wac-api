package kr.scramban.wac.parser.order.setup;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.parser.order.OrderParser;

public class OpponentBotOrderParser implements OrderParser {

    private final GameContext context;

    public OpponentBotOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        context.getPlayerList().addEnemy(args[0][0]);
        return null;
    }
}