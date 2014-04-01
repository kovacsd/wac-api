package kr.scramban.wac.parser.order;


public class StartingRegionsOrderParser implements OrderParser {

    @Override
    public String parse(final String[][] args) {
        StringBuilder response = new StringBuilder();
        for (int i = 1; i < ((args[0].length - 1) / 2) + 1; i++) {
            if (response.length() > 0) {
                response.append(" ");
            }
            response.append(args[0][i]);
        }
        return response.toString();
    }
}