import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day2 {

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int horizontal = 0, depth = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            String instruction = tokens[0];
            int value = Integer.valueOf(tokens[1]);
//            System.out.println(tokens[0] + ", " + value);
            if (instruction.equals("forward")) {
                horizontal += value;
            } else if (instruction.equals("up")) {
                depth -= value;
            } else if (instruction.equals("down")) {
                depth += value;
            }
        }
        System.out.println("horizontal * depth multiple: " + horizontal * depth);
    }

    static void readFile2(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int horizontal = 0, depth = 0, aim = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            String instruction = tokens[0];
            int value = Integer.valueOf(tokens[1]);
//            System.out.println(tokens[0] + ", " + value);
            if (instruction.equals("forward")) {
                horizontal += value;
                depth += aim * value;
            } else if (instruction.equals("up")) {
                aim -= value;
            } else if (instruction.equals("down")) {
                aim += value;
            }
        }
        System.out.println("horizontal * depth multiple: " + horizontal * depth);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 2");
        readFile(new File("data/day2.txt"));
        readFile2(new File("data/day2.txt"));
    }
}
