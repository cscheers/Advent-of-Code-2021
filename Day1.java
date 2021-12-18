import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day1 {

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        Integer prev = null;
        int increasing = 0, increasingWindow = 0;
        int windowSize = 3;
        int v0 = 0, v1 = 0, v2 = 0, sum = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int value = Integer.valueOf(line);
            if (prev != null) {
                if (value > prev) {
                    increasing++;
                }
            }
            prev = value;
            if (windowSize > 0) {
                sum = v1 + v2 + value;
                windowSize--;
            } else {
                if (value - v0 > 0) {
                    increasingWindow++;
                }
                sum += value - v0;
            }
            v0 = v1;
            v1 = v2;
            v2 = value;
        }
        System.out.println("Number of increasing measurements: " + increasing);
        System.out.println("Number of increasing sliding window measurements: " + increasingWindow);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 1");
        readFile(new File("data/day1.txt"));
    }
}
