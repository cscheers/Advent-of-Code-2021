import java.util.Scanner;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileNotFoundException;

public class Day9 {
        
    static List<String> list = new ArrayList<String>();
    static Queue<Integer> queue = new PriorityQueue<Integer>(Collections.reverseOrder());
    static boolean[][] visited;
    static int count;

    static void visit(int row, int col, int prev) {
        if (row < 0 || col < 0) {
            return;
        }
        if (row == list.size() || col == list.get(0).length()) {
            return;
        }
        char c = list.get(row).charAt(col);
        if (c == '9' || visited[row][col] || c <= prev) {
            return;
        }

        visited[row][col] = true;
        count++;
        visit(row + 1, col, c);
        visit(row - 1, col, c);
        visit(row, col + 1, c);
        visit(row, col - 1, c);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("\nDay 9");
        File file = new File("data/day9.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        // Apparently there is no need to initialize 'visited' for
        // each low point.
        visited = new boolean[list.size()][list.get(0).length()];
        String prev = null;
        String current = list.get(0);
        int risk = 0, row = 1;
        for(String next = current; next != null; current = next) {
            next = row++ < list.size() ? list.get(row - 1) : null;
            for (int i = 0; i < current.length(); i++) {
                if (prev != null && current.charAt(i) >= prev.charAt(i)) {
                    continue;
                }
                if (next != null && current.charAt(i) >= next.charAt(i)) {
                    continue;
                }
                if (i > 0 && current.charAt(i) >= current.charAt(i - 1)) {
                    continue;
                }
                if (i < current.length() - 1 && current.charAt(i) >= current.charAt(i + 1)) {
                    continue;
                }
                // This is a low point
                risk += (current.charAt(i) - '0') + 1;
                count = 0;
                visit(row - 2, i, current.charAt(i) - 1);
                queue.offer(count);
            }
            prev = current;
        }
        System.out.println("Risk: " + risk);
        int largests = queue.poll() * queue.poll() * queue.poll();
        System.out.println("Largests basins: " + largests);
    }
}
