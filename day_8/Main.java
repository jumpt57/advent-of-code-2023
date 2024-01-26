import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

void main() throws IOException {

    Path path = Paths.get("full.txt");

    try (var data = Files.lines(path)) {

        var lines = data.collect(Collectors.toList());
        var instructions = lines.removeFirst();

        lines.removeFirst(); // Remove second empty line

        var currentNode = lines.stream().filter(line -> line.substring(0, 3).compareTo("AAA") == 0).findFirst().orElseThrow();
        var steps = 1;

        for (int i = 0; i <= instructions.length() - 1; i++) {

            var instruction = instructions.charAt(i);

            System.out.println(instruction);

            var choices = currentNode.split(" = ")[1].split(", ");

            var nextNode = switch (instruction) {
                case 'L' -> choices[0].replace("(", "");
                case 'R'  -> choices[1].replace(")", "");
                default -> throw new IllegalStateException(STR."Instruction \{instruction} is unknown");
            };

            System.out.println(STR."Next node is \{nextNode}");

            if  ("ZZZ".compareTo(nextNode) == 0) break;

            currentNode = lines.stream().filter(line -> line.substring(0, 3).compareTo(nextNode) == 0).findFirst().orElseThrow();
            steps++;

            if (i == instructions.length() - 1) {
                i = -1;
            }
        }

        System.out.println(STR."Steps \{steps}");
    }
}