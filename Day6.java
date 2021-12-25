import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class Day6 {

    static List<Integer> fishes = new ArrayList<Integer>();
    static long[] newFishes = new long[9];

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(",");
            for (String token : tokens) {
                fishes.add(Integer.valueOf(token));
            }
        }
    }

    static void oneDay() {
        long extra = newFishes[0];
        for (int i = 0; i < newFishes.length - 1; i++) {
            newFishes[i] = newFishes[i + 1];
        }
        newFishes[6] += extra;
        for (int i = 0; i < fishes.size(); i++) {
            int fish = fishes.get(i);
            if (fish == 0) {
                fish = 6;
                extra++;
            } else {
                fish--;
            }
            fishes.set(i, fish);
        }
        newFishes[8] = extra;
    }

    static long countFish() {
        long count = fishes.size();
        for (long num : newFishes) {
            count += num;
        }
        return count;
    }

    static void play() {
        for (int day = 1; day <= 256; day++) {
            oneDay();
            if (day == 80 || day == 256) {
                System.out.println("Total after day " + day + ": " + countFish());
            }
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 6");
        readFile(new File("data/day6.txt"));
        play();
    }
}
