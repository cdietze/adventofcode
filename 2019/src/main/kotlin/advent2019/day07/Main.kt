package advent2019.day07

import advent2019.intcode.State
import advent2019.intcode.run
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import java.util.LinkedList

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

const val ampCount = 5

fun solvePart1(): Int = runBlocking {
    val mem = inputText.split(",").map { it.toInt() }
    (0 until ampCount).permute().map { phases ->
        phases.fold(0) { acc, i ->
            var outputList = mutableListOf<Int>()
            val inputQueue = LinkedList<Int>().apply { add(i); add(acc) }
            State(
                mem = mem.toMutableList(),
                read = { inputQueue.pop()!! },
                write = { outputList.add(it) }
            ).run()
            outputList.last()
        }
    }.max()!!
}

fun solvePart2(): Int = runBlocking {
    val mem = inputText.split(",").map { it.toInt() }
    (5..9).permute().map { phases ->
        val channels = phases.map { phase ->
            Channel<Int>(Channel.UNLIMITED).apply {
                send(phase)
            }
        }
        channels[0].send(0)
        val states = phases.mapIndexed { index, phase ->
            State(
                mem = mem.toMutableList(),
                read = { channels[index].receive() },
                write = { channels[(index + 1) % ampCount].send(it) }
            )
        }
        states.map { state -> async { state.run() } }.awaitAll()
        channels.forEach { it.close() }
        channels[0].toList().last()
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
