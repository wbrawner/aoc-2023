fun main() {
    fun part1(input: List<String>): Long = input.parseRaces()
        .waysToWin()
        .reduce { left, right -> left * right}

    fun part2(input: List<String>): Long = input.parseRace().waysToWin()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)

    val input = readInput("Day06")
    part1(input).println()
    check(part2(testInput) == 71503L)
    part2(input).println()
}

data class Race(val time: Long, val distance: Long)

fun List<String>.parseRaces(): List<Race> {
    val times = get(0).numbers()
    val distances = get(1).numbers()
    return times.zip(distances).map { Race(it.first, it.second) }.toList()
}

fun List<String>.parseRace(): Race {
    val time = get(0).number()
    val distance = get(1).number()
    return Race(time, distance)
}

fun List<Race>.waysToWin(): List<Long> = map { race ->
    race.waysToWin()
}

fun Race.waysToWin(): Long {
    var min = 0L
    for (pressTime in 1..<time) {
        val moveTime = time - pressTime
        val distanceMoved = moveTime * pressTime
        if (distanceMoved > distance) {
            min = pressTime
            break
        }
    }
    var max = 0L
    for (pressTime in time downTo 1) {
        val moveTime = time - pressTime
        val distanceMoved = moveTime * pressTime
        if (distanceMoved > distance) {
            max = pressTime
            break
        }
    }
    return max - min + 1
}

fun String.numbers() = Regex("\\d+").findAll(this).map { it.value.toLong() }

fun String.number() = Regex("\\d+").findAll(this).joinToString("") { it.value }.toLong()