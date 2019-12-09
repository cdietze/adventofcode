package advent2019.day07

import advent2019.intcode.State
import advent2019.intcode.run
import advent2019.intcode.toFun

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    // println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val mem = inputText.split(",").map { it.toInt() }
    return (0..4).permute().map { inputs ->
        val result = inputs.fold(0) { acc, i ->
            State(
                mem = mem.toMutableList(),
                input = listOf(i, acc).toFun()
            ).run().output.single()
        }
        result
    }.max()!!
}

fun <T> Iterable<T>.permute(): List<List<T>> {
    val iter = iterator()
    if (!iter.hasNext()) return listOf()
    val head = iter.next()
    if (!iter.hasNext()) return listOf(listOf(head))
    return drop(1).permute().flatMap { rest ->
        (0..rest.size).map { i ->
            rest.toMutableList().apply {
                add(i, head)
            }
        }
    }
}
