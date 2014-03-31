package kr.scramban.wac.parser.order;

import kr.scramban.wac.domain.GameContext;

public class DummyOrderParser implements OrderParser {
    @Override
    public String parse(final GameContext context, final String[][] args) {
        return null;
    }
}