import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Day19 {

    static List<List<int[]>> scanners = new ArrayList<List<int[]>>();
    static Transformation[][] transitions;

    static class Transformation {
        Transformation(int[] deltas, int flipState, int[] permutation) {
            this.deltas = deltas;
            this.flipState = flipState;
            this.permutation = permutation;
        }
        int[] deltas;
        int flipState;
        int[] permutation;
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        boolean numbers = false;
        List<int[]> scanner = null;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            if (line.length() == 0) {
                numbers = false;
            } else if (numbers) {
                String[] tokens = line.split(",");
                int[] xyz = {Integer.valueOf(tokens[0]), Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2])};
                scanner.add(xyz);
            } else {
                String[] tokens = line.split("\\s+");
                scanner = new ArrayList<int[]>();
                scanners.add(scanner);
                numbers = true;
            }
        }
        System.out.println("Total of " + scanners.size() + " scanners");
    }

    static void showBeacon(String message, int[] beacon) {
        System.out.println(message + " (" + beacon[0] + ", " + beacon[1] + ", " + beacon[2] + ")");
    }

    static void showBeacons(String message, List<int[]> beacons) {
        for (int[] beacon : beacons) {
            showBeacon(message, beacon);
        }
    }

    static boolean containsBeacon(List<int[]> refBeacons, int[] beacon) {
        for (int[] refBeacon : refBeacons) {
            if (refBeacon[0] == beacon[0] &&
                refBeacon[1] == beacon[1] &&
                refBeacon[2] == beacon[2]) {
                return true;
            }
        }
        return false;
    }

    static List<int[]> shiftAndMatchBeacons(List<int[]> refBeacons, List<int[]> beacons, int[] deltas) {
        List<int[]> commonBeacons = new ArrayList<int[]>();
        for (int[] beacon : beacons) {
            int[] shifted = beacon.clone();
            for (int i = 0; i < 3; i++) {
                shifted[i] += deltas[i];
            }
            if (containsBeacon(refBeacons, shifted)) {
                commonBeacons.add(shifted);
            }
        }
        return commonBeacons;
    }

    static List<int[]> shiftPositions(List<int[]> beacons, int[] deltas) {
        List<int[]> shiftedBeacons = new ArrayList<int[]>();
        for (int[] beacon : beacons) {
            int[] shifted = beacon.clone();
            for (int i = 0; i < 3; i++) {
                shifted[i] += deltas[i];
            }
            shiftedBeacons.add(shifted);
        }
        return shiftedBeacons;
    }

    static List<int[]> applyTransformation(Transformation transition, List<int[]> beacons) {
        List<int[]> permutedBeacons = permuteAxis(beacons, transition.permutation);
        List<int[]> flippedBeacons = flipAxis(permutedBeacons, transition.flipState);
        return shiftPositions(flippedBeacons, transition.deltas);
    }

    static void intersectBeacons(int refIndex, int compIndex, List<int[]> compBeacons,
                                 int[] permutation, int flipState) {
        List<int[]> refBeacons = scanners.get(refIndex);
        for (int[] refBeacon : refBeacons) {
            for (int[] compBeacon : compBeacons) {
                int[] deltas = new int[3];
                for (int i = 0; i < 3; i++) {
                    deltas[i] = refBeacon[i] - compBeacon[i];
                }
                List<int[]> commonBeacons = shiftAndMatchBeacons(refBeacons, compBeacons, deltas);
                if (commonBeacons.size() >= 12) {
                    System.out.println("Found 12 or more common beacons (" + commonBeacons.size() + ") stop searching.");
                    Transformation transition = new Transformation(deltas, flipState, permutation);
                    transitions[compIndex][refIndex] = transition;
//                    showBeacon("Permutation: ", permutation);
//                    System.out.println("Flip state: " + Integer.toBinaryString(flipState));
                    showBeacon("Scanner " + compIndex + " position relative to scanner " + refIndex, deltas);
//                    showBeacons("Common beacon relative to scanner " + refIndex, commonBeacons);
                    return;
                }
            }
        }
    }

    static List<int[]> flipAxis(List<int[]> beacons, int flipState) {
        List<int[]> flipped = new ArrayList<int[]>();
        for (int[] beacon : beacons) {
            flipped.add(beacon.clone());
        }
        for (int[] beacon : flipped) {
            if ((flipState & 4) != 0) {
                beacon[0] = - beacon[0];
            }
            if ((flipState & 2) != 0) {
                beacon[1] = - beacon[1];
            }
            if ((flipState & 1) != 0) {
                beacon[2] = - beacon[2];
            }
        }
        return flipped;
    }

    static void intersectFlippedBeacons(int refIndex, int compIndex, List<int[]> compBeacons,
                                        int[] permutation) {
        for (int flipState = 0; flipState < 8; flipState++) {
            List<int[]> flippedBeacons = flipAxis(compBeacons, flipState);
            intersectBeacons(refIndex, compIndex, flippedBeacons, permutation, flipState);
        }
    }

    static List<int[]> permuteAxis(List<int[]> beacons, int[] permutation) {
        List<int[]> permuted = new ArrayList<int[]>();
        for (int[] beacon : beacons) {
            int[] p = new int[3];
            for (int i = 0; i < 3; i++) {
                p[i] = beacon[permutation[i]];
            }
            permuted.add(p);
        }
        return permuted;
    }

    static void intersectPermutedBeacons(int refIndex, int compIndex) {
        int[][] permutations = { {0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0} };
        List<int[]> compBeacons = scanners.get(compIndex);
        for (int[] permutation : permutations) {
            List<int[]> permutedBeacons = permuteAxis(compBeacons, permutation);
            intersectFlippedBeacons(refIndex, compIndex, permutedBeacons, permutation);
        }
    }

    static void compareScanners() {
        transitions = new Transformation[scanners.size()][scanners.size()];
        for (int i = 0; i < scanners.size(); i++) {
            for (int j = 0; j < scanners.size(); j++) {
                if (i != j) {
                    intersectPermutedBeacons(i, j);
                }
            }
        }
    }

    static List<int[]> transformToScanner0(int scanner, List<int[]> beacons, boolean[] visiting, int origin) {
        for (int j = 0; j < transitions[0].length; j++) {
            Transformation next = transitions[scanner][j];
            if (next == null || visiting[j]) {
                continue;
            }
            List<int[]> nextBeacons = applyTransformation(next, beacons);
            if (origin > 0) {
                showBeacons("Scanner " + origin + " position relative to scanner " + j, nextBeacons);
            }
            if (j == 0) {
                return nextBeacons;
            }
            visiting[j] = true;
            nextBeacons = transformToScanner0(j, nextBeacons, visiting, origin);
            if (nextBeacons != null) {
                return nextBeacons;
            }
            visiting[j] = false;
        }
        return null;
    }

    static List<int[]> calculateScannerPositions() {
        List<int[]> allScanners = new ArrayList<int[]>();
        for (int i = 1; i < transitions.length; i++) {
            System.out.println("Scanner " + i);
            for (int j = 0; j < transitions[i].length; j++) {
                Transformation start = transitions[i][j];
                if (start == null) {
                    continue;
                }
                showBeacon("Scanner " + i + " position relative to scanner " + j, start.deltas);
                if (j == 0) {
                    allScanners.add(start.deltas);
                } else {
                    List<int[]> scanner = transformToScanner0(j, Arrays.asList(start.deltas), new boolean[scanners.size()], i);
                    allScanners.add(scanner.get(0));
                }
                break;
            }
        }
        return allScanners;
    }

    static void calculateAllBeacons() {
        List<int[]> allBeacons = scanners.get(0);
        for (int i = 1; i < scanners.size(); i++) {
            List<int[]> nextBeacons = transformToScanner0(i, scanners.get(i), new boolean[scanners.size()], 0);
            for (int[] next : nextBeacons) {
                if (!containsBeacon(allBeacons, next)) {
                    allBeacons.add(next);
                }
            }

        }
        System.out.println("Total number of beacons: " + allBeacons.size());
    }

    static int maxManhattan(List<int[]> beacons) {
        int maxDistance = 0;
        for (int i = 0; i < beacons.size(); i++) {
            int[] beacon = beacons.get(i);
            for (int j = i + 1; j < beacons.size(); j++) {
                int d = Math.abs(beacon[0] - beacons.get(j)[0]) +
                        Math.abs(beacon[1] - beacons.get(j)[1]) +
                        Math.abs(beacon[2] - beacons.get(j)[2]);
                maxDistance = Math.max(maxDistance, d);
            }
        }
        return maxDistance;
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 19");
        readFile(new File("data/day19.txt"));
        compareScanners();
        List<int[]> allScanners = calculateScannerPositions();
        showBeacons("All scanners:", allScanners);
        calculateAllBeacons();
        System.out.println("Max manhattan distance: " + maxManhattan(allScanners));
    }
}
