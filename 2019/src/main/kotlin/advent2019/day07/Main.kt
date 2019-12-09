package advent2019.day07

import advent2019.intcode.State
import advent2019.intcode.run

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val mem = inputText.split(",").map { it.toInt() }
    return (0..4).permute().map { inputs ->
        val result = inputs.fold(0) { acc, i ->
            State(
                mem = mem.toMutableList()
            ).run(input = listOf(i, acc).iterator()).outputList.single()
        }
        result
    }.max()!!
}

data class Amp(
    val state: State,
    val inputQueue: MutableList<Int>
)

fun solvePart2(): Int {
    val mem = inputText.split(",").map { it.toInt() }
    return (5..9).permute().map { phases ->
        val amps = phases.map { phase ->
            Amp(
                State(
                    mem = mem.toMutableList()
                ), mutableListOf(phase)
            )
        }
        amps[0].inputQueue.add(0)
        while (amps.any { !it.state.done }) {
            amps.forEachIndexed { index, amp ->
                val nextAmp = amps[(index + 1) % amps.size]
                amp.state.run(input = amp.inputQueue.iterator(), output = {
                    nextAmp.inputQueue.add(it)
                })
                amp.inputQueue.clear()
            }
        }
        val result = amps.last().state.outputList.last()
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
