import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class Day12 {

    static Map<String, Set<String>> adj = new HashMap<String, Set<String>>();
    static LinkedList<String> path = new LinkedList<String>();
    static Set<String> visiting = new HashSet<String>();

    static boolean isLowerCase(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    static void addKeyValue(String key, String value) {
        Set<String> adjNodes = adj.get(key);
        if (adjNodes == null) {
            adjNodes = new HashSet<String>();
            adj.put(key, adjNodes);
        }
        adjNodes.add(value);
    }

    static int visitNode(String cave, boolean hadOneExtra) {
        if (cave.equals("end")) {
//            System.out.println("Solution: " + path);
            return 1;
        }
        boolean triggeredOneExtra = false;
        if (isLowerCase(cave) && visiting.contains(cave)) {
            // Only allow 1 path through this node
            if (hadOneExtra) {
                return 0;
            } else {
                // Make 1 exception
                hadOneExtra = true;
                triggeredOneExtra = true;
            }
        }
        path.addLast(cave);
        visiting.add(cave);
        int pathCount = 0;
        for (String nextCave : adj.get(cave)) {
            pathCount += visitNode(nextCave, hadOneExtra);
        }
        path.removeLast();
        if (!triggeredOneExtra) {
            visiting.remove(cave);
        }
        return pathCount;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("\nDay 12");
        File file = new File("data/day12.txt");
        Scanner scanner = new Scanner(file);
        for (int i = 0; scanner.hasNextLine(); i++) {
            String line = scanner.nextLine();
            String[] caves = line.split("-");
            if (!caves[1].equals("start")) {
                // Ignore any links going towards start
                addKeyValue(caves[0], caves[1]);
            }
            if (!caves[0].equals("start")) {
                // Ignore any links going towards start
                addKeyValue(caves[1], caves[0]);
            }
        }
        System.out.println("Number of paths first star: " + visitNode("start", true));
        System.out.println("Number of paths second star: " + visitNode("start", false));
    }
}
