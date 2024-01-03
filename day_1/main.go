package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {

	digits_map := map[string]int{
		"one":   1,
		"two":   2,
		"three": 3,
		"four":  4,
		"five":  5,
		"six":   6,
		"seven": 7,
		"eight": 8,
		"nine":  9,
	}

	readFile, err := os.Open("./full.txt")

	if err != nil {
		panic(err)
	}

	fileScanner := bufio.NewScanner(readFile)
	fileScanner.Split(bufio.ScanLines)

	master_total := 0

	for fileScanner.Scan() {

		line := fileScanner.Text()

		fmt.Println("Line is :", line)

		new_line := ""

		str := ""
		for _, v := range strings.Split(line, "") {

			if isNumber(v) {
				fmt.Println("It is a number ! ", v)
				new_line = new_line + v
				str = ""
				continue
			} else {
				str = str + v
			}

			fmt.Println("Will try with ", str)

			if val, ok := digits_map[str]; ok {

				fmt.Println("Found a match  with ", val, " !")
				new_line = new_line + strconv.Itoa(val)

				// Find if last char is the start of other number
				last_letter := str[len(str)-1:]

				found := false
				for number := range digits_map {
					has_suffix := strings.HasPrefix(number, last_letter)
					//fmt.Println("Is this ", str, " a suffix of ", s, " ? ", has_prefix)
					if has_suffix {
						fmt.Println("Last leter ", last_letter, " is the start of ", number)
						found = true
						break
					}
				}

				if found {
					str = last_letter
				} else {
					str = ""
				}

			} else {
				if isNumber(str) {
					// it is a number !
					fmt.Println("It is a number ! ", str)
					new_line = new_line + str
					str = ""
				} else {
					//fmt.Println("Not find and not number for str ", str)
					found := false
					for s := range digits_map {
						has_prefix := strings.HasPrefix(s, str)
						//fmt.Println("Is this ", str, " a prefix of ", s, " ? ", has_prefix)
						if has_prefix {
							found = true
							break
						}
					}

					if !found {
						//fmt.Println("It is not a prefix of something ")
						if len(str) > 1 {
							// If not a prefix but has a letter before we remove it
							str = str[1:]
						} else {
							str = ""
						}
					}
				}
				continue
			}
		}

		fmt.Println("Line after is ", line)

		splitted_numbers := []string{}
		for _, v := range strings.Split(new_line, "") {
			for _, v2 := range strings.Split(v, "") {
				splitted_numbers = append(splitted_numbers, v2)
				fmt.Println("Char is ", v2)
			}
		}

		fmt.Println("Array = ", splitted_numbers)

		first := splitted_numbers[0]
		last := splitted_numbers[len(splitted_numbers)-1]

		total := first + last

		fmt.Println("First : ", first, " Last : ", last, "total = ", total)

		as_number, _ := strconv.ParseInt(total, 10, 0)

		master_total += int(as_number)

		fmt.Println("----------------------")
	}

	readFile.Close()

	fmt.Println(master_total)
}

func isNumber(value string) bool {
	if _, err := strconv.ParseInt(value, 10, 64); err == nil {
		return true
	}

	return false
}
