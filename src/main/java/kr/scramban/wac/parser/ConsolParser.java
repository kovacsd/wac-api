package kr.scramban.wac.parser;

import java.util.HashMap;
import java.util.Map;

import kr.scramban.wac.domain.GameContext;
import kr.scramban.wac.parser.order.AttackTransferOrderParser;
import kr.scramban.wac.parser.order.DummyOrderParser;
import kr.scramban.wac.parser.order.NeighborsOrderParser;
import kr.scramban.wac.parser.order.OpponentBotOrderParser;
import kr.scramban.wac.parser.order.OrderParser;
import kr.scramban.wac.parser.order.PlaceArmiesOrderParser;
import kr.scramban.wac.parser.order.RegionOrderParser;
import kr.scramban.wac.parser.order.StartingArmiesOrderParser;
import kr.scramban.wac.parser.order.StartingRegionsOrderParser;
import kr.scramban.wac.parser.order.SuperRegionOrderParser;
import kr.scramban.wac.parser.order.UpdateMapOrderParser;
import kr.scramban.wac.parser.order.YourBotOrderParser;

public class ConsolParser {

    private final GameContext gameContext = new GameContext();

    private final Map<InputOrder, OrderParser> parsers = new HashMap<InputOrder, OrderParser>();

    {
        parsers.put(InputOrder.SETTINGS_OPPONENT_BOT, new OpponentBotOrderParser(gameContext));
        parsers.put(InputOrder.SETTINGS_YOUR_BOT, new YourBotOrderParser(gameContext));
        parsers.put(InputOrder.SETUP_MAP_SUPER_REGIONS, new SuperRegionOrderParser(gameContext));
        parsers.put(InputOrder.SETUP_MAP_REGIONS, new RegionOrderParser(gameContext));
        parsers.put(InputOrder.SETUP_MAP_NEIGHBORS, new NeighborsOrderParser(gameContext));
        parsers.put(InputOrder.PICK_STARTING_REGIONS, new StartingRegionsOrderParser());
        parsers.put(InputOrder.SETTINGS_STARTING_ARMIES, new StartingArmiesOrderParser(gameContext));
        parsers.put(InputOrder.UPDATE_MAP, new UpdateMapOrderParser(gameContext));
        parsers.put(InputOrder.GO_PLACE_ARMIES, new PlaceArmiesOrderParser(gameContext));
        parsers.put(InputOrder.GO_ATTACK_TRANSFER, new AttackTransferOrderParser(gameContext));
        parsers.put(InputOrder.OPPONENT_MOVES, new DummyOrderParser());
    }

    public String parseLine(final String order) {
        String[] parts = order.split(" ");
        InputOrder inputOrder = InputOrder.getByOrderKeys(parts);
        OrderParser parser = parsers.get(inputOrder);
        if (parser == null) {
            throw new IllegalArgumentException("No parser for : " + inputOrder);
        }
        return parser.parse(inputOrder.paramParser(parts));
    }
}
