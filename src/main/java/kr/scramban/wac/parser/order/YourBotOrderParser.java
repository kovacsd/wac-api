package kr.scramban.wac.parser.order;

import kr.scramban.wac.domain.GameContext;

public class YourBotOrderParser implements OrderParser {

    @Override
    public String parse(final GameContext context, final String[][] args) {
        context.getPlayerList().addMe(args[0][0]);
        return null;
    }
}