import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;

public class Day21 {

    static int dice = 0, rollCount = 0;
    static Map<Integer,Integer> uniqueSteps = new HashMap<Integer, Integer>();

    static int[] readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int player = 1;
        int[] positions = new int[3];
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            String[] tokens = line.split("\\s+");
            positions[player] = Integer.valueOf(tokens[4]);
            System.out.println("Starting position for player " + player + " is: " + positions[player]);
            player++;
        }
        return positions;
    }

    /*
     * First star problem
     */

    static int rollDice() {
        rollCount++;
        if (++dice == 101) {
            dice = 1;
        }
        return dice;
    }

    static void moveDiceSteps(int[] positions, int[] scores, int player) {
        int steps = 0;
        String roll = "";
        for (int i = 0; i < 3; i++) {
            int one = rollDice();
            steps += one;
            roll += one + "+";
        }
        int from = positions[player];
        int next = from + steps;
        if (next > 10) {
            next = next % 10;
            if (next == 0) {
                next = 10;
            }
        }
        positions[player] = next;
        scores[player] += next;
//        System.out.println("Player " + player + " rolls " + roll + " and moves to space " + next + " for a total score of " + scores[player] + ", roll: " + rollCount);
    }

    static int play_alt(int[] positions, int[] scores) {
        while(true) {
            for (int player = 1; player < 3; player++) {
                moveDiceSteps(positions, scores, player);
                if (scores[player] >= 1000) {
                    System.out.println("Roll count: " + rollCount);
                    System.out.println("Loser score: " + scores[3 - player]);
                    return rollCount * scores[3 - player];
                }
            }
        }
    }

    static int play(int[] positions, int[] scores, int player) {
        moveDiceSteps(positions, scores, player);
        if (scores[player] >= 1000) {
            System.out.println("Roll count: " + rollCount);
            System.out.println("Loser score: " + scores[3 - player]);
            return rollCount * scores[3 - player];
        }
        return play(positions, scores, 3 - player);
    }

    /*
     * Second star problem
     */

    static void createUniqueSteps() {
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                for (int k = 1; k < 4; k++) {
                    int diceSum = i + j + k;
                    uniqueSteps.put(diceSum, uniqueSteps.getOrDefault(diceSum, 0) + 1);
                }
            }
        }
        System.out.println("Unique steps: " + uniqueSteps);
    }

    static void moveSteps(int[] positions, int[] scores, int player, int steps) {
        int next = positions[player] + steps;
        if (next > 10) {
            next = next % 10;
            if (next == 0) {
                next = 10;
            }
        }
        positions[player] = next;
        scores[player] += next;
    }

    static long[] playHard(int[] positions, int[] scores, int player) {
        long[] allWins = new long[3];
        for (Map.Entry<Integer,Integer> entry : uniqueSteps.entrySet()) {
            int steps = entry.getKey();
            int count = entry.getValue();
            int[] newPositions = positions.clone();
            int[] newScores = scores.clone();
            moveSteps(newPositions, newScores, player, steps);
            if (newScores[player] >= 21) {
                allWins[player] += count;
            } else {
                long[] wins = playHard(newPositions, newScores, 3 - player);
                allWins[1] += wins[1] * count;
                allWins[2] += wins[2] * count;
            }
        }
        return allWins;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 21");
        int[] positions = readFile(new File("data/day21.txt"));
        int[] scores = new int[3];
        System.out.println("First star result: " + play(positions, scores, 1));
//        System.out.println("Result (Alt): " + play_alt(positions, scores));

        positions = readFile(new File("data/day21.txt"));
        createUniqueSteps();
        scores = new int[3];
        long[] wins = playHard(positions, scores, 1);
        for (int player = 1; player < 3; player++) {
            System.out.println("Player " + player + " wins: " + wins[player]);
        }
    }
}

