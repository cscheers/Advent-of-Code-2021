import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Day20 {

    static String algo;
    static List<char[]> image = new ArrayList<char[]>();

    static void readFile(File file, int steps) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int extra = (steps + 1) * 2;
        char[] extraColumns = new char[extra];
        Arrays.fill(extraColumns, '.');
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            if (algo == null) {
                algo = line;
                System.out.println("Algo length: " + algo.length());
            } else if (line.length() > 0) {
                line = new String(extraColumns) + line + new String(extraColumns);
                image.add(line.toCharArray());
            }
        }
        System.out.println("Number of image rows: " + image.size());
        char[] extraRow = new char[image.get(0).length];
        Arrays.fill(extraRow, '.');
        for (int i = 0; i < extra; i++) {
            image.add(0, extraRow);
            image.add(extraRow);
        }
    }

    static void showImage() {
        for (char[] line : image) {
            System.out.println(new String(line));
        }
    }

    static int processColumns(int row, int col) {
        int ans = 0;
        for (int i = 0; i < 3; i++) {
            ans <<= 1;
            ans += image.get(row)[col + i] == '.' ? 0 : 1;
        }
        return ans;
    }

    static int processSquare(int row, int col) {
        int ans = processColumns(row, col);
        ans <<= 3;
        ans += processColumns(row + 1, col);
        ans <<= 3;
        ans += processColumns(row + 2, col);
        return ans;
    }

    static void enhance() {
        List<char[]> copy = new ArrayList<char[]>();
        char[] line = image.get(0).clone();
        Arrays.fill(line, '.');
        for (char[] old : image) {
            copy.add(line.clone());
        }
        for (int row = 0; row < image.size() - 2; row++) {
            for (int col = 0; col < image.get(row).length - 2; col++) {
                int code = processSquare(row, col);
                copy.get(row + 1)[col + 1] = algo.charAt(code);
            }
        }
        image = copy;
    }

    static int countLights(int steps) {
        // Edges deteriorate, so stay away from the edges
        int count = 0;
        for (int row = steps; row < image.size() - steps; row++) {
            char[] line = image.get(row);
            for (int col = steps; col < line.length - steps; col++) {
                count += line[col] == '#' ? 1 : 0;
            }
        }
        return count;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 20");
        int steps = 50;
        readFile(new File("data/day20.txt"), steps);
//        showImage();
        for (int i = 0; i < steps; i++) {
            if (i == 2) {
                System.out.println("Number of lights after 2 steps: " + countLights(i));
            }
            enhance();
//            showImage();
        }
        System.out.println("Number of lights after " + steps + " steps: " + countLights(steps));
    }
}
