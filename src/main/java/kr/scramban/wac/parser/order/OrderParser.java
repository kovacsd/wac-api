package kr.scramban.wac.parser.order;

import kr.scramban.wac.domain.GameContext;

public interface OrderParser {

    void parse(GameContext context, String... args);
}
