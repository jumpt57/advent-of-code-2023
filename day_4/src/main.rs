use std::fs::read_to_string;

fn main() {

    let filename: &str = "./src/full.txt";

    let result = read_to_string(filename);

    match result  {
        Ok(v) => {

            let total = v.lines().map(|line| {

                let id = line.split(":")
                    .find(|chunk| chunk.contains("Card"))
                    .unwrap()
                    .split(" ")
                    .last()
                    .unwrap();

                println!("Id is {}", id);

                let raw : Vec<_> = line.split(&['|', ':'])
                    .filter(|chunk| !chunk.contains("Card"))
                    .collect();

                println!("Raw : {:?}", raw);

                let extract_numbers = |numb_as_str: &str| -> Vec<i32> { 
                    numb_as_str.trim().split(" ")
                        .map(|chunk| chunk.trim())
                        .filter(|chunk| !chunk.is_empty())
                        .map(|chunk| chunk.parse().unwrap())
                        .collect()
                };

                let card_winning_numbers: Vec<i32> = extract_numbers(raw.first().unwrap());
                let my_numbers: Vec<i32> = extract_numbers(raw.last().unwrap());

                println!("Card wining numbers are : {:?}", card_winning_numbers);
                println!("My numbers are : {:?}", my_numbers);

                let common_numbers: Vec<i32> = my_numbers.into_iter()
                    .filter(|number| card_winning_numbers.contains(number))
                    .collect();

                println!("Commons are : {:?}", common_numbers);

                let result = common_numbers.into_iter()
                    .fold(0, |acc, e| if acc == 0 { acc + 1 } else {acc * 2});

                println!("Result is : {:?}", result);

                println!("------------");

                result
    
            }).reduce(|acc, e| acc + e);

            println!("Total is {}", total.unwrap())
        },
        Err(e) => {
            print!("Error with file : {}", e)
        }
    };
}
