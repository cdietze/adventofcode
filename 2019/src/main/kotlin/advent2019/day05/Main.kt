package advent2019.day05

import advent2019.intcode.State
import advent2019.intcode.run
import kotlinx.coroutines.runBlocking

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int = runBlocking {
    val mem = inputText.split(",").map { it.toInt() }.toMutableList()
    val output = mutableListOf<Int>()
    val state = State(mem = mem, read = { 1 }, write = { output.add(it) })
    state.run()
    output.last()
}

fun solvePart2(): Int = runBlocking {
    val mem = inputText.split(",").map { it.toInt() }.toMutableList()
    val output = mutableListOf<Int>()
    State(mem = mem, read = { 5 }, write = { output.add(it) }).run()
    output.first { it != 0 }
}
