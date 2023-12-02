fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            var firstDigit: Char? = null
            var lastDigit: Char? = null
            it.forEach { c ->
                if (!c.isDigit()) return@forEach
                if (firstDigit == null) {
                    firstDigit = c
                }
                lastDigit = c
            }
            "$firstDigit$lastDigit".toInt()
        }
    }

    val numbers = listOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9"
    )

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            var firstIndex: Int = Integer.MAX_VALUE
            var firstNumberIndex: Int? = null
            var lastIndex: Int = -1
            var lastNumberIndex: Int? = null
            numbers.forEachIndexed { numberIndex, number ->
                val firstLineIndex = line.indexOf(number)
                val lastLineIndex = line.lastIndexOf(number)
                if (firstLineIndex > -1 && firstIndex > firstLineIndex) {
                    firstIndex = firstLineIndex
                    firstNumberIndex = numberIndex
                }
                if (lastLineIndex > -1 && lastIndex < lastLineIndex) {
                    lastIndex = lastLineIndex
                    lastNumberIndex = numberIndex
                }
            }
            "${firstNumberIndex!!.numberValue}${lastNumberIndex!!.numberValue}".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

val Int.numberValue: Int
    get() {
        val value = (this + 1) % 9
        return if (value == 0) {
            9
        } else {
            value
        }
    }
