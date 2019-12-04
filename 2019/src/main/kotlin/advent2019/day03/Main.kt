package advent2019.day03

import advent2019.CommonParsers.int
import parsek.P
import parsek.Parser
import parsek.getOrFail
import parsek.map
import parsek.plus
import parsek.rep
import parsek.times
import kotlin.math.absoluteValue

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
}

data class Point(val x: Int, val y: Int)

val origin = Point(0, 0)

fun Point.manhattanDist(): Int = x.absoluteValue + y.absoluteValue

enum class Dir {
    Up, Down, Left, Right
}

operator fun Dir.invoke(p: Point): Point = when (this) {
    Dir.Up -> p.copy(y = p.y + 1)
    Dir.Down -> p.copy(y = p.y - 1)
    Dir.Left -> p.copy(x = p.x - 1)
    Dir.Right -> p.copy(x = p.x + 1)
}

fun solvePart1(): Int {
    val wires = inputText
        .lines()
        .map { lineParser.parse(it).getOrFail().value }
        .map { it.points() }
    return wires[0].intersect(wires[1]).map { it.manhattanDist() }.filter { it > 0 }.min()!!
}

data class Instr(val dir: Dir, val dist: Int)

val dirParser: Parser<Dir> =
    P("U").map { Dir.Up } +
        P("D").map { Dir.Down } +
        P("L").map { Dir.Left } +
        P("R").map { Dir.Right }

val instrParser: Parser<Instr> = (dirParser * int).map(::Instr)

val lineParser: Parser<List<Instr>> = instrParser.rep(sep = P(","))

fun List<Instr>.points(): Set<Point> {
    val set = mutableSetOf(origin)
    var pos = origin
    this.forEach { instr ->
        repeat(instr.dist) {
            pos = instr.dir(pos)
            set.add(pos)
        }
    }
    return set
}
