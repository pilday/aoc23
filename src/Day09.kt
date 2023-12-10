fun main() {

    fun processInputList(numList: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        if (numList.last().all { it == 0 }) return numList
        val newList = mutableListOf<Int>()
        numList.last().forEachIndexed { index, i ->
            if(index == 0) return@forEachIndexed
            else {
                newList.add(numList.last()[index] - numList.last()[index - 1])
            }
        }
        numList.add(newList)
        return processInputList(numList)
    }

    fun part1(input: List<String>): Int {
        var resultsum = 0
        input.forEach {
            val numList = it.split(" ").map { s -> s.toInt() }.toMutableList()
            val resultList = processInputList(mutableListOf(numList))
            resultList.reverse()
            val additionalNumbers = mutableListOf<Int>()
            resultList.forEachIndexed { index, ints ->
                if(index < resultList.size) {
                    if (index == 0) {
                        additionalNumbers.add(0)
                    } else {
                        additionalNumbers.add(ints.last() + additionalNumbers.last())
                    }
                }
            }
            resultsum += additionalNumbers.last()
            println()
        }
        return resultsum
    }

    fun part2(input: List<String>): Int {
        var resultsum = 0
        input.forEach {
            val numList = it.split(" ").map { s -> s.toInt() }.toMutableList()
            val resultList = processInputList(mutableListOf(numList))
            resultList.reverse()
            val additionalNumbers = mutableListOf<Int>()
            resultList.forEachIndexed { index, ints ->
                if(index < resultList.size) {
                    if (index == 0) {
                        additionalNumbers.add(0)
                    } else {
                        additionalNumbers.add(ints.first() - additionalNumbers.last())
                    }
                }
            }
            resultsum += additionalNumbers.last()
            println()
        }
        return resultsum
    }

    val input = readInput("input")
    part1(input).println()
    part2(input).println()
}
