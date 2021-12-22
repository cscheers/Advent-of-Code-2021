import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

public class Day5 {

    static List<Point> fromPoints = new ArrayList<Point>();
    static List<Point> toPoints = new ArrayList<Point>();
    static int[][] board;

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split("\\s+");
            String[] left = tokens[0].split(",");
            Point from = new Point(Integer.valueOf(left[0]), Integer.valueOf(left[1]));
            String[] right = tokens[2].split(",");
            Point to = new Point(Integer.valueOf(right[0]), Integer.valueOf(right[1]));
            fromPoints.add(from);
            toPoints.add(to);
        }
    }

    static int maxX(List<Point> points) {
        int max = 0;
        for (Point p : points) {
            max = Math.max(max, p.x);
        }
        return max;
    }

    static int maxY(List<Point> points) {
        int max = 0;
        for (Point p : points) {
            max = Math.max(max, p.y);
        }
        return max;
    }

    static void drawLine(Point from, Point to) {
        if (from.x == to.x) {
            int fromY = from.y;
            int toY = to.y;
            if (fromY > toY) {
                int tmp = fromY;
                fromY = toY;
                toY = tmp;
            }
            for (int i = fromY; i <= toY; i++) {
                board[from.x][i]++;
            }
        } else {
            int fromX = from.x;
            int toX = to.x;
            if (fromX > toX) {
                int tmp = fromX;
                fromX = toX;
                toX = tmp;
            }
            for (int i = fromX; i <= toX; i++) {
                board[i][from.y]++;
            }
        }
    }

    static int findOverlapCount() {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] > 1) {
                    count++;
                }
            }
        }
        return count;
    }

    static void play() {
        int maxX = maxX(fromPoints);
        maxX = Math.max(maxX, maxX(toPoints));
        int maxY = maxY(fromPoints);
        maxY = Math.max(maxY, maxY(toPoints));
        System.out.println("X max: " + maxX + ", Y max: " + maxY);
        board = new int[maxX + 1][maxY + 1];
        for (int i = 0; i < fromPoints.size(); i++) {
            Point from = fromPoints.get(i);
            Point to = toPoints.get(i);
            if (from.x != to.x && from.y != to.y) {
                continue;
            }
            drawLine(from, to);
        }
        int count = findOverlapCount();
        System.out.println("First star count: " + count);
    }

    static void drawDiagonal(Point from, Point to) {
        int deltaX = from.x < to.x ? 1 : -1;
        int deltaY = from.y < to.y ? 1 : -1;
        int y = from.y;
        for (int x = from.x; x != to.x; x += deltaX) {
            board[x][y]++;
            y += deltaY;
        }
        board[to.x][y]++;
    }

    static void playHard() {
        int maxX = maxX(fromPoints);
        maxX = Math.max(maxX, maxX(toPoints));
        int maxY = maxY(fromPoints);
        maxY = Math.max(maxY, maxY(toPoints));
        System.out.println("X max: " + maxX + ", Y max: " + maxY);
        board = new int[maxX + 1][maxY + 1];
        for (int i = 0; i < fromPoints.size(); i++) {
            Point from = fromPoints.get(i);
            Point to = toPoints.get(i);
            if (from.x != to.x && from.y != to.y) {
                drawDiagonal(from, to);
            } else {
                drawLine(from, to);
            }
        }
        int count = findOverlapCount();
        System.out.println("Second star count: " + count);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 5");
        readFile(new File("data/day5.txt"));
        play();
        playHard();
    }
}
