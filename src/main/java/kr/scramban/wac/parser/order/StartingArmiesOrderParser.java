package kr.scramban.wac.parser.order;

import static kr.scramban.wac.util.ParserUtil.toInt;
import kr.scramban.wac.domain.GameContext;

public class StartingArmiesOrderParser implements OrderParser {

    @Override
    public String parse(final GameContext context, final String[][] args) {
        context.getArmyCount().setReinforcement(toInt(args[0][0]));
        return null;
    }
}