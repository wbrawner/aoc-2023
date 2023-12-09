fun main() {
    fun part1(input: List<String>): Int {
        return input.toHistories()
            .sequences()
            .extrapolate()
            .sumOf { it.last().last() }
            .also { it.println() }
    }

    fun part2(input: List<String>): Int {
        return input.toHistories()
            .sequences()
            .extrapolatePrevious()
            .sumOf { it.last().first() }
            .also { it.println() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09")
    part1(input).println()
    check(part2(testInput) == 2)
    part2(input).println()
}

fun List<String>.toHistories(): List<List<Int>> = map { line -> line.split(' ').map { it.toInt() } }

fun List<List<Int>>.sequences() = map { history ->
    val differences = mutableListOf(history.toMutableList())
    var nextSequence = history.toMutableList()
    while (nextSequence.any { it != 0 }) {
        nextSequence = nextSequence.zipWithNext { a, b -> b - a }.toMutableList()
        differences.add(nextSequence)
    }
    differences
}

fun List<MutableList<MutableList<Int>>>.extrapolate() = map { history ->
    val bottomUpHistory = history.asReversed()
    bottomUpHistory.mapIndexed { index, sequence ->
        if (index == 0) {
            sequence.add(0)
        } else {
            val other = bottomUpHistory[index - 1].last()
            sequence.add(sequence.last() + other)
        }
        sequence
    }
}

fun List<MutableList<MutableList<Int>>>.extrapolatePrevious() = map { history ->
    val bottomUpHistory = history.asReversed()
    bottomUpHistory.mapIndexed { index, sequence ->
        if (index == 0) {
            sequence.add(0, 0)
        } else {
            val other = bottomUpHistory[index - 1].first()
            sequence.add(0, sequence.first() - other)
        }
        sequence
    }
}