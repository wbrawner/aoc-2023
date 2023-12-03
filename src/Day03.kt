import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        val numRegex = Regex("\\d+")
        val symbolRegex = Regex("[^.\\d]")
        input.forEachIndexed { index, line ->
            numRegex.findAll(line).forEach { numberResult ->
                numberResult.groups.forEach groups@{ group ->
                    if (group == null) return@groups
                    val searchRange = max(0, group.range.first - 1)..min(group.range.last + 1, line.lastIndex)
                    val searchRegions = listOf(
                        input[max(0, index - 1)],
                        input[index],
                        input[min(input.lastIndex, index + 1)],
                    )
                    if (searchRegions.any {
                            it.substring(searchRange).contains(symbolRegex)
                        }) {
                        sum += group.value.toInt()
                    }
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val numRegex = Regex("\\d+")
        val symbolRegex = Regex("[^.\\d]")
        val asterisks = mutableMapOf<Pair<Int, Int>, Set<Int>>()
        input.forEachIndexed { index, line ->
            numRegex.findAll(line).forEach { numberResult ->
                numberResult.groups.forEach groups@{ group ->
                    if (group == null) return@groups
                    val searchRange = max(0, group.range.first - 1)..min(group.range.last + 1, line.lastIndex)
                    val searchRegions = listOf(
                        input[max(0, index - 1)],
                        input[index],
                        input[min(input.lastIndex, index + 1)],
                    )
                    if (searchRegions.any {
                            it.substring(searchRange).contains(symbolRegex)
                        }) {
                        searchRegions.forEachIndexed { regionIndex, region ->
                            val gearIndex = region.substring(searchRange).indexOf('*')
                            if (gearIndex > -1) {
                                val coordinates = (index + regionIndex - 1) to gearIndex + searchRange.first
                                val gearNumbers = asterisks[coordinates]?.toMutableSet()?: mutableSetOf()
                                gearNumbers.add(group.value.toInt())
                                asterisks[coordinates] = gearNumbers
                            }
                        }
                    }
                }
            }
        }
        var sum = 0
        asterisks.values.forEach {
            if (it.size != 2) {
                return@forEach
            }

            sum += it.fold(1) { left, right -> left * right}
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()

    check(part2(testInput) == 467835)
    part2(input).println()
}
