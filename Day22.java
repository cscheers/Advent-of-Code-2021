import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.awt.Point;

public class Day22 {

    static Set<String> remaining = new HashSet<String>();
    static List<Cuboid> cuboids = new ArrayList<Cuboid>();

    static class Cuboid {
        Cuboid(Point x, Point y, Point z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        Point x, y, z;
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            boolean on = tokens[0].equals("on");
            tokens = tokens[1].split(",");
            Point x = null, y = null, z = null;
            for (int i = 0; i < 3; i++) {
                String[] xyz = tokens[i].split("=");
                String[] range = xyz[1].split("\\.\\.");
                Point p = new Point(Integer.valueOf(range[0]), Integer.valueOf(range[1]));
                insideRange(p);
                switch (i) {
                    case 0:
                        x = p;
                        break;
                    case 1:
                        y = p;
                        break;
                    case 2:
                        z = p;
                        break;
                }
            }
            reboot(on, x, y, z);
        }
    }

    static void readFile2(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            boolean on = tokens[0].equals("on");
            tokens = tokens[1].split(",");
            Point x = null, y = null, z = null;
            for (int i = 0; i < 3; i++) {
                String[] xyz = tokens[i].split("=");
                String[] range = xyz[1].split("\\.\\.");
                Point p = new Point(Integer.valueOf(range[0]), Integer.valueOf(range[1]));
                switch (i) {
                    case 0:
                        x = p;
                        break;
                    case 1:
                        y = p;
                        break;
                    case 2:
                        z = p;
                        break;
                }
            }
            Cuboid cuboid = new Cuboid(x, y, z);
            rebootHard(on, cuboid);
        }
    }

    static long countCubes(Cuboid c) {
        long dx = c.x.y - c.x.x + 1;
        long dy = c.y.y - c.y.x + 1;
        long dz = c.z.y - c.z.x + 1;
        return dx * dy * dz;
    }

    static long countAllCubes() {
        long count = 0;
        for (Cuboid cub : cuboids) {
            count += countCubes(cub);
        }
        return count;
    }

    static boolean overlapping(Cuboid refCub, Cuboid cub) {
        if (refCub.x.x > cub.x.y || cub.x.x > refCub.x.y) {
            return false; // Non-overlapping in x
        }
        if (refCub.y.x > cub.y.y || cub.y.x > refCub.y.y) {
            return false; // Non-overlapping in y
        }
        if (refCub.z.x > cub.z.y || cub.z.x > refCub.z.y) {
            return false; // Non-overlapping in y
        }
        return true;
    }

    static void intersectOn(Cuboid refCub, Cuboid cub, List<Cuboid> sections) {
        if (!overlapping(refCub, cub)) {
            sections.add(cub); // Just add the whole new cuboid
            return;
        }
        if (cub.x.x < refCub.x.x) {
            // Partially overlapping on left side
            Point sectionX = new Point(cub.x.x, refCub.x.x - 1);
            Cuboid section = new Cuboid(sectionX, new Point(cub.y), new Point(cub.z));
            sections.add(section); // New left X section
            cub.x.x = refCub.x.x; // Reduce new left X side 
        }
        if (cub.x.y > refCub.x.y) {
            // Partially overlapping on right side
            Point sectionX = new Point(refCub.x.y + 1, cub.x.y);
            Cuboid section = new Cuboid(sectionX, new Point(cub.y), new Point(cub.z));
            sections.add(section); // New right X section
            cub.x.y = refCub.x.y; // Reduce new right X side
        }
        if (cub.y.x < refCub.y.x) {
            // Partially overlapping on left side
            Point sectionY = new Point(cub.y.x, refCub.y.x - 1);
            Cuboid section = new Cuboid(new Point(cub.x), sectionY, new Point(cub.z));
            sections.add(section); // New left Y section
            cub.y.x = refCub.y.x; // Reduce new left Y side
        }
        if (cub.y.y > refCub.y.y) {
            // Partially overlapping on right side
            Point sectionY = new Point(refCub.y.y + 1, cub.y.y);
            Cuboid section = new Cuboid(new Point(cub.x), sectionY, new Point(cub.z));
            sections.add(section); // New right Y section
            cub.y.y = refCub.y.y; // Reduce new right Y side
        }
        if (cub.z.x < refCub.z.x) {
            // Partially overlapping on left side
            Point sectionZ = new Point(cub.z.x, refCub.z.x - 1);
            Cuboid section = new Cuboid(new Point(cub.x), new Point(cub.y), sectionZ);
            sections.add(section); // New left Z section
            cub.z.x = refCub.z.x; // Reduce new left Z side
        }
        if (cub.z.y > refCub.z.y) {
            // Partially overlapping on right side
            Point sectionZ = new Point(refCub.z.y + 1, cub.z.y);
            Cuboid section = new Cuboid(new Point(cub.x), new Point(cub.y), sectionZ);
            sections.add(section); // New right Z section
            cub.z.y = refCub.z.y; // Reduce new right Z side
        }
    }

    static void intersectOnWith(Cuboid cub) {
        List<Cuboid> newCubs = new ArrayList<Cuboid>();
        newCubs.add(cub);
        for (Cuboid refCub : cuboids) {
            List<Cuboid> sections = new ArrayList<Cuboid>();
            for (Cuboid newCub : newCubs) {
                intersectOn(refCub, newCub, sections);
            }
            // Continue with the remaining sections
            newCubs = sections;
        }
        cuboids.addAll(newCubs);
    }

    static void intersectOffWith(Cuboid cub) {
        List<Cuboid> newCubs = new ArrayList<Cuboid>();
        List<Cuboid> removedCubs = new ArrayList<Cuboid>();
        for (Cuboid refCub : cuboids) {
            if (overlapping(refCub, cub)) {
                removedCubs.add(refCub);
            } else {
                newCubs.add(refCub);
            }
        }
        cuboids = newCubs; // start with the ones that don't overlap
        for (Cuboid removedCub : removedCubs) {
            // Add sections that don't overlap with the 'off' cuboid
            intersectOn(cub, removedCub, cuboids);
        }
    }

    static void rebootHard(boolean on, Cuboid cub) {
        long before = countAllCubes();
//        System.out.println("on: " + on + ", x: " + cub.x + ", y: " + cub.y + ", z: " + cub.z);
        if (on) {
            intersectOnWith(cub);
        } else {
            intersectOffWith(cub);
        }
        System.out.print("Number of cuboids: " + cuboids.size() + ", ");
        long after = countAllCubes();
        long delta = after - before;
        if (delta > 0) {
            System.out.print("Added " + delta);
        } else {
            System.out.print("Removed " + -delta);
        }
        System.out.println(" cubes, total number of cubes: " + after);
    }

    static void insideRange(Point p) {
        if (p.y < -50 || p.x > 50) {  // Completely outside range
            p.x = 0;
            p.y = -1;
        } else {
            if (p.x < -50) {
                p.x = -50;
            }
            if (p.y > 50) {
                p.y = 50;
            }
        }
    }

    static boolean containsCube(List<int[]> refCubes, int[] cube) {
        for (int[] refCube : refCubes) {
            if (refCube[0] == cube[0] &&
                refCube[1] == cube[1] &&
                refCube[2] == cube[2]) {
                return true;
            }
        }
        return false;
    }

    static String getCubeKey(int[] cube) {
        return cube[0] + "|" + cube[1] + "|" + cube[2];
    }

    static void reboot(boolean on, Point x, Point y, Point z) {
        int added = 0, removed = 0;
        for (int i = x.x; i <= x.y; i++) {
            for (int j = y.x; j <= y.y; j++) {
                for (int k = z.x; k <= z.y; k++) {
                    int[] cube = {i, j, k};
                    String key = getCubeKey(cube);
                    if (on) {
                        if (!remaining.contains(key)) {
                            remaining.add(getCubeKey(cube));
                            added++;
                        }
                    } else {
                        if (remaining.contains(key)) {
                            removed++;
                            remaining.remove(key);
                        }
                    }
                }
            }
        }
        if (added > 0) {
            System.out.println(added + " cubes added");
        }
        if (removed > 0) {
            System.out.println(removed + " cubes removed");
        }
        if (added > 0 || removed > 0) {
            System.out.println("First star - " + remaining.size() + " cubes");
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 22");
        readFile(new File("data/day22.txt"));
        readFile2(new File("data/day22.txt"));
    }
}
