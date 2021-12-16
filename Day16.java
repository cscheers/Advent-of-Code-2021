import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class Day16 {

    static String code;
    static int hexIndex = 0;
    static int bitIndex = 0;
    static int versionSum;

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            code = scanner.nextLine();
        }
    }

    static int getBits(int count) {
        int value = (byte)Character.digit(code.charAt(hexIndex), 16);
        value &= 0b1111 >> bitIndex;
        if (count + bitIndex <= 4) {
            value >>= 4 - (count + bitIndex);
            bitIndex += count;
        } else {
            count -= 4 - bitIndex; // Remaining bits
            while (count > 4) {
                hexIndex++;
                int next = (byte)Character.digit(code.charAt(hexIndex), 16);
                count -= 4;
                value = (value << 4) + next;
            }
            hexIndex++;
            int next = (byte)Character.digit(code.charAt(hexIndex), 16);
            next >>= 4 - count;
            value = (value << count) + next;
            bitIndex = count;
        }
        if (bitIndex == 4) {
            hexIndex++;
            bitIndex = 0;
        }
        return value;
    }

    static long processOperations(int op, List<Long> args) {
        long result = 0;
        switch (op) {
            case 0 : // Sum
                for (long arg : args) {
                    result += arg;
                }
                break;
            case 1 : // Product
                result = 1;
                for (long arg : args) {
                    result *= arg;
                }
                break;
            case 2 : // Minimum
                result = args.get(0);
                for (long arg : args) {
                    result = Math.min(result, arg);
                }
                break;
            case 3 : // Maximum
                result = args.get(0);
                for (long arg : args) {
                    result = Math.max(result, arg);
                }
                break;
            case 5 : // Greater than
                result = args.get(0) > args.get(1) ? 1 : 0;
                break;
            case 6 : // Less than
                result = args.get(0) < args.get(1) ? 1 : 0;
                break;
            case 7 : // Equal
                result = args.get(0).equals(args.get(1)) ? 1 : 0;
                break;
            default :
                System.out.println("Operation not supported: " + op);
        }
        return result;
    }

    static class Pair {
        Pair(int bits, long value) {
            this.bits = bits;
            this.value = value;
        }
        int bits;
        long value;
    }

    static Pair processPacket() {
        int version = getBits(3);
        versionSum += version;
        int type = getBits(3);
        int bits = 6;
        long value = 0;
        if (type == 4) {
            int mask = 1 << 4;
            while (true) {
                int five = getBits(5);
                bits += 5;
                value = (value << 4) + (five & ~mask);
                if ((five & mask) == 0) {
//                    System.out.println("==> literal (" + value + ") " + Long.toBinaryString(value));
                    break;
                }
            }
        } else {
            List<Long> args = new ArrayList<Long>();
            int lengthId = getBits(1);
            if (lengthId == 0) {
                int requiredBits = getBits(15);
                int packetBits = 0;
                while (packetBits != requiredBits) {
                    Pair p = processPacket();
                    packetBits += p.bits;
                    args.add(p.value);
                }
                bits += requiredBits + 16;
            } else {
                int requiredPackets = getBits(11);
                for (; requiredPackets > 0; requiredPackets--) {
                    Pair p = processPacket();
                    bits += p.bits;
                    args.add(p.value);
                }
                bits += 12;
            }
            value = processOperations(type, args);
        }
        return new Pair(bits, value);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day16");
        readFile(new File("data/day16.txt"));

        Pair p = processPacket();
        System.out.println("Version sum: " + versionSum);
        System.out.println("Final result: " + p.value);
    }
}
