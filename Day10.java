import java.util.Scanner;
import java.util.Collections;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Day10 {

    public static void go() throws FileNotFoundException {
        System.out.println("\nDay 10");
        File file = new File("data/day10.txt");
        Scanner scanner = new Scanner(file);
        int score = 0;
        List<Long> list = new ArrayList<Long>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Stack<Character> stack = new Stack<Character>();
            boolean invalid = false;
            for (int i = 0; i < line.length(); i++) {
                switch (line.charAt(i)) {
                    case '(' :
                    case '[' :
                    case '{' :
                    case '<' :
                        stack.push(line.charAt(i));
                        break;
                    case ')' :
                        if (stack.isEmpty() || stack.pop() != '(') {
                            invalid = true;
                            score += 3;
                        }
                        break;
                    case ']' :
                        if (stack.isEmpty() || stack.pop() != '[') {
                            invalid = true;
                            score += 57;
                        }
                        break;
                    case '}' :
                        if (stack.isEmpty() || stack.pop() != '{') {
                            invalid = true;
                            score += 1197;
                        }
                        break;
                    case '>' :
                        if (stack.isEmpty() || stack.pop() != '<') {
                            invalid = true;
                            score += 25137;
                        }
                        break;
                }
            }
            if (!invalid) {
                long incompleteScore = 0;
                while (!stack.isEmpty()) {
                    long point = 0;
                    switch (stack.pop()) {
                        case '(' :
                            point = 1;
                            break;
                        case '[' :
                            point = 2;
                            break;
                        case '{' :
                            point = 3;
                            break;
                        case '<' :
                            point = 4;
                            break;
                    }
                    incompleteScore = incompleteScore * 5 + point;
                }
                list.add(incompleteScore);
            }
        }
        System.out.println("Score: " + score);
        Collections.sort(list);
        long middle = list.get(list.size() / 2);
        System.out.println("Middle: " + middle);
    }
}
