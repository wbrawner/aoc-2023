import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        var matchValue = 0
        input.forEach { card ->
            val winningNumbers = card.winningNumbers
            val matches = card.myNumbers.sumOf { number ->
                (if (winningNumbers.contains(number)) 1 else 0).toInt()
            }
            matchValue += when {
                matches > 1 -> 2.0.pow(matches - 1).toInt()
                else -> matches
            }
        }
        return matchValue
    }

    fun part2(input: List<String>): Int {
        val cardCounts = MutableList(input.size) { 1 }
        val matches = MutableList(input.size) { 0 }
        input.forEachIndexed { index, card ->
            val winningNumbers = card.winningNumbers
            val matchCount = card.myNumbers.sumOf { number ->
                (if (winningNumbers.contains(number)) 1 else 0).toInt()
            }
            matches[index] = matchCount * cardCounts[index]
            for (i in index + 1..index + matchCount) {
                cardCounts[i] = cardCounts[i] + (1 * cardCounts[index])
            }
        }
        return cardCounts.subList(0, input.size).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()
    check(part2(testInput) == 30)
    part2(input).println()
}

val String.winningNumbers: Set<Int>
    get() = substringAfter(':')
        .substringBefore('|')
        .split(' ')
        .mapNotNull { it.toIntOrNull() }
        .toSet()

val String.myNumbers: List<Int>
    get() = substringAfter('|')
        .split(' ')
        .mapNotNull { it.toIntOrNull() }