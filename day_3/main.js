const fs = require('node:fs');

fs.readFile('full.txt', 'utf8', (err, data) => {
  if (err) {
    console.error(err)
    return
  }

  var lines = data.split("\n")

  var matrix = lines.map(line => line.split(''))

  console.log(matrix)

  var matrixSize = {x: matrix.length - 1, y: matrix[0].length - 1}

  let total = 0

  for (var y = 0; y <= matrix.length - 1; y++) {

    for (var x = 0; x <= matrix[y].length - 1; x++) {

        var coordinate = {x, y}

        var char = getCharAtCoordonate(matrix, coordinate)

        if (char == "." || !isNaN(char)) {
            console.log("It is a dot or a number, will continue...")

        } else if (char == "*") {

            console.log("Contains gear")

            var numbers = getAdjacentNumbersForGears(matrix, matrixSize, coordinate)

            if (numbers.length == 2) {

                console.log(numbers)

                const ratio = numbers[0] * numbers[1]

                total = total + ratio
            }

        } else {
            // console.log("Contains symbol " + char)
            // var numbers = getAdjacentNumbers(matrix, matrixSize, coordinate)

            // console.log(numbers)

            // total = total + numbers.reduce((a, b) => a + b, 0)
        }

        console.log("-----------------") 
    }

  }

  console.log("total is " + total)

})

function getAdjacentNumbersForGears(matrix, matrixSize, coordinate){

    const actions = ["above", "above-l", "above-r", "below", "below-l", "below-r", "before", "after"]

    const numbers = actions.map(action => getNumber(matrix, matrixSize, coordinate, action))
        .filter(Boolean);
        
    return [...new Set(numbers)]
}

function getAdjacentNumbers(matrix, matrixSize, coordinate){

    const actions = ["above", "above-l", "above-r", "below", "below-l", "below-r", "before", "after"]

    const numbers = actions.flatMap(action => getNumber(matrix, matrixSize, coordinate, action))
        .filter(Boolean)

    return [...new Set(numbers)]
}

function getNumber(matrix, matrixSize, coordinate, action) {

    console.log("Looking " + action)

    try {
        let nextCoordinate = getNewCoordonate(action, coordinate, matrixSize)
        let char = getCharAtCoordonate(matrix, nextCoordinate);

        if (!isNaN(char)) {
            console.log(char + " is  a number")

            let number = ""

            let before = true
            while(before) {
                try {
                    nextCoordinate = getNewCoordonate("before", nextCoordinate, matrixSize)
                    char = getCharAtCoordonate(matrix, nextCoordinate);
                    before = !isNaN(char)
                    console.log("Before char is " + char)
                } catch (error) {
                    break
                }
            }

            console.log("Will recompose number now")

            let after = true
            char = getCharAtCoordonate(matrix, nextCoordinate)
            if(!isNaN(char)) {
                number = number + char
            }
            

            while(after) {
                try {
                    nextCoordinate = getNewCoordonate("after", nextCoordinate, matrixSize)
                    char = getCharAtCoordonate(matrix, nextCoordinate);

                    after = !isNaN(char)

                    if(after) {
                        number = number + char
                    }
                } catch (error) {
                    break
                }
            }

            console.log("Found number " + number)

            console.log("###########")

            return parseInt(number)
        }

    } catch (error) {
        return undefined
    }
}

function getNewCoordonate(action, coordinate, matrixSize) {
    switch(action) {
        case "above":
            var x = coordinate.x;
            var y = minusY(coordinate)
            return {x, y}
        case "above-l":
            var x = minusX(coordinate)
            var y = minusY(coordinate)
            return {x, y}
        case "above-r":
            var x = addX(coordinate, matrixSize)
            var y = minusY(coordinate)
            return {x, y}
        case "below":
            var x = coordinate.x
            var y = addY(coordinate, matrixSize)
            return {x, y}
        case "below-l":
            var x = minusX(coordinate)
            var y = addY(coordinate, matrixSize)
            return {x, y}
        case "below-r":
            var x = addX(coordinate, matrixSize)
            var y = addY(coordinate, matrixSize)
            return {x, y}
        case "before":
            var x = minusX(coordinate)
            var y = coordinate.y
            return {x, y}
        case "after":
            var x = addX(coordinate, matrixSize)
            var y = coordinate.y
            return {x, y}
    }
}

function getCharAtCoordonate(matrix, coordinate) {
    console.log("Get char at (x" + coordinate.x + ",y" + coordinate.y + ")")
    return matrix[coordinate.y][coordinate.x]
}

function addY(coordinate, matrixSize){
    if(coordinate.y + 1 > matrixSize.y) {
        throw new Error("Out of bound !")
    } else {
        return coordinate.y + 1
    }
}

function minusY(coordinate){
    if(coordinate.y - 1 < 0) {
        throw new Error("Out of bound !")
    } else {
        return coordinate.y - 1
    }
}

function addX(coordinate, matrixSize){
    if(coordinate.x + 1 > matrixSize.x) {
        throw new Error("Out of bound !")
    } else {
        return coordinate.x + 1
    }
}

function minusX(coordinate){
    if(coordinate.x - 1 < 0) {
        throw new Error("Out of bound !")
    } else {
        return coordinate.x - 1
    }
}