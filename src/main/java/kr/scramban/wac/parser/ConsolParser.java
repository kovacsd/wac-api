package kr.scramban.wac.parser;


public class ConsolParser {

    public String parseLine(final String order) {
        String[] parts = order.split(" ");
        InputOrder inputOrder = InputOrder.getByOrderKeys(parts);
        return null;
    }
}
