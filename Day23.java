import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Day23 {

    static char[][] burrow;
    static Point[][] amphipods;
    static int[] podCost = {1, 10, 100, 1000};
    static List<Integer> costHistory = new ArrayList<Integer>();
    static int moveCount, minSoFar;
    static Set<char[][]> history = new HashSet<char[][]>();

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

    static void moveOutsideRight(int pod, int col, int steps, List<Point> moves, List<Integer> costs) {
        int cost = steps * podCost[pod];
        while (col < 10 && burrow[0][col] == '.') { // No blockage
            moves.add(new Point(0, col));
            costs.add(cost);
            col += 2;
            cost += 2 * podCost[pod];
        }
        if (col == 11 && burrow[0][10] == '.') { // No blockage
            cost -= podCost[pod];
            moves.add(new Point(0, 10));
            costs.add(cost);
        }
    }

    static void moveOutsideLeft(int pod, int col, int steps, List<Point> moves, List<Integer> costs) {
        int cost = steps * podCost[pod];
        while (col > 0 && burrow[0][col] == '.') {
            moves.add(new Point(0, col));
            costs.add(cost);
            col -= 2;
            cost += 2 * podCost[pod];
        }
        if (col == -1 && burrow[0][0] == '.') { // No blockage
            cost -= podCost[pod];
            moves.add(new Point(0, 0));
            costs.add(cost);
        }
    }

    static void moveOutsideRoom(int pod, int col, int steps, List<Point> moves, List<Integer> costs) {
        moveOutsideLeft(pod, col - 1, steps, moves, costs);
        moveOutsideRight(pod, col + 1, steps, moves, costs);
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

    static void moveInsideRoom(int pod, int col, List<Point> moves, List<Integer> costs) {
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
        moves.add(new Point(row, target));
        costs.add(cost);
    }

    static void findPossibleMoves(Point pos, List<Point> moves, List<Integer> costs) {
        char podName = burrow[pos.x][pos.y];
        int pod = podName - 'A';
        if (pos.x == 0) { // In the hallway, try to move into a room
            moveInsideRoom(pod, pos.y, moves, costs);
            return;
        }
        // In a room, try to move out
        for (int row = 1; row < pos.x; row++) {
            if (burrow[row][pos.y] != '.') {
                return; // Blocked, can't move out
            }
        }
        moveOutsideRoom(pod, pos.y, pos.x + 1, moves, costs);
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
                List<Point> moves = new ArrayList<Point>();
                List<Integer> costs = new ArrayList<Integer>();
                findPossibleMoves(pos, moves, costs);
//                System.out.println("# possible moves for: " + podName + ": " + moves);
                for (int i = 0; i < moves.size(); i++) {
                    Point newPos = moves.get(i);
                    int extraCost = costs.get(i);
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

        readFile(new File("data/day23.txt"), false);
        showBurrow("Initial");
        play(0);
        System.out.println("Number of moves: " + moveCount);
    }
}
