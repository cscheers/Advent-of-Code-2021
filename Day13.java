import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day13 {

    static int[][] grid;
    static int width, height;

    static void showGrid() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[x][y] == 0) {
                    System.out.print(".");
                } else {
                    System.out.print("#");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    static int countDots() {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[x][y] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    static void fold(char dir, int value) {
        if (dir == 'y') {
            for (int y = 1; y + value < height; y++) {
                for (int x = 0; x < width; x++) {
                    grid[x][value - y] += grid[x][value + y];
                }
            }
            height = value;
        } else {
            for (int x = 1; x + value < width; x++) {
                for (int y = 0; y < height; y++) {
                    grid[value - x][y] += grid[value + x][y];
                }
            }
            width = value;
        }
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        boolean parsingXYs = true;
        int step = 1;
        for (int i = 0; scanner.hasNextLine(); i++) {
            String line = scanner.nextLine();
            if (line.length() == 0) {
                parsingXYs = false;
            } else if (parsingXYs) {
                String[] xy = line.split(",");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                if (grid == null) {
                    width = Math.max(width, x);
                    height = Math.max(height, y);
                } else {
                    grid[x][y] = 1;
                }
            } else {
                if (grid == null) {
                    break;
                }
                String[] tokens = line.split("\\s+");
                String[] fold = tokens[2].split("=");
                char foldDir = fold[0].charAt(0);
                int foldVal = Integer.parseInt(fold[1]);
                fold(foldDir, foldVal);
                if (step++ == 1) {
                    System.out.println("Number of dots after " + (step - 1) + " fold: " + countDots());
                }
                countDots();
            }
        }
        if (grid == null) {
            width++;
            height++;
            System.out.println("Grid size: (" + width + ", " + height + ")");
            grid = new int[width][height];
        } else {
            System.out.println("Number of dots after " + (step - 1) + " fold: " + countDots());
            showGrid();
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("\nDay 13");
        File file = new File("data/day13.txt");
        readFile(file);
        readFile(file);
    }
}
