fun main() {
    fun part1(input: List<String>): Int {
        return input.first()
            .split(',')
            .fold(0) { acc, s ->
                acc + s.hash()
            }
    }

    fun part2(input: List<String>): Int {
        return input.size  
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)

    val input = readInput("Day15")
    part1(input).println()
    check(part2(testInput) == 0)
    part2(input).println()
}

fun String.hash(): Int = toCharArray().fold(0) { sum, char ->
    ((sum + char.code) * 17) % 256
}