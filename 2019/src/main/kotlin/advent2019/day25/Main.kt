package advent2019.day25

import advent2019.intcode.State
import advent2019.intcode.run
import advent2019.intcode.toMem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.InputStream

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    // playInteractive()
    println("Running part1 1...")
    solvePart1()
}

fun Int.getBit(n: Int): Boolean = (this shr n and 1) == 1

fun solvePart1(): Unit = runBlocking {
    val mem = inputText.split(",").map { it.toLong() }
    val x = sequence<Char> {

        suspend fun SequenceScope<Char>.cmd(vararg s: String) =
            s.joinToString(separator = "\n", postfix = "\n").map { yield(it) }

        cmd(
            "north",
            "north",
            "north",
            "take mutex",
            "south",
            "south",
            "east",
            "north",
            "take loom",
            "south",
            "west",
            "south"
        )
        cmd("east", "take semiconductor", "east", "take ornament", "west", "west")
        cmd(
            "west",
            "west",
            "take sand",
            "south",
            "east",
            "take asterisk",
            "north",
            "take wreath",
            "south",
            "west",
            "north"
        )
        cmd("north", "take dark matter", "east")
        val items = listOf("asterisk", "dark matter", "ornament", "semiconductor", "loom", "mutex", "sand", "wreath")
        suspend fun SequenceScope<Char>.takeItem(n: Int) = cmd("take ${items[n]}")
        suspend fun SequenceScope<Char>.dropItem(n: Int) = cmd("drop ${items[n]}")
        suspend fun SequenceScope<Char>.dropAll() = (0..7).map { dropItem(it) }
        for (x in 0..255) {
            println("Trying combination #$x")
            dropAll()
            (0..7).map { if (x.getBit(it)) takeItem(it) }
            cmd("inv")
            cmd("east")
        }
    }
    val s: Iterator<Char> = x.iterator()
    val state = State(
        mem = mem.toMem(),
        read = { s.next().apply { print(this) }.toLong() },
        write = { print(it.toChar()) }
    )
    state.run()
}

fun playInteractive(): Unit = runBlocking {
    val mem = inputText.split(",").map { it.toLong() }
    val state = State(
        mem = mem.toMem(),
        read = { System.`in`.readAsync().toLong() },
        write = { print(it.toChar()) }
    )
    state.run()
}

suspend fun InputStream.readAsync(): Int =
    withContext(Dispatchers.IO) {
        read()
    }
