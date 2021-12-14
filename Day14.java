import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Day14 {

    static Map<String, Character> inserts = new HashMap<String, Character>();
    static String template = null;
    static Map<String, Long> pairCounts = new HashMap<String, Long>();

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        boolean parsingXYs = true;
        for (int i = 0; scanner.hasNextLine(); i++) {
            String line = scanner.nextLine();
            if (template == null) {
                template = line;
                System.out.println("Template: " + template);
                scanner.nextLine(); // Skip blank line
            } else {
                String[] tokens = line.split("\\s+");
                inserts.put(tokens[0], tokens[2].charAt(0));
            }
        }
    }

    static void initPairCounts() {
        for (int i = 0; i < template.length() - 1; i++) {
            String pair = new String (new char[] { template.charAt(i), template.charAt(i + 1) });
            pairCounts.put(pair, pairCounts.getOrDefault(pair, 0L) + 1);
        }
    }

    static void processPolymer() {
        Map<String, Long> newCounts = new HashMap<String, Long>();
        for (Map.Entry<String, Long> entry : pairCounts.entrySet()) {
            String key = entry.getKey();
            Long count = entry.getValue();
            char middle = inserts.get(key);
            String first = new String(new char[] { key.charAt(0),  middle });
            newCounts.put(first, newCounts.getOrDefault(first, 0L) + count);
            String second = new String(new char[] { middle, key.charAt(1) });
            newCounts.put(second, newCounts.getOrDefault(second, 0L) + count);
        }
        pairCounts = newCounts;
    }

    static long countLetters() {
        Map<Character, Long> freq = new HashMap<Character, Long>();
        for (Map.Entry<String, Long> entry : pairCounts.entrySet()) {
            char first = entry.getKey().charAt(1);
            freq.put(first, freq.getOrDefault(first, 0L) + entry.getValue());
        }
        freq.put(template.charAt(0), freq.getOrDefault(template.charAt(0), 0L) + 1);
        List<Long> list = new ArrayList<Long>(freq.values());
        Collections.sort(list);
        return list.get(list.size() - 1) - list.get(0);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day14");
        readFile(new File("data/day14.txt"));
        initPairCounts();
        for (int step = 1; step <= 40; step++) {
            processPolymer();
            if (step == 10 || step == 40) {
                System.out.println("Result after " + step + " steps: " + countLetters());
            }
        }
    }
}
