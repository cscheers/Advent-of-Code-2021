import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;

public class Day3 {

    static List<String> numbers = new ArrayList<String>();

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int[] ones = null;
        int count = 0;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            numbers.add(line);
//            System.out.println(line);
            if (ones == null) {
                ones = new int[line.length()];
            }
            for (int i = 0; i < ones.length; i++) {
                ones[i] += line.charAt(i) == '1' ? 1 : 0;
            }
            count++;
        }
        int gamma = 0, epsilon = 0;
        for (int i = 0; i < ones.length; i++) {
//            System.out.println(ones[i]);
            int next = ones[i] > count / 2 ? 1 : 0;
            gamma <<= 1;
            gamma += next;
            epsilon <<= 1;
            epsilon += next == 1 ? 0 : 1;
        }
        System.out.println("gamma: " + gamma + ", " + Integer.toBinaryString(gamma));
        System.out.println("epsilon: " + epsilon + ", " + Integer.toBinaryString(epsilon));
        System.out.println("Product: " + (gamma * epsilon));
    }

    static List<String> selectSubset(List<String> in, int position, char c) {
        List<String> out = new ArrayList<String>();
        for (String number : in) {
            if (number.charAt(position) == c) {
                out.add(number);
            }            
        }
        return out;
    }

    static int countOnes(List<String> in, int position) {
        int ones = 0;
        for (String number : in) {
            ones += number.charAt(position) == '1' ? 1 : 0;            
        }
        return ones;
    }

    static int findOxyRating() {
        List<String> next = numbers;
        for (int position = 0; next.size() > 1; position++) {
            int ones = countOnes(next, position);
            int zeros = next.size() - ones;
//            System.out.println(ones + " ones out of " + next.size() + " at position: " + position);
            char oxy = '1';
            if (zeros > ones) {
                oxy = '0';
            }
            next = selectSubset(next, position, oxy);
//            System.out.println("Subset (" + oxy + ") " + next);
        }
        int rating = Integer.parseInt(next.get(0), 2);
        System.out.println("Oxygen generator rating: " + rating + ", " + next.get(0));
        return rating;
    }

    static int findCO2Rating() {
        List<String> next = numbers;
        for (int position = 0; next.size() > 1; position++) {
            int ones = countOnes(next, position);
            int zeros = next.size() - ones;
//            System.out.println(ones + " ones out of " + next.size() + " at position: " + position);
            char select = '0';
            if (ones < zeros) {
                select = '1';
            }
            next = selectSubset(next, position, select);
//            System.out.println("Subset (" + select + ") " + next);
        }
        int rating = Integer.parseInt(next.get(0), 2);
        System.out.println("CO2 scrubber rating: " + rating + ", " + next.get(0));
        return rating;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 3");
        readFile(new File("data/day3.txt"));

        int oxyRating = findOxyRating();
        int co2Rating = findCO2Rating();
        System.out.println("Product: " + oxyRating * co2Rating);
    }
}
