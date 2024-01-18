import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws Exception {

        var main = new Main();
        var hands = main.extractHands("full.txt");

        var handsWithScore = hands.stream()
                .map(hand -> Map.entry(hand[0], main.handTypeValue(hand[0])))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        System.out.println("");
        System.out.println("Before Sorting :");
        hands.forEach(hand -> System.out
                .println(String.format("For hand %s score is %d", hand[0], handsWithScore.get(hand[0]))));

        hands.sort((hand1, hand2) -> {

            var score1 = handsWithScore.get(hand1[0]);
            var score2 = handsWithScore.get(hand2[0]);

            if (score1 == score2) {
                for (int i = 0; i < hand1[0].length(); i++) {

                    var subScore1 = main.power(String.valueOf(hand1[0].charAt(i)));
                    var subScore2 = main.power(String.valueOf(hand2[0].charAt(i)));

                    if (subScore1 != subScore2) {
                        return subScore1.compareTo(subScore2);
                    }
                }
            }

            return score1.compareTo(score2);
        });

        System.out.println("");
        System.out.println("After Sorting :");
        hands.forEach(hand -> System.out
                .println(String.format("For hand %s score is %d", hand[0], handsWithScore.get(hand[0]))));

        Integer result = IntStream.range(0, hands.size())
                .map(i -> {
                    return Integer.parseInt(hands.get(i)[1]) * (i + 1);
                })
                .reduce(0, Integer::sum);

        System.out.println("");
        System.out.println(String.format("Result is %d", result));
    }

    private List<String[]> extractHands(String fileName) throws Exception {
        Path path = Paths.get(Main.class.getClassLoader().getResource(fileName).toURI());

        try (Stream<String> lines = Files.lines(path)) {
            return lines.map(line -> line.split(" "))
                    .collect(Collectors.toList());
        }

    }

    private Integer power(String card) {
        switch (card) {
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "T":
                return 10;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            case "A":
                return 14;
            default:
                throw new IllegalArgumentException(String.format("Card %s is unknown", card));
        }
    }

    /*
     * 
     * Five of a kind, where all five cards have the same label: AAAAA
     * Four of a kind, where four cards have the same label and one card has a
     * different label: AA8AA
     * Full house, where three cards have the same label, and the remaining two
     * cards share a different label: 23332
     * Three of a kind, where three cards have the same label, and the remaining two
     * cards are each different from any other card in the hand: TTT98
     * Two pair, where two cards share one label, two other cards share a second
     * label, and the remaining card has a third label: 23432
     * One pair, where two cards share one label, and the other three cards have a
     * different label from the pair and each other: A23A4
     * High card, where all cards' labels are distinct: 23456
     * 
     * 
     */
    private Integer handTypeValue(String hand) {

        var cards = List.of(hand.split(""));

        if (isFiveOfKind(cards)) {
            var score = 7; // * scoreOfCards(cards);
            System.out.println(String.format("Hand %s is five of kind, score is %d", hand, score));
            return score;
        } else if (isFourOfKind(cards)) {
            var score = 6; // * scoreOfCards(cards);
            System.out.println(String.format("Hand %s is four of kind, score is %d", hand, score));
            return score;
        } else if (isFullHouse(cards)) {
            var score = 5; // * scoreOfCards(cards);
            System.out.println(String.format("Hand %s is full house, score is %d", hand, score));
            return score;
        } else if (isThreeOfKind(cards)) {
            var score = 4; // * scoreOfCards(cards);
            System.out.println(String.format("Hand %s is three of kind, score is %d", hand, score));
            return score;
        } else if (isTwoPairs(cards)) {
            var score = 3; // * scoreOfCards(cards);
            System.out.println(String.format("Hand %s is two pairs, score is %d", hand, score));
            return score;
        } else if (isOnePair(cards)) {
            var score = 2; // * scoreOfCards(cards);
            System.out.println(String.format("Hand %s is one pair, score is %d", hand, score));
            return score;
        } else if (isHighCard(cards)) {
            var score = 1; // * scoreOfCards(cards);
            System.out.println(String.format("Hand %s is high card, score is %d", hand, score));
            return score;
        } else {
            System.out.println(String.format("Hand %s is something else", hand));
        }

        return 0;
    }

    private Integer scoreOfCards(List<String> cards) {
        return cards.stream()
                .map(card -> power(card))
                .reduce(0, Integer::sum);
    }

    private Boolean isFiveOfKind(List<String> cards) {
        return cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 5)
                .distinct()
                .collect(Collectors.toList()).size() == 1;
    }

    private Boolean isFourOfKind(List<String> cards) {
        return cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 4)
                .distinct()
                .collect(Collectors.toList())
                .size() == 1;
    }

    private Boolean isFullHouse(List<String> cards) {

        var found1IterationOf3 = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 3)
                .distinct()
                .collect(Collectors.toList())
                .size() == 1;

        var found2IterationOf1 = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 1)
                .distinct()
                .collect(Collectors.toList())
                .size() == 2;

        return found1IterationOf3 && !found2IterationOf1;
    }

    private Boolean isThreeOfKind(List<String> cards) {
        var found1IterationOf3 = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 3)
                .distinct()
                .collect(Collectors.toList())
                .size() == 1;

        var found2IterationOf1 = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 1)
                .distinct()
                .collect(Collectors.toList())
                .size() == 2;

        return found1IterationOf3 && found2IterationOf1;
    }

    private Boolean isTwoPairs(List<String> cards) {
        var found2Pair = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 2)
                .distinct()
                .collect(Collectors.toList())
                .size() == 2;

        var found1IterationOf1 = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 1)
                .distinct()
                .collect(Collectors.toList())
                .size() == 1;

        return found2Pair && found1IterationOf1;
    }

    private Boolean isOnePair(List<String> cards) {
        var found1Pair = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 2)
                .distinct()
                .collect(Collectors.toList())
                .size() == 1;

        var found3IterationOf1 = cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 1)
                .distinct()
                .collect(Collectors.toList())
                .size() == 3;

        return found1Pair && found3IterationOf1;
    }

    private Boolean isHighCard(List<String> cards) {
        return cards.stream()
                .filter(card -> Collections.frequency(cards, card) == 1)
                .distinct()
                .collect(Collectors.toList())
                .size() == 5;
    }

}