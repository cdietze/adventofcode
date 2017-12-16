package advent10

import java.io.File
import kotlin.experimental.xor
import kotlin.test.assertEquals

tailrec fun <T> twist(data: List<T>, commands: List<Int>, cursor: Int = 0, skipSize: Int = 0): List<T> {
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

fun <T> List<T>.repeat(n: Int): List<T> = List(size * n, { this[it % size] })

fun List<Byte>.denseHash(): List<Byte> {
    require(this.size % 16 == 0)
    return List(this.size / 16, { i ->
        (0..15).map { this[i * 16 + it] }.reduce(Byte::xor)
    })
}

fun List<Byte>.hex(): String = map { String.format("%02x", it) }.joinToString("")

fun hash(input: String): String {
    val commands = input.map { it.toInt() }.toMutableList().apply { addAll(listOf(17, 31, 73, 47, 23)) }.repeat(64)
    val sparseHash = twist((0..255).toList().map { it.toByte() }, commands)
    return sparseHash.denseHash().hex()
}

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent10/input.txt").readText().split(",").map { it.toInt() }
    println("Result part 1: ${twist((0..255).toList(), input).let { it[0] * it[1] }}")
    val textInput = File("src/main/kotlin/advent10/input.txt").readText()
    println("Result part 2: ${hash(textInput)}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(listOf(3, 4, 2, 1, 0), twist((0..4).toList(), listOf(3, 4, 1, 5)))
        assertEquals("a2582a3a0e66e6e86e3812dcb672a272", hash(""))
        assertEquals("33efeb34ea91902bb2f59c9920caa6cd", hash("AoC 2017"))
    }
}