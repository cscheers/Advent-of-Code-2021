import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Day18 {

    static int pos;
    static List<Pair> numbers = new ArrayList<Pair>();

    static class Pair {
        Pair(long value) {
            this.value = value;
        }
        Pair(Pair left, Pair right) {
            this.left = left;
            this.right = right;
            value = -1;
        }
        long value;
        Pair left, right;
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\[");
        Pair all = null;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            pos = 0;
            Pair pair = parsePair(line);
            numbers.add(pair);
            if (all == null) {
                all = pair;
            } else {
                all = addPairs(all, pair);
            }
        }
//        showPair(all);
//        System.out.println();
        System.out.println("Magnitude: " + magnitude(all));
        maxSum();
    }

    static void maxSum() {
        long max = 0;
        for (int i = 0; i < numbers.size(); i++) {
            Pair first = numbers.get(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                Pair second = numbers.get(j);
                max = Math.max(max, magnitude(addPairs(first, second)));
                max = Math.max(max, magnitude(addPairs(second, first)));
            }
        }
        System.out.println("Largest magnitude: " + max);
    }

    static long magnitude(Pair pair) {
        if (pair.left == null && pair.right == null) {
            // A leaf node
            return pair.value;
        } else {
            return 3 * magnitude (pair.left) + 2 * magnitude(pair.right);
        }
    }

    static Pair addPairs(Pair p1, Pair p2) {
        Pair pair = new Pair(clonePair(p1), clonePair(p2));
        while (true) {
            Pair target = findExplodeTarget(pair, 0);
            while (target != null) {
                explode(pair, target);
                target = findExplodeTarget(pair, 0);
            }
            if (!split(pair)) {
                break;
            }
        }
        return pair;
    }

    static Pair clonePair(Pair pair) {
        if (pair.left == null && pair.right == null) {
            // A leaf node
            return new Pair(pair.value);
        }
        return new Pair(clonePair(pair.left), clonePair(pair.right));
    }

    static boolean split(Pair pair) {
        if (pair.left == null && pair.right == null) {
            // A leaf node
            if (pair.value > 9) {
                pair.left = new Pair(pair.value / 2);
                pair.right = new Pair(pair.value - pair.left.value);
                return true;
            } else {
                return false;
            }
        }
        if (split(pair.left)) {
            return true;
        } else {
            return split(pair.right);
        }
    }

    static void explode(Pair pair, Pair target) {
        Stack<Pair> stack = new Stack<Pair>();
        Pair prev = null;
        while (pair != null || !stack.isEmpty()) {
            while (pair != null) {
                stack.push(pair);
                pair = pair.left;
            }
            pair = stack.pop();
            if (pair.left == null) {
                if (pair == target.left) { // Update prev node
                    if (prev != null) {
                        prev.value += target.left.value;
                    }
                } else if (prev == target.right) { // Update this node
                    pair.value += target.right.value;
                }
                prev = pair;
            }
            pair = pair.right;
        }
        // Explode target node
        target.left = null;
        target.right = null;
        target.value = 0;
    }

    static Pair findExplodeTarget(Pair pair, int level) {
        if (pair.left == null && pair.right == null) {
            // A leaf node
            return null;
        }
        if (level < 4) {
            Pair target = findExplodeTarget(pair.left, level + 1);
            if (target != null) {
                return target;
            }
            return findExplodeTarget(pair.right, level + 1);
        }
        return pair;
    }

    static void showPair(Pair pair) {
        if (pair.left == null && pair.right == null) {
            // Leaf node
            System.out.print(pair.value);
            return;
        }
        System.out.print("[");
        showPair(pair.left);
        System.out.print(",");
        showPair(pair.right);
        System.out.print("]");
    }

    static Pair parsePair(String line) {
        if (line.charAt(pos) != '[') {
            System.out.println("Parsing error: " + line);
            return null;
        }
        pos++;
        Pair left, right;
        if (line.charAt(pos) == '[') { // Nested pair
            left = parsePair(line);
            pos++;
        } else { // Number followed by comma
            int comma = line.indexOf(',', pos);
            left = new Pair(Integer.valueOf(line.substring(pos, comma)));
            pos = comma + 1;
        }
        if (line.charAt(pos) == '[') { // Nested pair
            right = parsePair(line);
            pos++;
        } else { // Number followed by close bracket
            int close = line.indexOf(']', pos);
            right = new Pair(Integer.valueOf(line.substring(pos, close)));
            pos = close + 1;
        }
        return new Pair(left, right);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 18");
        readFile(new File("data/day18.txt"));
    }
}
