fun main() {
    fun part1(input: List<String>): Int {
        val pathSegments = mutableSetOf<PathSegment>()
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (pipes.contains(char)) {
                    pathSegments.add(PathSegment(Point(x, y), char))
                }
            }
        }
        val start = pathSegments.first { it.pipe == 'S' }
        val loopLength = pathSegments.findLoop(start).maxOf { it.distanceFromStart }
        return loopLength / 2 + 1
    }

    fun part2(input: List<String>): Int {
        val pathSegments = mutableSetOf<PathSegment>()
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (pipes.contains(char)) {
                    pathSegments.add(PathSegment(Point(x, y), char))
                }
            }
            println()
        }
        val start = pathSegments.first { it.pipe == 'S' }
        val loop = pathSegments.findLoop(start)
        var innerArea = 0
        input.forEachIndexed { y, row ->
            var loopOpen = false
            val loopPiecesInRow = loop.filter { it.point.y == y }.sortedBy { it.point.x }
            if (loopPiecesInRow.isEmpty()) return@forEachIndexed
            var pipesSeen = 0
            row.forEachIndexed { x, char ->
                print(char.toBlockChar(loopOpen))
                val loopPiece = loopPiecesInRow.firstOrNull { it.point == Point(x, y) }
                if (loopPiece == null) {
                    if (loopOpen) {
                        innerArea++
//                        println("Inner area at $x, $y")
                    }
                } else {
                    loopOpen = when (loopPiece.pipe) {
                        'S', 'L', 'F', '-' -> true
                        '7', 'J' -> false
                        '|' -> when (++pipesSeen % 2) {
                            1 -> true
                            0 -> false
                            else -> throw IllegalStateException("Not possible")
                        }
                        else -> throw IllegalStateException("Unhandled pipe char: ${loopPiece.pipe}")
                    }
                }
            }
            println()
        }
        return innerArea.also {
            it.println()
        }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 4)

    val input = readInput("Day10")
//    part1(input).println()
    check(part2(testInput) == 10)
    part2(input).println()
}

val pipes = setOf(
    'S',
    '|',
    '-',
    'L',
    'J',
    '7',
    'F',
)

data class Point(val x: Int, val y: Int)

data class PathSegment(val point: Point, val pipe: Char, val distanceFromStart: Int = 0)

fun PathSegment.validMoves(): Set<Point> = when (pipe) {
    'S' -> setOf(
        point.copy(x = point.x - 1),
        point.copy(x = point.x + 1),
        point.copy(y = point.y - 1),
        point.copy(y = point.y + 1),
    )

    '|' -> setOf(
        point.copy(y = point.y - 1),
        point.copy(y = point.y + 1),
    )

    '-' -> setOf(
        point.copy(x = point.x - 1),
        point.copy(x = point.x + 1),
    )

    'L' -> setOf(
        point.copy(x = point.x + 1),
        point.copy(y = point.y - 1),
    )

    'J' -> setOf(
        point.copy(x = point.x - 1),
        point.copy(y = point.y - 1),
    )

    '7' -> setOf(
        point.copy(x = point.x - 1),
        point.copy(y = point.y + 1),
    )

    'F' -> setOf(
        point.copy(x = point.x + 1),
        point.copy(y = point.y + 1),
    )

    else -> throw IllegalArgumentException("Attempt to check valid moves for non-pipe character")
}

fun Iterable<PathSegment>.findLoop(start: PathSegment): Iterable<PathSegment> {
    val visited = mutableSetOf<PathSegment>()
    val queue = ArrayDeque<PathSegment>()
    var next = start
    queue.addFirst(next)
    while (queue.isNotEmpty()) {
        next = queue.removeFirst()
        if (next.distanceFromStart == 1) {
            visited.clear()
            visited.add(start)
        }
        visited.add(next)
        val validMoves = next.validMoves()
        forEach { pathSegment ->
            if (!validMoves.contains(pathSegment.point)) {
                return@forEach
            }
            if (next.distanceFromStart > 1 && pathSegment.pipe == 'S') {
                return visited
            }
            if (visited.any { it.point == pathSegment.point }) {
                return@forEach
            }
            queue.addFirst(pathSegment.copy(distanceFromStart = next.distanceFromStart + 1))
        }
    }
    throw IllegalStateException("Failed to find loop")
}

fun Char.toBlockChar(loopOpen: Boolean = false) = when (this) {
    'S' -> '★'
    'L' -> '╚'
    'F' -> '╔'
    '7' -> '╗'
    'J' -> '╝'
    '|' -> '║'
    '-' -> '═'
    '.' -> if (loopOpen) '•' else ' '
    else -> throw IllegalArgumentException("Unhandled char mapping: $this")
}