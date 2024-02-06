import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.stream.Collectors;


void main() throws IOException {
    aoc("input3.txt");
    aoc("full.txt");
}

void aoc(String fileName) throws IOException {

    Path path = Paths.get(fileName);

    var lines = Files.readAllLines(path);

    var steps = lines.getFirst();
    var rest = lines.subList(2, lines.size());

    var network = new HashMap<String, String[]>();
    for (String line : rest) {
        var s = line.split(" = ");
        network.put(s[0], s[1].substring(1, s[1].length() -1).split(", "));
    }

    var positions = network.keySet().stream().filter(key -> key.endsWith("A")).toList();
    var cycles = new ArrayList<ArrayList<Integer>>();

    for (String current : positions) {

        var cycle = new ArrayList<Integer>();

        var current_steps = steps;
        var step_count = 0;
        String first_z = null;

        while (true) {

            while (step_count == 0 || !current.endsWith("Z")) {
                step_count++;

                current = network.get(current)[current_steps.charAt(0) == 'L' ? 0 : 1];
                current_steps = current_steps.substring(1) + current_steps.charAt(0);
            }

            cycle.add(step_count);

            if (first_z == null) {
                first_z = current;
                step_count = 0;
            } else if (first_z.equals(current)) {
                break;
            }
        }

        cycles.add(cycle);
    }

    var numsList = cycles.stream().map(ArrayList::getFirst).map(BigInteger::valueOf).toList();
    var nums = new Stack<BigInteger>();
    nums.addAll(numsList);

    BigInteger lcm = nums.pop();

    for (BigInteger num : nums) {
        lcm = lcm.multiply(num).divide(lcm.gcd(num));
    }


    System.out.printf("%d \n", lcm);
}