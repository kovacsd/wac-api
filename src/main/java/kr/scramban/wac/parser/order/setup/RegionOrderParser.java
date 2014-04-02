package kr.scramban.wac.parser.order.setup;

import static kr.scramban.wac.util.ParserUtil.toInt;
import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.parser.order.OrderParser;

public class RegionOrderParser implements OrderParser {

    private final GameContext context;

    public RegionOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        for (String[] regionArgs : args) {
            context.getWorld().addRegion(toInt(regionArgs[0]), toInt(regionArgs[1]));
        }
        return null;
    }
}