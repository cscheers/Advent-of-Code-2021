import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class Day23 {

    static char[][] burrow;
    static Point[][] amphipods;
    static int[] podCost = {1, 10, 100, 1000};
    static List<char[][]> history = new ArrayList<char[][]>();
    static List<Integer> costHistory = new ArrayList<Integer>();
    static int moveCount, minSoFar;

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

    static void moveOutsideRight(int pod, int col, int steps, List<Point> moves, List<Integer> costs) {
        int cost = steps * podCost[pod];
        while (col < 10) {
            if (burrow[0][col] == '.') {
//                System.out.println(pod + " can move to: (0, " + col + ") at cost: " + cost);
                moves.add(new Point(0, col));
                costs.add(cost);
                col += 2;
                cost += 2 * podCost[pod];
            } else {
//                System.out.println(pod + " can not move to: (0, " + col + "), occupied");
                break;
            }
        }
        if (col == 11) { // No blockage
            if (burrow[0][10] == '.') {
                cost -= podCost[pod];
//                System.out.println(pod + " can move to: (0, 10) at cost: " + cost);
                moves.add(new Point(0, 10));
                costs.add(cost);
            } else {
//                System.out.println(pod + " can not move to (0, 10), occupied" );
            }
        }
    }

    static void moveOutsideLeft(int pod, int col, int steps, List<Point> moves, List<Integer> costs) {
        int cost = steps * podCost[pod];
        while (col > 0) {
            if (burrow[0][col] == '.') {
//                System.out.println(pod + " can move to: (0, " + col + ") at cost: " + cost);
                moves.add(new Point(0, col));
                costs.add(cost);
                col -= 2;
                cost += 2 * podCost[pod];
            } else {
//                System.out.println(pod + " can not move to: (0, " + col + "), occupied");
                break;
            }
        }
        if (col == -1) { // No blockage
            if (burrow[0][0] == '.') {
                cost -= podCost[pod];
//                System.out.println(pod + " can move to: (0, 0) at cost: " + cost);
                moves.add(new Point(0, 0));
                costs.add(cost);
            } else {
//                System.out.println(pod + " can not move to (0, 0), occupied" );
            }
        }
    }

    static void moveOutsideRoom(int pod, int col, int steps, List<Point> moves, List<Integer> costs) {
        moveOutsideLeft(pod, col - 1, steps, moves, costs);
        moveOutsideRight(pod, col + 1, steps, moves, costs);
    }

    static void moveInsideRoom(int pod, int col, List<Point> moves, List<Integer> costs) {
        int target = pod * 2 + 2;
//        System.out.println("Target room: " + target);
        int delta = Math.abs(col - target);
        int step = col < target ? 1 : - 1;
//        System.out.println("pod: " + pod + ", col: " + col + ", target: " + target + ", step: " + step);
        if (col == 2) {

        }
        for (int start = col + step; start != target; start += step) {            
            if (burrow[0][start] != '.') {
//                System.out.println(pod + " can not move to room " + target + ", blocked");
                return;
            }
        }
        int cost = delta * podCost[pod];
        if (burrow[1][target] == '.') {
            if (burrow[2][target] == '.') {
                // Move to bottom
                cost += 2 * podCost[pod];
//                System.out.println(pod + " can move to: (2, " + target + ") at cost: " + cost);
                moves.add(new Point(2, target));
                costs.add(cost);
            } else if (burrow[2][target] - 'A' == pod) {
                // Move to top
                cost += podCost[pod];
//                System.out.println(pod + " can move to: (1, " + target + ") at cost: " + cost);
                moves.add(new Point(1, target));
                costs.add(cost);
            } else {
//                System.out.println(pod + " can not move to (1-2, " + target + "), blocked" );
            }
        } else {
//            System.out.println(pod + " can not move to (1, " + target + "), blocked" );
        }
    }

    static void findPossibleMoves(Point pos, List<Point> moves, List<Integer> costs) {
        char podName = burrow[pos.x][pos.y];
        int pod = podName - 'A';
//        System.out.println(podName + " is at " + pos + ", cost is: ");
        int movingCost = 0;
        if (pos.x == 2) {
            if (burrow[1][pos.y] == '.') {
                moveOutsideRoom(pod, pos.y, 3, moves, costs);
            } else {
//                System.out.println(pod + " can't move up");
            }
        } else if (pos.x == 1) {
            moveOutsideRoom(pod, pos.y, 2, moves, costs);
        } else { // In hallway - try to move into a room
            moveInsideRoom(pod, pos.y, moves, costs);
        }
    }

    static boolean home() {
        boolean home = burrow[1][2] == 'A' && burrow[2][2] == 'A' &&
                       burrow[1][4] == 'B' && burrow[2][4] == 'B' &&
                       burrow[1][6] == 'C' && burrow[2][6] == 'C' &&
                       burrow[1][8] == 'D' && burrow[2][8] == 'D';
        return home;
    }

    static boolean identical(char[][] oldBurrow) {
        for (int i = 0; i < burrow.length; i++) {
            for (int j = 0; j < burrow[i].length; j++) {
                if (oldBurrow[i][j] != burrow[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean visited(int cost) {
        for (int i = 0; i < history.size(); i++) {
            char[][] oldBurrow = history.get(i);
                if (identical(oldBurrow)) {
                    if (cost < costHistory.get(i)) {
//                        System.out.println("Found lower cost path: " + cost + ", " + costHistory.get(i));
                        costHistory.set(i, cost);
                        return false; // Continue on lower cost path                        
                    } else {
                        return true;
                    }
            }
        }
        return false;
    }

    static void addToHistory(int cost) {
//        if (burrow[0][3] == 'B' && burrow[1][2] == 'B' &&
//            burrow[2][2] == 'A' && burrow[2][4] == 'D' && burrow[2][6] == 'C' && burrow[2][8] == 'A' &&
//            burrow[1][8] == 'D') {
//            showBurrow("Specific step, cost: " + cost);
//        }
//        if (cost == 28387 || cost == 28389) {
//            showBurrow("Specific step, cost: " + cost);
//        }
        char[][] newBurrow = { burrow[0].clone(), burrow[1].clone(), burrow[2].clone()};
        history.add(newBurrow);
        costHistory.add(cost);
    }

    static void play(int cost) {        
        moveCount++;
        if (cost > minSoFar) {
            return;
        }

//        if (moveCount++ >= 2000) {
//            System.out.println("Just bail out ...");
//            return;
//        }
//        showBurrow("At entry: " + cost);
        if (home()) {
            if (cost < minSoFar) {
                System.out.println("Reached one path home: " + cost);
                minSoFar = cost;
            }
            return;
        }
        if (visited(cost)) {
//            System.out.println("Seen this already");
            return;
        }
//        addToHistory(cost);
        for (Point[] pair : amphipods) {
            for (int p = 0; p < pair.length; p++) {
                Point pos = pair[p];
                char podName = burrow[pos.x][pos.y];
                int pod = podName - 'A';
                if (pos.x == 2 && pos.y - 2 == pod * 2) {
//                    System.out.println("Don't move this one: " + podName);
                    continue;
                }
                if (pos.x == 1 && pos.y - 2 == pod * 2) {
                    char nextPodName = burrow[2][pos.y];
                    if (nextPodName - 'A' == pod) {
//                        System.out.println("Same one below, don't move this one either: " + podName);
                        continue;
                    }
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
//                    showBurrow("move done");
                    play(cost + extraCost);
                    // Restore
                    burrow[newPos.x][newPos.y] = '.';
                    burrow[pos.x][pos.y] = podName;
                    pair[p] = pos;
//                    showBurrow("restored");
                }
            }
        }
    }








    static boolean homeHard() {
        boolean home = burrow[1][2] == 'A' && burrow[2][2] == 'A' &&
                       burrow[1][4] == 'B' && burrow[2][4] == 'B' &&
                       burrow[1][6] == 'C' && burrow[2][6] == 'C' &&
                       burrow[1][8] == 'D' && burrow[2][8] == 'D';
        return home;
    }

    static void moveInsideRoomHard(int pod, int col, List<Point> moves, List<Integer> costs) {
        int target = pod * 2 + 2;
//        System.out.println("Target room: " + target);
        int delta = Math.abs(col - target);
        int step = col < target ? 1 : - 1;
        for (int start = col + step; start != target; start += step) {            
            if (burrow[0][start] != '.') {
//                System.out.println(pod + " can not move to room " + target + ", blocked");
                return;
            }
        }
        for (int row = 1; row <= 4; row++) {
            if (burrow[row][target] != '.' &&
                burrow[row][target] - 'A' != pod) {
//                System.out.println("Can not move into this room.");
                return;
            }
        }
        int cost = delta * podCost[pod];
        if (burrow[1][target] == '.') {
            if (burrow[2][target] == '.') {
                if (burrow[3][target] == '.') {
                    if (burrow[4][target] == '.') {
                        // Move to bottom
                        cost += 4 * podCost[pod];
//                System.out.println(pod + " can move to: (4, " + target + ") at cost: " + cost);
                        moves.add(new Point(4, target));
                        costs.add(cost);
                    } if (burrow[4][target] - 'A' == pod) {
                        // Move to 3, same pod at 4
                        cost += 3 * podCost[pod];
//                System.out.println(pod + " can move to: (3, " + target + ") at cost: " + cost);
                        moves.add(new Point(3, target));
                        costs.add(cost);
                    }
                } else if (burrow[3][target] - 'A' == pod) {
                    // Move to 2, same pod at 3
                    cost += 2 * podCost[pod];
//                System.out.println(pod + " can move to: (2, " + target + ") at cost: " + cost);
                    moves.add(new Point(2, target));
                    costs.add(cost);
                }
            } else if (burrow[2][target] - 'A' == pod) {
                // Move to 1, same pod at 2
                cost += podCost[pod];
//                System.out.println(pod + " can move to: (1, " + target + ") at cost: " + cost);
                moves.add(new Point(1, target));
                costs.add(cost);
            } else {
//                System.out.println(pod + " can not move to (1-2, " + target + "), blocked" );
            }
        } else {
//            System.out.println(pod + " can not move to (1, " + target + "), blocked" );
        }
    }

    static void findPossibleMovesHard(Point pos, List<Point> moves, List<Integer> costs) {
        char podName = burrow[pos.x][pos.y];
        int pod = podName - 'A';
//        System.out.println(podName + " is at " + pos + ", cost is: ");
        int movingCost = 0;
        if (pos.x == 4) {
            if (burrow[3][pos.y] == '.' && burrow[2][pos.y] == '.' && burrow[1][pos.y] == '.') {
                moveOutsideRoom(pod, pos.y, 5, moves, costs);
            } else {
//                System.out.println(pod + " can't move up");
            }
        } else if (pos.x == 3) {
            if (burrow[2][pos.y] == '.' && burrow[1][pos.y] == '.') {
                moveOutsideRoom(pod, pos.y, 4, moves, costs);
            } else {
//                System.out.println(pod + " can't move up");
            }
        } else if (pos.x == 2) {
            if (burrow[1][pos.y] == '.') {
                moveOutsideRoom(pod, pos.y, 3, moves, costs);
            } else {
//                System.out.println(pod + " can't move up");
            }
        } else if (pos.x == 1) {
            moveOutsideRoom(pod, pos.y, 2, moves, costs);
        } else { // In hallway - try to move into a room
            moveInsideRoomHard(pod, pos.y, moves, costs);
        }
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

    static void playHard(int cost) {
        moveCount++;
        if (cost > minSoFar) {
            return;
        }
        if (homeHard()) {
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
                findPossibleMovesHard(pos, moves, costs);
//                System.out.println("# possible moves for: " + podName + ": " + moves);
                for (int i = 0; i < moves.size(); i++) {
                    Point newPos = moves.get(i);
                    int extraCost = costs.get(i);
                    burrow[pos.x][pos.y] = '.';
                    burrow[newPos.x][newPos.y] = podName;
                    pair[p] = newPos;
                    playHard(cost + extraCost);
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
        playHard(0);
        System.out.println("Number of moves: " + moveCount);
    }
}
