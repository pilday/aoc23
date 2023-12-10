enum class Directions(val dir: Pair<Int, Int>) {
    NORTH(Pair(-1, 0)),
    SOUTH(Pair(1, 0)),
    WEST(Pair(0, -1)),
    EAST(Pair(0, 1))
}

val connectionsTo = mapOf(
        '|' to Pair(Directions.SOUTH, Directions.NORTH),
        '-' to Pair(Directions.WEST, Directions.EAST),
        'F' to Pair(Directions.NORTH, Directions.WEST),
        '7' to Pair(Directions.NORTH, Directions.EAST),
        'J' to Pair(Directions.SOUTH, Directions.EAST),
        'L' to Pair(Directions.SOUTH, Directions.WEST),
)

val connectionsFrom = mapOf(
        '|' to Pair(Directions.SOUTH, Directions.NORTH),
        '-' to Pair(Directions.WEST, Directions.EAST),
        'F' to Pair(Directions.SOUTH, Directions.EAST),
        '7' to Pair(Directions.SOUTH, Directions.WEST),
        'J' to Pair(Directions.NORTH, Directions.WEST),
        'L' to Pair(Directions.NORTH, Directions.EAST),
)


fun main() {

    fun getPossibleWays(map: List<CharArray>, currentPointY: Int, currentPointX: Int, lastDir: Directions?): List<Directions> {
        val possibleYWays = when (currentPointY) {
            0 -> {
                listOf(Directions.SOUTH)
            }

            map.size - 1 -> {
                listOf(Directions.NORTH)
            }

            else -> {
                listOf(Directions.SOUTH, Directions.NORTH)
            }
        }

        val possibleXWays = when (currentPointX) {
            0 -> {
                listOf(Directions.EAST)
            }

            map.first().size - 1 -> {
                listOf(Directions.WEST)
            }

            else -> {
                listOf(Directions.EAST, Directions.WEST)
            }
        }

        val possibleWays = possibleYWays.union(possibleXWays).toMutableList()
        if (lastDir != null) {
            when (lastDir) {
                Directions.NORTH -> possibleWays.remove(Directions.SOUTH)
                Directions.SOUTH -> possibleWays.remove(Directions.NORTH)
                Directions.EAST -> possibleWays.remove(Directions.WEST)
                Directions.WEST -> possibleWays.remove(Directions.EAST)
            }
        }
        val currentPipeType = map[currentPointY][currentPointX]
        val connectionsTo = connectionsFrom[currentPipeType]

        if (lastDir == null) {
            return possibleWays
        }
        return possibleWays.filter { connectionsTo?.first == it || connectionsTo?.second == it }
    }

    fun getPossiblePipes(map: List<CharArray>, currentPointY: Int, currentPointX: Int, possibleDirs: List<Directions>): List<Directions> {
        val possiblePipes = mutableListOf<Directions>()
        possibleDirs.forEach {
            val pipeType = map[currentPointY + it.dir.first][currentPointX + it.dir.second]
            if (connectionsTo[pipeType]?.first == it || connectionsTo[pipeType]?.second == it) {
                possiblePipes.add(it)
            }
        }
        return possiblePipes
    }

    fun travel(map: MutableList<CharArray>, currentPointY: Int, currentPointX: Int, lastPointY: Int, lastPointX: Int, isFirstStep: Boolean): Pair<Int, Int> {
        val possibleDirs = getPossibleWays(map, currentPointY, currentPointX, Directions.entries.firstOrNull { it.dir == Pair(currentPointY - lastPointY, currentPointX - lastPointX) })
        val possiblePipes = getPossiblePipes(map, currentPointY, currentPointX, possibleDirs)
        if (isFirstStep) {
            val firstDirection = Directions.entries.first { it.dir == Pair(possiblePipes.first().dir.first * -1, possiblePipes.first().dir.second * -1) }
            val secondDirection = Directions.entries.first { it.dir == Pair(possiblePipes.last().dir.first * -1, possiblePipes.last().dir.second * -1) }
            val pipe = connectionsTo.entries.first { (it.value.first == firstDirection && it.value.second == secondDirection) || (it.value.first == secondDirection && it.value.second == firstDirection) }.key
            var xIndex = 0
            var yIndex = 0
            map.forEachIndexed { index, array ->
                if (array.contains('S')) {
                    xIndex = array.indexOf('S')
                    yIndex = index
                }
            }
            map[yIndex][xIndex] = pipe
        }
        return Pair(currentPointY + possiblePipes.first().dir.first, currentPointX + possiblePipes.first().dir.second)
    }


    fun part1(input: List<String>): Int {
        val charListInput = input.map { it.toCharArray() }
        val startingPointY = charListInput.indexOfFirst { it.contains('S') }
        val startingPointX = charListInput[startingPointY].indexOfFirst { it == 'S' }
        var steps = 0
        var nextPoint = Pair(startingPointY, startingPointX)
        var currentPoint = Pair(startingPointY, startingPointX)
        var lastPoint = Pair(-1, -1)

        while(currentPoint.first != startingPointY || currentPoint.second != startingPointX || steps <= 1) {
            steps++
            nextPoint = travel(charListInput.toMutableList(), currentPoint.first, currentPoint.second, lastPoint.first, lastPoint.second, steps == 1)
            lastPoint = currentPoint
            currentPoint = nextPoint
        }
        println()
        return steps / 2
    }

    fun printSections(sections: MutableList<List<Pair<Int, Int>>>, charListInput: List<CharArray>) {
        sections.forEach {section ->
            val resultmatrix = mutableListOf<MutableList<String>>()
            charListInput.forEachIndexed {indexY, row ->
                val charList = mutableListOf<String>()
                row.forEachIndexed { indexX, x ->
                    if(section.contains(Pair(indexY, indexX))) {
                        charList.add("X")
                    } else {
                        charList.add(".")
                    }
                }
                resultmatrix.add(charList)
            }
            resultmatrix.forEach {
                it.joinToString(" ").println()
            }
            println()
            println()
            println()
        }
    }

    fun part2(input: List<String>): Int {
        val charListInput = input.map { it.toCharArray() }
        val startingPointY = charListInput.indexOfFirst { it.contains('S') }
        val startingPointX = charListInput[startingPointY].indexOfFirst { it == 'S' }
        var steps = 0
        var nextPoint: Pair<Int, Int>
        var currentPoint = Pair(startingPointY, startingPointX)
        var lastPoint = Pair(-1, -1)
        val loopCoordinates = mutableSetOf<Pair<Int, Int>>()

        while(currentPoint.first != startingPointY || currentPoint.second != startingPointX || steps <= 1) {
            steps++
            nextPoint = travel(charListInput.toMutableList(), currentPoint.first, currentPoint.second, lastPoint.first, lastPoint.second, steps == 1)
            lastPoint = currentPoint
            currentPoint = nextPoint
            loopCoordinates.add(currentPoint)
        }
        println()
        val resultmatrix = mutableListOf<MutableList<String>>()
        charListInput.forEachIndexed {indexY, row ->
            val charList = mutableListOf<String>()
            row.forEachIndexed { indexX, x ->
                if(loopCoordinates.contains(Pair(indexY, indexX))) {
                    charList.add(charListInput[indexY][indexX].toString())
                } else {
                    charList.add(".")
                }
            }
            resultmatrix.add(charList)
        }
        resultmatrix.forEach {
            it.joinToString(" ").println()
        }

        val loopList = loopCoordinates.toList()
        val sections = mutableListOf<List<Pair<Int, Int>>>()

        loopList.forEachIndexed {index, pair ->
            if (index < 3) return@forEachIndexed
            val adjacentPairIndex = loopList.subList(0, index - 3).indexOfFirst {visitedPair ->
                visitedPair == Pair(pair.first - 1, pair.second) || visitedPair == Pair(pair.first + 1, pair.second) || visitedPair == Pair(pair.first, pair.second -1) || visitedPair == Pair(pair.first, pair.second + 1)
            }
            if (adjacentPairIndex != -1) {
                val section = loopList.subList(adjacentPairIndex, index + 1).toMutableList()
                if(sections.isNotEmpty() && sections.subList(0, sections.size - 1).any { it.contains(loopList[adjacentPairIndex]) }) {
                    println("position ${loopList[adjacentPairIndex]} is already part of a section. Skipping")
                    return@forEachIndexed
                }
                if(section.any { pair -> sections.any { it.contains(pair) } }) {
                    println("Section already contains used elements. Skipping")
                    sections.removeAt(sections.size - 1)
                }
                sections.add(loopList.subList(adjacentPairIndex, index + 1))
                println("Found visited pair- Current Pair $pair, visited pair ${loopList[adjacentPairIndex]}, parts of border $section")
            }
        }
        printSections(sections, charListInput)

        return steps / 2
    }


    val input = readInput("input-Day10")
    println(part1(input))
    println(part2(input))
}