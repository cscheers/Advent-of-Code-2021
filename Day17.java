import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;

public class Day17 {

    static Point from, to, probe, velocity;
    static int height, hitCount = 0;

    static Point parseRange(String range) {
        String[] tokens = range.split("=|\\.\\.");
        return new Point(Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]));
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] tokens = line.split(",|\\s+");
            Point x = parseRange(tokens[2]);
            Point y = parseRange(tokens[4]);
            from = new Point(x.x, y.y);
            to = new Point(x.y, y.x);
            System.out.println("From: " + from);
            System.out.println("To: " + to);
        }
    }

    static void step() {
        probe.x += velocity.x;
        probe.y += velocity.y;
        if (velocity.x != 0) {
            if (velocity.x > 0) {
                velocity.x--;
            } else {
                velocity.x++;
            }
        }
        velocity.y--;
    }

    static boolean onTarget() {
        height = probe.y;
        while (true) {
            step();
            height = Math.max(height, probe.y);
            if (probe.x > to.x) { // Right of target, missed it.
                return false;
            } else if (probe.y < to.y) { // Below target, missed it.
                return false;
            } else if (probe.y > from.y) { // Above target, continue.
            } else if (probe.x < from.x) { // Left of target, continue.
            } else { // On target !
                return true;
            }
        }
    }

    static int findMinVelocityX() {
        for (int vx = 1; true; vx++) {
            probe.x = 0; probe.y = 0;
            velocity.x = vx; velocity.y = 0;
            while (velocity.x != 0) {
                step();
                if (probe.x >= from.x) {
                    System.out.println("Minimum velocity required: " + vx);
                    return vx;
                }
                if (probe.y < to.y) {
                    System.out.println("Below target, missed it");
                    break;
                }
            }
        }
    }

    static void findBestY(int vx) {
        int maxHeight = -1;
        boolean hit = false;
        int bestY = to.y - 1;
        System.out.println("Find best velocity y between: " + to.y + " and " + to.x);
        for (int vy = to.y; vy <= to.x; vy++) {
            probe.x = 0; probe.y = 0;
            velocity.x = vx; velocity.y = vy;
            if (onTarget()) {
                if (!hit) {
//                    System.out.println("First hit at velocity y: " + vy + ", height: " + height);
                    hit = true;
                }
                if (height > maxHeight) {
                    bestY = vy;
                    maxHeight = height;
                }
            }
        }
        System.out.println("Best velocity y: " + bestY);
        System.out.println("Max height: " + maxHeight);
    }

    static void findTarget(int vx) {
        for (int vy = to.y; vy <= to.x; vy++) {
            probe.x = 0; probe.y = 0;
            velocity.x = vx; velocity.y = vy;
            if (onTarget()) {
//                System.out.println("Fount hit at velocity (" + vx + ", " + vy + ")");
                hitCount++;
            }
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 17");
        readFile(new File("data/day17.txt"));
        probe = new Point(0, 0);
        velocity = new Point(0, 0);
        int vx = findMinVelocityX();
        findBestY(vx);
        while (vx <= to.x) {
            findTarget(vx++);
        }
        System.out.println("Total hit count: " + hitCount);
    }
}
