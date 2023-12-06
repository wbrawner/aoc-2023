fun main() {
    fun part1(input: List<String>): Long {
        return SupplyMapper(input).lowestLocation()
    }

    fun part2(input: List<String>): Long {
        return SupplyMapper(input).lowestLocationPart2()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()
    check(part2(testInput) == 46L)
    part2(input).println()
}

class SupplyMapper(input: List<String>) {
    private val seeds: List<Long>
    private val seedRanges: List<LongRange>
    private val rangeMaps: List<Set<RangeMap>>

    init {
        val inputIterator = input.iterator()
        seeds = inputIterator.next()
            .substringAfter(' ')
            .split(' ')
            .map { it.toLong() }
        val seedRanges = mutableListOf<LongRange>()
        for (i in seeds.indices.step(2)) {
            val start = seeds[i]
            val end = start + seeds[i + 1]
            seedRanges.add(start..<end)
        }
        this.seedRanges = seedRanges
        inputIterator.next()
        val rangeMaps = mutableListOf<Set<RangeMap>>()
        while (inputIterator.hasNext()) {
            rangeMaps.add(inputIterator.rangeMaps())
        }
        this.rangeMaps = rangeMaps
    }

    fun lowestLocation(): Long = seeds.minOf(::mapSeedToLocation)

    fun lowestLocationPart2(): Long {
        repeat(Int.MAX_VALUE) { number ->
            val location = number.toLong()
            val seed = mapLocationToSeed(location)
            if (seedRanges.any { it.contains(seed) }) {
                return@lowestLocationPart2 location
            }
        }
        return 0L
    }

    private fun mapSeedToLocation(seed: Long): Long {
        return rangeMaps.fold(seed) { num, rangeMapSet -> rangeMapSet.map(num) }
    }

    private fun mapLocationToSeed(location: Long): Long {
        return rangeMaps.reversed().fold(location) { num, rangeMapSet -> rangeMapSet.reverseMap(num) }
    }
}

private fun Iterator<String>.rangeMaps(): Set<RangeMap> {
    val ranges = mutableSetOf<RangeMap>()
    next()
    var line = next()
    while (line.isNotBlank() && line.first().isDigit()) {
        ranges.add(RangeMap.parse(line))
        if (!hasNext()) break
        line = next()
    }
    return ranges
}

data class RangeMap(val sourceRange: LongRange, val destRange: LongRange) {
    constructor(sourceStart: Long, destStart: Long, rangeSize: Long) :
            this(sourceStart..<sourceStart + rangeSize, destStart..<destStart + rangeSize)

    fun map(source: Long): Long? {
        return if (!sourceRange.contains(source)) {
            null
        } else {
            destRange.first + sourceRange.offset(source)
        }
    }

    fun reverseMap(source: Long): Long? {
        return if (!destRange.contains(source)) {
            null
        } else {
            sourceRange.first + destRange.offset(source)
        }
    }

    companion object {
        fun parse(s: String): RangeMap {
            val (destStart, srcStart, rangeSize) = s.split(' ').map { it.toLong() }
            return RangeMap(sourceStart = srcStart, destStart = destStart, rangeSize = rangeSize)
        }
    }
}

fun LongRange.offset(at: Long) = at - first

fun Set<RangeMap>.map(source: Long) = firstNotNullOfOrNull { it.map(source) } ?: source

fun Set<RangeMap>.reverseMap(source: Long) = firstNotNullOfOrNull { it.reverseMap(source) } ?: source
