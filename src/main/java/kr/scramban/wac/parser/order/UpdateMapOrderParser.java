package kr.scramban.wac.parser.order;

import static kr.scramban.wac.util.ParserUtil.toInt;
import kr.scramban.wac.domain.GameContext;

public class UpdateMapOrderParser implements OrderParser {

    private final GameContext context;

    public UpdateMapOrderParser(final GameContext context) {
        this.context = context;
    }

    @Override
    public String parse(final String[][] args) {
        for (String[] mapArgs : args) {
            context.getWorld().setRegion(toInt(mapArgs[0]), context.getPlayerList().getPlayer(mapArgs[1]), toInt(mapArgs[2]));
        }
        return null;
    }
}