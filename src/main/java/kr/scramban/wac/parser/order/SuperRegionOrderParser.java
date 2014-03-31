package kr.scramban.wac.parser.order;

import static kr.scramban.wac.util.ParserUtil.toInt;
import kr.scramban.wac.domain.GameContext;

public class SuperRegionOrderParser implements OrderParser {

    @Override
    public String parse(final GameContext context, final String[][] args) {
        for (String[] superRegionArgs : args) {
            context.getWorld().addSuperRegion(toInt(superRegionArgs[0]), toInt(superRegionArgs[1]));
        }
        return null;
    }
}