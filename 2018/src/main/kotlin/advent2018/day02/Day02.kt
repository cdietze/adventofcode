package advent2018.day02

import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day02/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int =
    inputFile
        .readLines()
        .map { line ->
            line.groupBy { it }.values.map { it.size }.let { counts ->
                Pair(
                    if (counts.contains(2)) 1 else 0,
                    if (counts.contains(3)) 1 else 0
                )
            }
        }
        .reduce { a, b -> Pair(a.first + b.first, a.second + b.second) }
        .let { it.first * it.second }

fun solvePart2(): String {
    val lines = inputFile
        .readLines()
    val pairs = lines.withIndex().flatMap { indexedValue ->
        lines.drop(indexedValue.index).map { s -> Pair(indexedValue.value, s) }
    }
    val match = pairs.first { diff(it.first, it.second) == 1 }
    return filterDiff(match.first, match.second)
}

fun diff(a: String, b: String): Int =
    a.withIndex().count { it.value != b[it.index] }

fun filterDiff(a: String, b: String): String =
    a.filterIndexed { index, c -> c == b[index] }
