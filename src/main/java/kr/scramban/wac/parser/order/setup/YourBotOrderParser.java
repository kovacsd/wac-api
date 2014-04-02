package kr.scramban.wac.parser.order.setup;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.parser.order.OrderParser;

public class YourBotOrderParser implements OrderParser {

    private final GameContext context;

    public YourBotOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        context.getPlayerList().addMe(args[0][0]);
        return null;
    }
}