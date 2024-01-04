from decimal import Decimal
from itertools import chain
import re

LOGGING = False

def find(red: int, green: int, blue: int):

    total = 0

    with open('full.txt', 'r') as file:
        lines = file.readlines()
        for line in lines:
            line_without_n = line.replace("\n", "")
            print(line_without_n)


            game_id = line_without_n.split(":")[0].split(" ")[1]
            print(f"Game ID is {game_id}")

            splitted_line = re.split(', |; |:', line_without_n)

            red_count = extract_sum_of_color(splitted_line, 'red')
            green_count = extract_sum_of_color(splitted_line, 'green')
            blue_count = extract_sum_of_color(splitted_line, 'blue')

            print(f"red {red_count} green {green_count} blue {blue_count}")

            if red_count > red or green_count > green or blue_count > blue:
                print(f"Game {game_id} not possible !")
            else :
                print(f"Game {game_id} is possible")
                total = total + Decimal(game_id)


            print("-----------")


    return total

def extract_sum_of_color(line: list[str], color: str) -> int:
    colors = list(filter(lambda fragment: (color in fragment), line))
    if LOGGING : print(f"{color} are {colors}")
    splitted_color = list(map(lambda fragment: fragment.split(" "), colors))
    if LOGGING : print(f"{color} splitted is {splitted_color}")
    cleaned_color: list[str] = [j for i in splitted_color for j in i]
    if LOGGING : print(f"{color} cleaned is {cleaned_color}")
    color_only_numbers = list(filter(lambda fragment: fragment.isdigit(), cleaned_color))
    if LOGGING : print(f"{color} numbers is {color_only_numbers}")
    color_as_int = list(map(lambda fragment: int(fragment), color_only_numbers))
    if LOGGING : print(f"{color} int is {color_as_int}")
    color_sum = max(Decimal(i) for i in color_as_int)
    if LOGGING : print(f"{color} sum is {color_sum}")
    
    if color_sum is None:
        return 0
    else:
        return color_sum

if __name__ == "__main__":
    print(find(12, 13, 14))