package advent2019.day01

import java.io.File

val inputFile = File("src/main/kotlin/advent2019/day01/input.txt")

fun main() {
    println("Result part 1: ${solvePart1()}")
}

fun solvePart1(): String =
    inputFile.readLines().map { (it.toInt() / 3) - 2 }.sum().toString()
