import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class Day7 {

    static List<Integer> crabs = new ArrayList<Integer>();
    static int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(",");
            for (String token : tokens) {
                int value = Integer.valueOf(token);
                min = Math.min(min, value);
                max = Math.max(max, value);
                crabs.add(value);
            }
        }
    }

    static int movingCost(int steps, boolean firstStar) {
        if (firstStar) {
            return steps;
        }
        int cost = steps * steps / 2 + steps / 2;
        if (steps % 2 != 0) {
            cost++;
        }
        return cost;
    }

    static void play(boolean firstStar) {
        int minCost = Integer.MAX_VALUE;
        for (int i = min; i <= max; i++) {
            int cost = 0;
            for (int crab : crabs) {
                cost += movingCost(Math.abs(crab - i), firstStar);
            }
            minCost = Math.min(minCost, cost);
        }
        String level = firstStar ? "first" : "second";
        System.out.println("Minimum cost for " + level + " star is: " + minCost);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 7");
        readFile(new File("data/day7.txt"));
        play(true);
        play(false);
    }
}
