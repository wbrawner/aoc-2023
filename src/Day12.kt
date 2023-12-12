val potentialSprings = listOf('#', '?')

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (springs, constraints) = line.split(' ')
            var permutations = 1
            val firstSolution = constraints.solveForConstraints()

            println()
            permutations
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)

    val input = readInput("Day12")
    part1(input).println()
    check(part2(testInput) == 0)
    part2(input).println()
}

fun String.solveForConstraints(): String {
    var modifiedLine = this
    var subStart = 0
    split(',')
        .map { it.toInt() }
        .forEach { constraint ->
            var subSize = 0
            for (i in subStart..modifiedLine.lastIndex) {
                if (potentialSprings.contains(modifiedLine[i])) {
                    if (subSize == 0) {
                        subStart = i
                    }
                    subSize++
                    if (subSize >= constraint) {
                        if (modifiedLine.getOrNull(subStart + subSize) == '#') {
                            // If character after replacement group is invalid, we can't place it here
                            subStart++
                            subSize--
                        } else {
                            modifiedLine = modifiedLine.replaceRange(subStart, subStart + subSize, "#".repeat(subSize))
                            modifiedLine.println()
                            subStart += subSize + 1
                            break
                        }
                    }
                } else {
                    subStart = i
                    subSize = 0
                }
            }
        }
    return modifiedLine
}