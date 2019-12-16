package advent2019.day09

import advent2019.intcode.State
import advent2019.intcode.run
import advent2019.intcode.toMem
import kotlinx.coroutines.runBlocking

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
}

fun solvePart1(): Long = runBlocking {
    val mem = inputText.split(",").map { it.toLong() }
    var outputList = mutableListOf<Long>()
    val state = State(
        mem = mem.toMem(),
        read = { 1 },
        write = { outputList.add(it) }
    )
    state.run()
    outputList.single()
}
