import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;
import java.awt.Point;

public class Day24 {

    static List<String[]> instructions = new ArrayList<String[]>();
    static Map<Character, Long> vars = new HashMap<Character, Long>();
    static Queue<Integer> inputs;
    static List<int[]> constants = new ArrayList<int[]>();

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split("\\s+");
            instructions.add(tokens);
        }
    }

    static long nextInput() {
        return inputs.poll();
    }

    static boolean processInstruction(String[] instr) {
        char arg1 = instr[1].charAt(0);
        if ("inp".equals(instr[0])) {
            long val = nextInput();
            vars.put(arg1, val);
            return true;
        }
        long arg1Value = vars.get(arg1);
        char c = instr[2].charAt(0);
        long arg2Value = c == 'x' || c == 'y' || c == 'z' || c == 'w' ?
                         vars.get(c) : Long.valueOf(instr[2]);
        long result = 0;
        if ("add".equals(instr[0])) {
            result = arg1Value + arg2Value;
        } else if ("mul".equals(instr[0])) {
            result = arg1Value * arg2Value;
        } else if ("div".equals(instr[0])) {
            result = arg1Value / arg2Value;
        } else if ("mod".equals(instr[0])) {
            result = arg1Value % arg2Value;
        } else if ("eql".equals(instr[0])) {
            result = arg1Value == arg2Value ? 1 : 0;
        } else {
            System.out.println("Unknown instruction: " + instr[0]);
            return false;
        }
        vars.put(arg1, result);
        return true;
    }

    static void processInstructions() {
        for (String[] instruction : instructions) {
            if (!processInstruction(instruction)) {
                break;
            }
        }
    }

    static void initInputs(int[] list) {
        inputs = new LinkedList<Integer>();
        for (Integer value : list) {
            inputs.add(value);
        }
        vars.put('w', 0L);
        vars.put('x', 0L);
        vars.put('y', 0L);
        vars.put('z', 0L);
    }

    static void processModelNumber(int[] modelNumber) {
        initInputs(modelNumber);
        processInstructions();
        long result = vars.get('z');
        System.out.print("Final result: " + result);
        if (result == 0) {
            System.out.println(" - valid model number !");
        } else {
            System.out.println(" - invalid model number");
        }
    }

    static void findConstants() {
        for (int i = 0; i < instructions.size(); i += 18) {
            int[] values = { Integer.valueOf(instructions.get(i + 4)[2]),
                             Integer.valueOf(instructions.get(i + 5)[2]),
                             Integer.valueOf(instructions.get(i + 15)[2])};
            constants.add(values);
        }
    }

    static int[] generateModelNumber(boolean firstStar) {
        int[] digits = new int[14];
        Stack<Point> prevOnes = new Stack<Point>();
        for (int i = 0; i < constants.size(); i++) {
            int a = constants.get(i)[0];
            int b = constants.get(i)[1];
            int c = constants.get(i)[2];
            if (a == 1) {
                prevOnes.push(new Point(i, c));
            } else {
                Point prevOne = prevOnes.pop();
                int prevI = prevOne.x;
                int prevC = prevOne.y;
                int complement = prevC + b;
                digits[prevI] = firstStar ? Math.min(9, 9 - complement) : Math.max(1, 1 - complement);
                digits[i] = digits[prevI] + complement;
            }
        }
        System.out.print("Model number: ");
        for (int i = 0; i < digits.length; i++) {
            System.out.print(digits[i]);
        }
        System.out.println();
        return digits;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 24");
        readFile(new File("data/day24.txt"));
        findConstants();
        int[] largest = generateModelNumber(true);
        processModelNumber(largest);
        int[] smallest = generateModelNumber(false);
        processModelNumber(largest);
    }
}
