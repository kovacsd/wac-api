package kr.scramban.wac.parser.order;

import kr.scramban.wac.domain.GameContext;

public interface OrderParser {

    String parse(GameContext context, String[][] args);
}
