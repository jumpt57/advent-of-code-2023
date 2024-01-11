use std::{cmp, collections::HashMap, fs::read_to_string};

fn main() {
    let extract_game_number = |chunk: &str| -> i32 {
        chunk
            .split(":")
            .find(|chunk| chunk.contains("Card"))
            .unwrap()
            .split(" ")
            .last()
            .unwrap()
            .to_string()
            .parse()
            .unwrap()
    };

    let extract_list_of_numbers = |chunk: &str| -> Vec<i32> {
        chunk
            .trim()
            .split(" ")
            .map(|chunk| chunk.trim())
            .filter(|chunk| !chunk.is_empty())
            .map(|chunk| chunk.parse().unwrap())
            .collect()
    };

    let filename: &str = "./src/full.txt";

    let result = read_to_string(filename);

    match result {
        Ok(v) => {
            let lines: Vec<String> = v.lines().map(String::from).collect();

            let mut aggregate_map: HashMap<i32, i32> = HashMap::new();

            for line in &lines {
                aggregate_map.insert(extract_game_number(&line), 1);
            }

            println!("{:?} size {}", lines, lines.len());

            println!("-----------");

            let mut i = 0;
            loop {
                let line = lines.get(i).unwrap().clone();

                let game_id = extract_game_number(&line);

                println!("Game Id is {} [{}]", game_id, i);

                let raw: Vec<_> = line
                    .split(&['|', ':'])
                    .filter(|chunk| !chunk.contains("Card"))
                    .collect();

                println!("Raw : {:?}", raw);

                let card_winning_numbers: Vec<i32> = extract_list_of_numbers(raw.first().unwrap());
                let my_numbers: Vec<i32> = extract_list_of_numbers(raw.last().unwrap());

                println!("Card wining numbers are : {:?}", card_winning_numbers);
                println!("My numbers are : {:?}", my_numbers);

                let common_numbers: Vec<i32> = my_numbers
                    .into_iter()
                    .filter(|number| card_winning_numbers.contains(number))
                    .collect();

                println!("Commons are : {:?}", common_numbers);
                println!("How many cards bellow to get : {:?}", common_numbers.len());

                if common_numbers.len() > 0 {
                    let start = cmp::min(i + 1, lines.len());
                    let end = cmp::min(i + 1 + common_numbers.len(), lines.len());

                    println!("Will get from {} to {}", start, end);

                    let cards_to_add: &Vec<String> = &lines[start..end].to_vec();

                    let cards_to_add_numb: Vec<i32> = cards_to_add
                        .iter()
                        .map(|chunk| extract_game_number(chunk))
                        .collect();

                    println!("Cards to add : {:?}", cards_to_add_numb);

                    for card_to_add in cards_to_add_numb {
                        let mut card_muliplicator = aggregate_map.get(&card_to_add).unwrap().clone();
                        card_muliplicator = card_muliplicator + 1 * aggregate_map.get(&game_id).unwrap();

                        println!("For card {} new multiplicator is {}", card_to_add, card_muliplicator);

                        aggregate_map.insert(card_to_add, card_muliplicator);
                    }
                }

                println!("Aggregate is {:?}", aggregate_map);

                println!("------------");

                i += 1;
                if i == lines.len() {
                    break;
                }
            }

            let all_scores = aggregate_map.values().cloned().collect::<Vec<i32>>();
            let sum_scores: i32 = all_scores.iter().sum();

            println!("Total is {:?}", sum_scores);
        }
        Err(e) => {
            print!("Error with file : {}", e)
        }
    };
}
