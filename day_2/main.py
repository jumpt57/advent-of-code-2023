from decimal import Decimal
from itertools import chain
import re

LOGGING = False

def ex2(red: int, green: int, blue: int):

    total = 0

    with open('full.txt', 'r') as file:
        lines = file.readlines()
        for line in lines:
            line_without_n = line.replace("\n", "")
            print(line_without_n)

            data =  line_without_n.split(":")

            game_id = data[0].split(" ")[1]
            print(f"Game ID is {game_id}")

            game_sets = list(map(lambda set: set.replace(" ", "").split(","), re.split(';', data[1])))
            print(f"Sets : {game_sets}")

            total_game = 0

            max_blue = 0
            max_red = 0
            max_green = 0
            for set in game_sets:

                for dice in set:
                    if "blue" in dice:
                        max_blue = max(int(dice.replace("blue", "")), max_blue)
                    elif "red" in dice:
                        max_red = max(int(dice.replace("red", "")), max_red)
                    elif "green" in dice:
                        max_green = max(int(dice.replace("green", "")), max_green)
                    else:
                        raise Exception("Wrong color")
                    
                print(f"b{max_blue}, r{max_red}, g{max_green}")

                total_game = max_red * max_green * max_blue

                print(f"Total for this game : {total_game}")

        
                    
            total = total + total_game

            print("-----------")


    return total

def ex1(red: int, green: int, blue: int):

    total = 0

    with open('input1.txt', 'r') as file:
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

def extract_values_for_color(line: list[str], color: str) -> list[int]:
    color_as_int = extract_numbers_from_line(line, color)
    color_as_int.sort(reverse=True)

    print(f"Permutation for {color} : {color_as_int}")

    return color_as_int

def extract_sum_of_color(line: list[str], color: str) -> int:

    color_as_int = extract_numbers_from_line(line, color)

    color_sum = max(Decimal(i) for i in color_as_int)
    if LOGGING : print(f"{color} sum is {color_sum}")
    
    if color_sum is None:
        return 0
    else:
        return color_sum
    
def extract_numbers_from_line(line: list[str], color: str) -> list[int]:
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

    return color_as_int

def generate_minimum_possibility(game_id: str, reds: list[int], greens: list[int], blues: list[int], max_red: int, max_green: int, max_blue: int) -> dict | None:
    for red in reds:
        for green in greens:
            for blue in blues :
                print(f"Trying permutation r{red}, g{green}, b{blue}")
                if red > max_red or green > max_green or blue > max_blue:
                    print(f"Game {game_id} not possible !")
                    continue
                else :
                    print(f"Game {game_id} is possible")

                    return {
                        "red": red,
                        "green": green,
                        "blue":  blue
                    }

    return None

if __name__ == "__main__":
    print(ex2(12, 13, 14))