package kr.scramban.wac.main;

import java.util.Scanner;

import kr.scramban.wac.parser.ConsolParser;

public class Starter {

    public static void main(final String[] args) {
        Scanner scan = new Scanner(System.in);
        ConsolParser parser = new ConsolParser();
        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            if (line.length() > 0) {
                String response = parser.parseLine(line);
                if (response != null) {
                    System.out.println(response);
                }
            }
        }
    }
}
