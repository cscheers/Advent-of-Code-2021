import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Day15 {

    static List<int[]> grid = new ArrayList<int[]>();
    static int rows, columns;
    static boolean[][] accounted;
    static int[][] risks;

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int[] row = new int[line.length()];
            for (int i = 0; i < line.length(); i++) {
                row[i] = line.charAt(i) - '0';
            }
            grid.add(row);
        }
        rows = grid.size();
        columns = grid.get(0).length;
        System.out.println("Grid size: (" + rows + ", " + columns + ")");
    }

    static void visit(int risk, int row, int col) {
        if (row < 0 || col < 0) {
            return;
        }
        if (row == rows || col == columns) {
            return;
        }
        if (accounted[row][col]) {
            return;
        }
        int newRisk = risk + grid.get(row)[col];
        if (newRisk < risks[row][col]) {
            risks[row][col] = newRisk;
        }
    }

    static void dijkstra() {
        risks = new int[rows][columns];
        accounted = new boolean[rows][columns];
        for (int[] arr : risks) {
            Arrays.fill(arr, Integer.MAX_VALUE);
        }
        risks[0][0] = 0;
        int row = 0, col = 0;
        System.out.println("Total number of iterations: " + (rows * columns));
        int iteration = 0;
        int startRow = 0, endRow = rows - 1;
        while (true) {
            if (++iteration % 10000 == 0) {
                System.out.print("Iteration: " +  iteration);
                System.out.println(", start row: " + startRow + ", end row: " + endRow);
            }
            int risk = risks[row][col]; // Minimum risk from 0, 0 to row, col
            visit(risk, row, col + 1); // Go right
            visit(risk, row, col - 1); // Go left
            visit(risk, row + 1, col); // Go down
            visit(risk, row - 1, col); // Go up
            accounted[row][col] = true;
            // Select next element
            int minRisk = Integer.MAX_VALUE;
            for (int i = startRow; i < rows; i++) {
                boolean allMaxValues = true;
                boolean allAccounted = true;
                for (int j = 0; j < columns; j++) {
                    if (accounted[i][j]) {
                        allMaxValues = false;
                        continue;
                    }
                    allAccounted = false;
                    if (risks[i][j] < minRisk) {
                        minRisk = risks[i][j];
                        row = i;
                        col = j;
                    }
                    if (risks[i][j] != Integer.MAX_VALUE) {
                        allMaxValues = false;
                    }
                }
                if (allAccounted && i == startRow) {
                    startRow++;
                }
                if (allMaxValues) {
                    endRow = i;
                    break;
                }
            }
            if (minRisk == Integer.MAX_VALUE) {
                break;
            }
        }
        System.out.println("Finished " + iteration + " iterations");
    }

    static void showGrid() {
        for (int i = 0; i < grid.size(); i++) {
            int[] row = grid.get(i);
            for (int j = 0; j < row.length; j++) {
                System.out.print(row[j]);
            }
            System.out.println();
        }
    }

    static void largerGrid() {
        List<int[]> larger = new ArrayList<int[]>();
        for (int i = 0; i < rows; i++) {
            int[] row = new int[columns * 5];
            larger.add(row);
            int[] old = grid.get(i);
            for (int j = 0; j < columns; j++) {
                row[j] = old[j];
            }
            for (int j = 0; j < 4 * columns; j++) {
                row[j + columns] = row[j] + 1 == 10 ? 1 : row[j] + 1;
            }
        }
        for (int i = 0; i < 4 * rows; i++) {
            int[] old = larger.get(i);
            int[] row = new int[columns * 5];
            larger.add(row);
            for (int j = 0; j < old.length; j++) {
                row[j] = old[j] + 1 == 10 ? 1 : old[j] + 1;
            }
        }
        grid = larger;
        rows = larger.size();
        columns = larger.get(0).length;
        System.out.println("Grid size: (" + rows + ", " + columns + ")");
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day15");
        readFile(new File("data/day15.txt"));
//        showGrid();
        dijkstra();
        System.out.println("Min risk: " + risks[rows - 1][columns - 1]);
        largerGrid();
//        showGrid();
        dijkstra();
        System.out.println("Min risk larger grid: " + risks[rows - 1][columns - 1]);
    }
}
