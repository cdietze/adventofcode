package advent2018.day01

import advent2018.common.scan
import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day01/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int =
    inputFile
        .readLines()
        .map { it.toInt() }
        .sum()

fun solvePart2(): Int {
    val seen = mutableSetOf<Int>()
    return inputFile
        .readLines()
        .map { it.toInt() }
        .asSequence()
        .loop()
        .scan { acc, elem -> acc + elem }
        .first { e ->
            seen.contains(e).apply {
                seen.add(e)
            }
        }
}

fun <T> Sequence<T>.loop() = kotlin.sequences.sequence {
    while (true) {
        yieldAll(this@loop)
    }
}
