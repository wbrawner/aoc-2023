import java.awt.Desktop
import java.net.URI

plugins {
    kotlin("jvm") version "1.9.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }

    // Adapted from https://github.com/kotlin-hands-on/advent-of-code-kotlin-template/pull/17
    task("generateNextDay") {
        doLast {
            val prevDayNum = fileTree("$projectDir/src").matching {
                include("Day*.kt")
            }.maxOf {
                val (prevDayNum) = Regex("Day(\\d\\d)").find(it.name)!!.destructured
                prevDayNum.toInt()
            }
            val newDayNum = (prevDayNum + 1).toString()
            File("$projectDir/src", "Day${newDayNum.padStart(2, '0')}.kt").writeText(
                """fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size  
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${newDayNum.padStart(2, '0')}_test")
    check(part1(testInput) == 0)

    val input = readInput("Day${newDayNum.padStart(2, '0')}")
    part1(input).println()
    check(part2(testInput) == 0)
    part2(input).println()
}
"""
            )
            val challengeUrl = "https://adventofcode.com/2023/day/$newDayNum"
            Desktop.getDesktop().browse(URI.create(challengeUrl))
            File("$projectDir/src", "Day$newDayNum.txt").createNewFile()
            File("$projectDir/src", "Day${newDayNum}_test.txt").createNewFile()
        }
    }
}
