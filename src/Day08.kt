fun main() {
    fun part1(input: List<String>): Int {
        val stepOrder = input.first()
        val nodes = input.slice(2..input.lastIndex).toNodes()
        var currentNode = "AAA"
        var stepIndex = 0
        var steps = 0
        while (currentNode != "ZZZ") {
            val step = stepOrder[stepIndex]
            currentNode = nodes.require(currentNode).require(step)
            steps++
            if (++stepIndex == stepOrder.length) {
                stepIndex = 0
            }
        }
        return steps
    }

    fun part2(input: List<String>): Int {
        throw RuntimeException("Day 8 part 2 was done in lua, see Day08.lua")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

fun List<String>.toNodes(): Map<String, Map<Char, String>> = associate { line ->
    val letterGroups = Regex("\\w+").findAll(line).map { it.value }.toList()
    letterGroups.first() to mapOf(
        'L' to letterGroups[1],
        'R' to letterGroups[2],
    )
}

fun <K, V> Map<K, V>.require(key: K): V = requireNotNull(get(key))