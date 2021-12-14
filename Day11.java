import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day11 {

    static int[][] grid;

    public static void showGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] > 9) {
                    System.out.print("x");
                } else {
                    System.out.print(grid[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void increase(int i, int j) {
        if (i < 0 || j < 0) {
            return;
        }
        if (i == grid.length || j == grid[0].length) {
            return;
        }
        // Don't increase 0's
        if (grid[i][j] > 0) {
            grid[i][j]++;
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("\nDay 11");
        File file = new File("data/day11.txt");
        Scanner scanner = new Scanner(file);
        for (int i = 0; scanner.hasNextLine(); i++) {
            String line = scanner.nextLine();
            if (grid == null) {
                grid = new int[line.length()][line.length()];
            }
            for (int j = 0; j < line.length(); j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
        }

        int total = 0;
        for (int step = 1; step <= 10000; step++) {
            // Increase by one
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j]++;
                }
            }

            int flashing, flashingInStep = 0;
            do {
                flashing = 0;
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        if (grid[i][j] > 9) {
                            flashing++;
                            grid[i][j] = 0;
                            increase(i, j - 1);
                            increase(i - 1, j - 1);
                            increase(i - 1, j);
                            increase(i - 1, j + 1);
                            increase(i, j + 1);
                            increase(i + 1, j + 1);
                            increase(i + 1, j);
                            increase(i + 1, j - 1);
                        }
                    }
                }
                flashingInStep += flashing;
            } while (flashing > 0);
            total += flashingInStep;
            if (flashingInStep == 100) {
                System.out.println("All flashing after step: " + step);
//                showGrid();
                break;
            }
            if (step == 100) {
                System.out.println("After step " + step + " : " + total);
//                showGrid();
            }
        }
    }
}
