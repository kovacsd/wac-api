package kr.scramban.wac.parser.order;

import static kr.scramban.wac.util.ParserUtil.toInt;
import kr.scramban.wac.domain.GameContext;

public class RegionOrderParser implements OrderParser {

    @Override
    public String parse(final GameContext context, final String[][] args) {
        for (String[] regionArgs : args) {
            context.getWorld().addRegion(toInt(regionArgs[0]), toInt(regionArgs[1]));
        }
        return null;
    }
}