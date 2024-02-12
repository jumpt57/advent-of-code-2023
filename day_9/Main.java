import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

void main() throws IOException {
    aoc("test.txt");
    aoc("input.txt");
    aoc("full.txt");
}

void aoc(String fileName) throws IOException {

    Path path = Paths.get(fileName);

    var lines = Files.readAllLines(path);

    var histories = lines.stream()
            .map(line -> line.split(" "))
            .map(history -> Arrays.stream(history)
                    .map(Integer::valueOf)
                    .collect(Collectors.toCollection(ArrayList::new)))
            .toList();

    var acc = 0;

    for (List<Integer> history : histories) {
        List<List<Integer>> accumulator = new ArrayList<>();
        accumulator.add(history);
        accumulator = arrayDiff(accumulator, history);

        accumulator = accumulator.reversed();

        var lastValues = accumulator.stream()
                .map(List::getLast)
                .toList();

        int sum = lastValues.stream()
                .reduce(0, Integer::sum);

        acc = acc + sum;
    }

    System.out.println(acc);

}

List<List<Integer>> arrayDiff(List<List<Integer>> accumulator, List<Integer> values) {

    var diff = new ArrayList<Integer>();
    var previous = values.getFirst();

    for (Integer value : values.subList(1, values.size())) {
        diff.add(value - previous);
        previous = value;
    }

    accumulator.add(diff);

    if (Collections.frequency(diff, 0) != diff.size()) {
        arrayDiff(accumulator, diff);
    }

    return accumulator;
}