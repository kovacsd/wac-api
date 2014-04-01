package kr.scramban.wac.parser.order;

import static kr.scramban.wac.util.ParserUtil.toInt;
import kr.scramban.wac.domain.GameContext;

public class NeighborsOrderParser implements OrderParser {

    private final GameContext context;

    public NeighborsOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        for (String[] regionArgs : args) {
            context.getWorld().addNeighbors(toInt(regionArgs[0]), toInt(regionArgs[1].split(",")));
        }
        return null;
    }
}