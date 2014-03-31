package kr.scramban.wac.parser.order;

import kr.scramban.wac.domain.GameContext;

public class OpponentBotOrderParser implements OrderParser {
    @Override
    public String parse(final GameContext context, final String[][] args) {
        context.getPlayerList().addEnemy(args[0][0]);
        return null;
    }
}