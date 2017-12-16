package advent10

import java.io.File
import kotlin.test.assertEquals

tailrec fun twist(data: List<Int>, commands: List<Int>, cursor: Int = 0, skipSize: Int = 0): List<Int> {
    if (commands.isEmpty()) return data
    val result = data.toMutableList()
    val command = commands.first()
    for (i in 0 until (command / 2)) {
        val a = cursor + i
        val b = cursor + command - i - 1
        result.swap(a % result.size, b % result.size)
    }
    return twist(result, commands.drop(1), (cursor + command + skipSize) % result.size, skipSize + 1)
}

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent10/input.txt").readText().split(",").map { it.toInt() }
    println("Result part 1: ${twist((0..255).toList(), input).let { it[0] * it[1] }}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(listOf(3, 4, 2, 1, 0), twist((0..4).toList(), listOf(3, 4, 1, 5)))
    }
}