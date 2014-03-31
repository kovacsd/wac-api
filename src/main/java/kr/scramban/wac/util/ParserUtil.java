package kr.scramban.wac.util;

public final class ParserUtil {

    private ParserUtil() {
    }

    public static int toInt(final String arg) {
        return Integer.valueOf(arg).intValue();
    }

    public static int[] toInt(final String[] args) {
        int[] result = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = toInt(args[i]);
        }
        return result;
    }
}
