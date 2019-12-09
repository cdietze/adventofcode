package advent2019.day05

import advent2019.intcode.State
import advent2019.intcode.run

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val mem = inputText.split(",").map { it.toInt() }.toMutableList()
    return State(mem = mem).run(input = generateSequence { 1 }.iterator()).outputList.first { it != 0 }
}

fun solvePart2(): Int {
    val mem = inputText.split(",").map { it.toInt() }.toMutableList()
    return State(mem = mem).run(input = generateSequence { 5 }.iterator()).outputList.first { it != 0 }
}
