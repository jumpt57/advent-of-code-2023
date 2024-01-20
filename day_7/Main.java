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
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws Exception {

        var main = new Main();
        var hands = main.extractHands("full.txt");

        var handsWithScore = hands.stream()
                .map(hand -> Map.entry(hand[0], main.handValue(hand[0])))
                .map(entry -> Map.entry(entry.getKey(),
                        main.foundHigherHandsPowerWithJoker(entry.getKey(), entry.getValue())))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        System.out.println("");
        System.out.println("Before Sorting :");
        hands.forEach(hand -> System.out
                .println(String.format("For hand %s %s", hand[0], hand[1])));
        System.out.println("");

        hands.sort((hand1, hand2) -> {

            System.out.println(String.format("Comparing %s with %s", hand1[0], hand2[0]));

            var score1 = handsWithScore.get(hand1[0]);
            var score2 = handsWithScore.get(hand2[0]);

            if (score1 == score2) {

                System.out.println(String.format("%s with %s are equals", hand1[0], hand2[0]));

                for (int i = 0; i < hand1[0].length(); i++) {

                    var char1 = String.valueOf(hand1[0].charAt(i));
                    var char2 = String.valueOf(hand2[0].charAt(i));

                    System.out.println(String.format("Comparing %s with %s", char1, char2));

                    var subScore1 = main.power(char1);
                    var subScore2 = main.power(char2);

                    if (subScore1 != subScore2) {
                        var result = subScore1.compareTo(subScore2);

                        System.out.println(String.format("%s is %s %s", char1,
                                result == 0 ? "equal" : result == 1 ? "supp" : "infer", char2));

                        return result;
                    }
                }
            }

            System.out.println("");

            return score1.compareTo(score2);
        });

        System.out.println("");
        System.out.println("After Sorting :");
        hands.forEach(hand -> System.out
                .println(String.format("For hand %s %s", hand[0], hand[1])));

        Integer result = IntStream.range(0, hands.size())
                .map(i -> Integer.parseInt(hands.get(i)[1]) * (i + 1))
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
            case "J":
                return 1;
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
            case "Q":
                return 11;
            case "K":
                return 12;
            case "A":
                return 13;
            default:
                throw new IllegalArgumentException(String.format("Card %s is unknown", card));
        }
    }

    private Integer handValue(String hand) {

        var cards = List.of(hand.split(""));

        var score = 0;

        if (isFiveOfKind(cards)) {
            System.out.println(String.format("Hand %s is five of kind, score is 7", hand));
            score = 7;
        } else if (isFourOfKind(cards)) {
            System.out.println(String.format("Hand %s is four of kind, score is 6", hand));
            score = 6;
        } else if (isFullHouse(cards)) {
            System.out.println(String.format("Hand %s is full house, score is 5", hand));
            score = 5;
        } else if (isThreeOfKind(cards)) {
            System.out.println(String.format("Hand %s is three of kind, score is 4", hand));
            score = 4;
        } else if (isTwoPairs(cards)) {
            System.out.println(String.format("Hand %s is two pairs, score is 3", hand));
            score = 3;
        } else if (isOnePair(cards)) {
            System.out.println(String.format("Hand %s is one pair, score is 2", hand));
            score = 2;
        } else if (isHighCard(cards)) {
            System.out.println(String.format("Hand %s is high card, score is 1", hand));
            score = 1;
        } else {
            System.out.println(String.format("Hand %s is something else", hand));
        }

        return score;
    }

    private Integer foundHigherHandsPowerWithJoker(String hand, final Integer currentScore) {

        if (!hand.contains("J")) {
            return currentScore;
        }

        var cards = List.of(hand.split(""));
        var localCards = cards.stream().collect(Collectors.joining(""));
        var jokerCardsCount = Collections.frequency(cards, "J");
        var maxScore = currentScore;

        Optional<Map.Entry<String, Integer>> opHigherFrequencyCard = cards.stream()
                .filter(card -> {
                    var result = card.compareTo("J") != 0;
                    return result;
                })
                .map(card -> Map.entry(card, Collections.frequency(cards, card)))
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .findFirst();

        if (opHigherFrequencyCard.isPresent()) {

            var higherFrequencyCard = opHigherFrequencyCard.get();

            for (int i = 0; i < jokerCardsCount; i++) {
                var newHand = localCards.replace("J", higherFrequencyCard.getKey());
                var newScore = handValue(newHand);
    
                if (newScore > maxScore) {
                    maxScore = newScore;
                }
            }
    
            System.out.println(String.format("With Joker hand %s has power of %d", localCards, maxScore));
            System.out.println("");
    
            return maxScore;

        } 

        if (hand.compareTo("JJJJJ") == 0) {
            return handValue("AAAAA");
        }

        opHigherFrequencyCard.orElseThrow();

        return 0;
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