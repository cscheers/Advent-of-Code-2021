import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Day23 {

    static char[][] burrow;
    static Point[][] amphipods;
    static int[] podCost = {1, 10, 100, 1000};
    static int moveCount, minSoFar;
    static Map<String, Integer> history = new HashMap<String, Integer>();

    static class Move {
        Move(Point pos, int cost) {
            this.pos = pos;
            this.cost = cost;
        }
        Point pos;
        int cost;
    }

    static void readFile(File file, boolean firstStar) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int row = 0;
        moveCount = 0;
        minSoFar = Integer.MAX_VALUE;
        burrow = null;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            if (burrow == null) {
                burrow = new char[firstStar ? 3 : 5][line.length() - 2];
            } else if (row < burrow.length) {
                if (firstStar && row == 2) {
                    line = fs.nextLine(); // Skip two lines
                    line = fs.nextLine();
                }
                for (int i = 0; i < line.length() - 2; i++) {
                    burrow[row][i] = line.charAt(i + 1);
                }                
                row++;
            }
        }
        burrow[2][9] = '#';
        if (!firstStar) {
            burrow[3][9] = '#';
            burrow[4][9] = '#';
        }
        amphipods = new Point[4][firstStar ? 2 : 4];
        getPositions();
    }

    static void getPositions() {
        for (int i = 0; i < burrow.length; i++) {
            for (int j = 0; j < burrow[i].length; j++) {
                int row = burrow[i][j] - 'A';
                if (row < 0 || row > 3) {
                    continue; // Must be dot ('.') or hash ('#')
                }
                Point[] pair = amphipods[row];
                for (int k = 0; k < 4; k++) {
                    if (pair[k] == null) {
                        pair[k] = new Point(i, j);
                        break;
                    }
                }
            }
        }
    }

    static void showBurrow(String message) {
        System.out.println(message);
        for (char[] row : burrow) {
            System.out.println(new String(row));
        }
        for (Point[] pair : amphipods) {
            for (int p = 0; p < pair.length; p++) {
                System.out.println(pair[p]);
            }
        }
    }

    static String getBurrowKey() {
        StringBuffer key = new StringBuffer();
        for (int row = 0; row < burrow.length; row++) {
            key.append(burrow[row]);
        }
        return key.toString();
    }

    static boolean home() { // Just checking top elements should be enough ?
        for (int row = 1; row < burrow.length; row++) {
            for (int col = 2; col <= 8; col += 2) {
                if (burrow[row][col] - 'A' != col / 2 - 1) {
                    return false;
                }
            }
        }
        return true;
    }

    static void moveOutsideRight(int pod, int col, int steps, List<Move> moves) {
        int cost = steps * podCost[pod];
        while (col < 10 && burrow[0][col] == '.') { // No blockage
            moves.add(new Move(new Point(0, col), cost));
            col += 2;
            cost += 2 * podCost[pod];
        }
        if (col == 11 && burrow[0][10] == '.') { // No blockage
            cost -= podCost[pod];
            moves.add(new Move(new Point(0, 10), cost));
        }
    }

    static void moveOutsideLeft(int pod, int col, int steps, List<Move> moves) {
        int cost = steps * podCost[pod];
        while (col > 0 && burrow[0][col] == '.') {
            moves.add(new Move(new Point(0, col), cost));
            col -= 2;
            cost += 2 * podCost[pod];
        }
        if (col == -1 && burrow[0][0] == '.') { // No blockage
            cost -= podCost[pod];
            moves.add(new Move(new Point(0, 0), cost));
        }
    }

    static void moveOutsideRoom(int pod, int col, int steps, List<Move> moves) {
        moveOutsideLeft(pod, col - 1, steps, moves);
        moveOutsideRight(pod, col + 1, steps, moves);
    }

    static boolean hallwayClear(int startCol, int targetCol) {
        int step = startCol < targetCol ? 1 : - 1;
        for (int col = startCol + step; col != targetCol; col += step) {            
            if (burrow[0][col] != '.') {
                return false; // Can not move into target room, hallway is blocked
            }
        }
        return true;
    }

    static void moveInsideRoom(int pod, int col, List<Move> moves) {
        int target = pod * 2 + 2;
        if (!hallwayClear(col, target)) {
            return;
        }
        int horizontalSteps = Math.abs(col - target);
        int row = burrow.length - 1;
        while (burrow[row][target] != '.') {
            if (burrow[row][target] - 'A' != pod) {
                return;
            }
            row--;
        }
        int cost = (horizontalSteps + row) * podCost[pod];
        moves.add(new Move(new Point(row, target), cost));
    }

    static void findPossibleMoves(Point pos, List<Move> moves) {
        char podName = burrow[pos.x][pos.y];
        int pod = podName - 'A';
        if (pos.x == 0) { // In the hallway, try to move into a room
            moveInsideRoom(pod, pos.y, moves);
            return;
        }
        // In a room, try to move out
        for (int row = 1; row < pos.x; row++) {
            if (burrow[row][pos.y] != '.') {
                return; // Blocked, can't move out
            }
        }
        moveOutsideRoom(pod, pos.y, pos.x + 1, moves);
    }

    static boolean rowsBelowMatch(Point pos, int pod) {
        for (int row = pos.x + 1; row < burrow.length; row++) {
            char nextPodName = burrow[row][pos.y];
            if (nextPodName - 'A' != pod) {
                return false;
            }
        }
        return true;
    }

    static void play(int cost) {
        moveCount++;
        if (cost > minSoFar) {
            return;
        }
        String key = getBurrowKey();
        Integer prevCost = history.get(key);
        if (prevCost != null && prevCost < cost) {
            return; //  Been here before at a lower cost
        }
        history.put(key, cost);
        if (home()) {
            if (cost < minSoFar) {
                System.out.println("Reached shorter path home: " + cost);
                minSoFar = cost;
            }
            return;
        }
        for (Point[] pair : amphipods) {
            for (int p = 0; p < pair.length; p++) {
                Point pos = pair[p];
                char podName = burrow[pos.x][pos.y];
                int pod = podName - 'A';
                if (pos.y - 2 == pod * 2 && rowsBelowMatch(pos, pod)) {
                    continue; // Correct column and rows below match, don't move this one
                }
                List<Move> moves = new ArrayList<Move>();
                findPossibleMoves(pos, moves);
                // Not much benefit from sorting these moves
                moves.sort((m1, m2) -> (m1.cost - m2.cost));
                for (Move move : moves) {
                    Point newPos = move.pos;
                    int extraCost = move.cost;
                    burrow[pos.x][pos.y] = '.';
                    burrow[newPos.x][newPos.y] = podName;
                    pair[p] = newPos;
                    play(cost + extraCost);
                    // Restore
                    burrow[newPos.x][newPos.y] = '.';
                    burrow[pos.x][pos.y] = podName;
                    pair[p] = pos;
                }
            }
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 23");
        readFile(new File("data/day23.txt"), true);
        showBurrow("Initial");
        play(0);
        System.out.println("Number of moves: " + moveCount);
        System.out.println("Number of board positions: " + history.size());

        readFile(new File("data/day23.txt"), false);
        showBurrow("Initial");
        play(0);
        System.out.println("Number of moves: " + moveCount);
        System.out.println("Number of board positions: " + history.size());
    }
}
