fun main() {
    fun part1(input: List<String>): Int {
        val maxCubes = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )
        return input.map { Game.parse(it) }
            .sumOf { game ->
                val validGame = game.subsets.all { subset ->
                    subset.none { (color, count) ->
                        (count > maxCubes[color]!!)
                    }
                }
                if (validGame) {
                    game.id
                } else {
                    0
                }
            }
    }

    fun part2(input: List<String>): Int {
        return input.map { Game.parse(it) }
            .sumOf { game ->
                val minNecessary = mutableMapOf(
                    "blue" to 0,
                    "green" to 0,
                    "red" to 0
                )
                game.subsets.forEach { subset ->
                    subset.forEach { (color, count) ->
                        if (count > minNecessary[color]!!) {
                            minNecessary[color] = count
                        }
                    }
                }
                var power = 1
                minNecessary.values.forEach { power *= it }
                power
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Game(val id: Int, val subsets: Set<Map<String, Int>>) {
    companion object {
        fun parse(s: String): Game {
            val colonPosition = s.indexOf(':')
            val id = s.substring(5, colonPosition).toInt()
            val subsets = s.substring(colonPosition + 2)
                .split(';')
                .map { set ->
                    set.split(',')
                        .associate {
                            val parts = it.trim().split(' ')
                            parts[1] to parts[0].toInt()
                        }
                }
                .toSet()
            return Game(id, subsets)
        }
    }
}
