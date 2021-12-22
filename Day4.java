import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Day4 {

    static List<Integer> numbers = new ArrayList<Integer>();
    static List<int[][]> bingo = new ArrayList<int[][]>();
    static List<boolean[][]> drawn;

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int[][] board = null;
        int row = 0;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            if (numbers.size() == 0) {
                String[] tokens = line.split(",");
                for (String token : tokens) {
                    numbers.add(Integer.valueOf(token));
                }
            } else if (line.length() == 0) {
                // New board
                if (board != null) {
                    bingo.add(board);
                }
                board = new int[5][5];
                row = 0;
            } else {
                String[] tokens = line.trim().split("\\s+");
                for (int col = 0; col < 5; col++) {
                    board[row][col] = Integer.valueOf(tokens[col]);
                }
                row++;
            }
        }
        bingo.add(board);
        System.out.println(numbers.size() + " numbers: " + numbers);
    }

    static void showVector(int[] vec) {
        for (int value : vec) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    static void showBingo() {
        for (int[][] board : bingo) {
            System.out.println();
            for (int i = 0; i < board.length; i++) {
                showVector(board[i]);
            }
        }
    }

    static void markBoards(int num) {
        for (int i = 0; i < bingo.size(); i++) {
            int[][] board = bingo.get(i);
            boolean[][] marked = drawn.get(i);
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (board[row][col] == num) {
                        marked[row][col] = true;
                    }
                }
            }
        }
    }

    static Set<Integer> won(boolean[] alreadyWon) {
        Set<Integer> winners = new HashSet<Integer>();
        for (int i = 0; i < bingo.size(); i++) {
            if (alreadyWon[i]) {
                continue;
            }
            int[][] board = bingo.get(i);
            boolean[][] marked = drawn.get(i);
            for (int row = 0; row < 5; row++) {
                boolean all = true;
                for (int col = 0; col < 5; col++) {
                    if (!marked[row][col]) {
                        all = false;
                    }
                }
                if (all) {
                    alreadyWon[i] = true;
                    winners.add(i);
                    break;
                }
            }
            for (int col = 0; col < 5; col++) {
                boolean all = true;
                for (int row = 0; row < 5; row++) {
                    if (!marked[row][col]) {
                        all = false;
                    }
                }
                if (all) {
                    alreadyWon[i] = true;
                    winners.add(i);
                    break;
                }
            }
        }
        return winners;
    }

    static int score(int boardIndex) {
        int[][] board = bingo.get(boardIndex);
        boolean[][] marked = drawn.get(boardIndex);
        int sum = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (!marked[row][col]) {
                    sum += board[row][col];
                }
            }
        }
        return sum;
    }

    static int play() {
        drawn = new ArrayList<boolean[][]>();
        for (int[][] board : bingo) {
            drawn.add(new boolean[5][5]);
        }
        boolean[] alreadyWon = new boolean[bingo.size()];
        for (int num : numbers) {
            markBoards(num);
            Set<Integer> winners = won(alreadyWon);
            if (winners.size() == 0) {
                continue;
            }
            int i = winners.iterator().next();
            System.out.println("Board " + i + " (out of " + bingo.size() + ") won");
            return score(i) * num;
        }
        return 0;
    }

    static int playHard() {
        drawn = new ArrayList<boolean[][]>();
        for (int[][] board : bingo) {
            drawn.add(new boolean[5][5]);
        }
        boolean[] alreadyWon = new boolean[bingo.size()];
        int score = 0;
        for (int num : numbers) {
            markBoards(num);
            Set<Integer> winners = won(alreadyWon);
            if (winners.size() == 0) {
                continue;
            }
            score = score(winners.iterator().next()) * num;
            System.out.println("Boards " + winners + " won with score " + score);
        }
        return score;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 4");
        readFile(new File("data/day4.txt"));
//        showBingo();
        System.out.println("First star score: " + play());
        System.out.println("Second star score: " + playHard());
    }
}
