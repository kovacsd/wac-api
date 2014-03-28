package kr.scramban.wac.parser;

import java.util.HashMap;
import java.util.Map;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.parser.order.OrderParser;

public class ConsolParser {

    private final GameContext gameContext = new GameContext();

    private final Map<InputOrder, OrderParser> parsers = new HashMap<InputOrder, OrderParser>();

    {
        parsers.put(InputOrder.SETTINGS_OPPONENT_BOT, new OrderParser() {
            public String parse(final GameContext context, final String[][] args) {
                context.getPlayerList().addEnemy(args[0][0]);
                return null;
            }
        });
    }

    public String parseLine(final String order) {
        String[] parts = order.split(" ");
        InputOrder inputOrder = InputOrder.getByOrderKeys(parts);
        OrderParser parser = parsers.get(inputOrder);
        return parser.parse(gameContext, inputOrder.paramParser(parts));
    }
}
