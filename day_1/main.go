package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {

    readFile, err := os.Open("./full.txt")

	if err != nil {
		panic(err)
	}

	fileScanner := bufio.NewScanner(readFile)
    fileScanner.Split(bufio.ScanLines)

	master_total := 0

	for fileScanner.Scan() {

		line := fileScanner.Text()

		re := regexp.MustCompile("[0-9]+")
		only_numbers := re.FindAllString(line, -1)

		splitted_numbers := []string{}
		for _, v := range only_numbers {
			for _, v2 := range strings.Split(v, "") {
				splitted_numbers = append(splitted_numbers,v2)
				fmt.Println("Char is ", strings.Split(v2, ""))
			}
		}

		fmt.Println("Array = ", splitted_numbers)

		first := splitted_numbers[0]
		last := splitted_numbers[len(splitted_numbers) - 1]

		total := first + last

		fmt.Println("First : ", first, " Last : ", last, " total = ", "total = ", total)

		as_number, _ := strconv.ParseInt(total, 10, 64)

		master_total += int(as_number)

		
	}

	readFile.Close()

	fmt.Println(master_total)
}