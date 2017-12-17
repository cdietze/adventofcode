package advent11

import parsek.*
import parsek.Parsers.string
import java.io.File
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

data class Hex(val s: Int = 0, val t: Int = 0)

val Hex.u get() = 0 - s - t

operator fun Hex.plus(o: Hex): Hex = Hex(s + o.s, t + o.t)

fun List<String>.parseHex(): List<Hex> = map { hexParser.parse(it).get().value }
fun List<Hex>.finalHex(): Hex = fold(Hex(), Hex::plus)
fun Hex.distFromOrigin(): Int = (s.absoluteValue + t.absoluteValue + u.absoluteValue) / 2

/** Computes a prefix scan of this list */
fun <T, R> List<T>.scan(init: R, op: (R, T) -> R): List<R> {
    var acc = init
    return map { acc = op(acc, it); acc }
}

fun List<Hex>.maxDistFromOrigin(): Int = scan(Hex(), Hex::plus).map { it.distFromOrigin() }.max()!!

val hexParser: Parser<Hex> = string("nw").map { Hex(-1, 0) }.or(
        string("ne").map { Hex(1, -1) }).or(
        string("n").map { Hex(0, -1) }).or(
        string("sw").map { Hex(-1, 1) }).or(
        string("se").map { Hex(1, 0) }).or(
        string("s").map { Hex(0, 1) })

val inputParser: Parser<List<Hex>> = (hexParser.ignore(string(",").optional())).rep()

fun main(args: Array<String>) {
    val input = inputParser.parseFully(File("src/main/kotlin/advent11/input.txt").readText()).get().value
    println("Result part 1: ${input.finalHex().distFromOrigin()}")
    println("Result part 2: ${input.maxDistFromOrigin()}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(3, listOf("ne", "ne", "ne").parseHex().finalHex().distFromOrigin())
        assertEquals(0, listOf("ne", "ne", "sw", "sw").parseHex().finalHex().distFromOrigin())
        assertEquals(2, listOf("ne", "ne", "s", "s").parseHex().finalHex().distFromOrigin())
        assertEquals(3, listOf("se", "sw", "se", "sw", "sw").parseHex().finalHex().distFromOrigin())
    }
}
