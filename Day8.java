import java.util.Scanner;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

public class Day8 {

    static Map<String, Integer> map;

    public static void addNumberToken(String token, int value) {
        char[] arr = token.toCharArray();
        Arrays.sort(arr);
        map.put(new String(arr), value);
    }

    public static int findToken(String token) {
        char[] arr = token.toCharArray();
        Arrays.sort(arr);
        return map.get(new String(arr));
    }

    public static boolean hasAll(String token, String chars) {
        for (int i = 0; i < chars.length(); i++) {
            if (token.indexOf(chars.charAt(i)) < 0) {
                return false;
            }
        }
        return true;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("\nDay 8");
        File file = new File("data/day8.txt");
        Scanner scanner = new Scanner(file);
        long count = 0, total = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\|");
            String left = parts[0];
            String[] tokens = left.trim().split("\\s+");
            map = new HashMap<String, Integer>();

            String one = null, four = null;
            for (String token : tokens) {
                if (token.length() == 2) {
                    one = token; addNumberToken(token, 1);
                } else if (token.length() == 3) {
                    addNumberToken(token, 7);
                } else if (token.length() == 4) {
                    four = token; addNumberToken(token, 4);
                } else if (token.length() == 7) {
                    addNumberToken(token, 8);
                }
            }
            String diff = four.replace(one.charAt(0), ' ');
            diff = diff.replace(one.charAt(1), ' ').replace(" ", "");
            for (String token : tokens) {
                if (token.length() == 5) {
                    if (hasAll(token, one)) {
                        addNumberToken(token, 3);
                    } else if (hasAll(token, diff)) {
                        addNumberToken(token, 5);
                    } else {
                        addNumberToken(token, 2);
                    }
                }
                if (token.length() == 6) {
                    if (hasAll(token, one)) {
                        if (hasAll(token, four)) {
                            addNumberToken(token, 9);
                        } else {
                            addNumberToken(token, 0);
                        }
                    } else {
                        addNumberToken(token, 6);
                    }
                }
            }
            String right = parts[1];
            tokens = right.trim().split("\\s+");
            int factor = 1000;
            int value = 0;
            for (String token : tokens) {
                int digit = findToken(token);
                value += digit * factor;
                factor /= 10;
                if (token.length() == 2 ||
                    token.length() == 3 ||
                    token.length() == 4 ||
                    token.length() == 7) {
                    count++;
                }
            }
            total += value;
//            System.out.println("Output value: " + value);
        }
        System.out.println("Total number of 1 4 7 8 is: " + count);
        System.out.println("All output values: " + total);
    }
}
