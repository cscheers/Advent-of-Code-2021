import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class Day25 {

    static List<char[]> floor = new ArrayList<char[]>();

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            floor.add(line.toCharArray());
        }
    }

    static void showFloor() {
        for (char[] row : floor) {
            System.out.println(new String(row));
        }
        System.out.println();
    }

    static boolean moved() {
        boolean moved = false;
        for (int i = 0; i < floor.size(); i++) {
            char[] row = floor.get(i);
            if (row[row.length - 1] == '>' && row[0] == '.') {
                row[0] = '<';
                row[row.length - 1] = '#';
                moved = true;
            }
            for (int col = 0; col < row.length - 1; col++) {
                if (row[col] == '>' && row[col + 1] == '.') {
                    row[col] = '.';
                    row[col + 1] = '<';  // Prevent moving in next iteration
                    moved = true;
                } else if (row[col] == '<') {
                    row[col] = '>';
                }
            }
            if (row[row.length - 1] == '<') { // From previous position
                row[row.length - 1] = '>';
            } else if (row[row.length - 1] == '#') { // End of line moved to first
                row[row.length - 1] = '.';
            }
        }
        for (int col = 0; col < floor.get(0).length; col++) {
            if (floor.get(floor.size() - 1)[col] == 'v' && floor.get(0)[col] == '.') {
                floor.get(0)[col] = 'n';
                floor.get(floor.size() - 1)[col] = '#';
                moved = true;
            }
            for (int row = 0; row < floor.size() - 1; row++) {
                if (floor.get(row)[col] == 'v' && floor.get(row + 1)[col] == '.') {
                    floor.get(row)[col] = '.';
                    floor.get(row + 1)[col] = 'n';
                    moved = true;
                } else if (floor.get(row)[col] == 'n') {
                    floor.get(row)[col] = 'v';
                }
            }
            if (floor.get(floor.size() - 1)[col] == 'n') {
                floor.get(floor.size() - 1)[col] = 'v';
            } else if (floor.get(floor.size() - 1)[col] == '#') {
                floor.get(floor.size() - 1)[col] = '.';
            }
        }
        return moved;
    }

    static void play() {
//        showFloor();
        int steps = 0;
        while (moved()) {
            steps++;
//            System.out.println("After " + steps + " steps");
//            showFloor();
        }
        steps++;
        System.out.println("Floor stops moving after " + steps + " steps");
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 25");
        readFile(new File("data/day25.txt"));
        play();
    }
}
