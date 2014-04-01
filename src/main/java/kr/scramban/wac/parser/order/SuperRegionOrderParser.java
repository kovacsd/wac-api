package kr.scramban.wac.parser.order;

import static kr.scramban.wac.util.ParserUtil.toInt;
import kr.scramban.wac.domain.GameContext;

public class SuperRegionOrderParser implements OrderParser {

    private final GameContext context;

    public SuperRegionOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        for (String[] superRegionArgs : args) {
            context.getWorld().addSuperRegion(toInt(superRegionArgs[0]), toInt(superRegionArgs[1]));
        }
        return null;
    }
}