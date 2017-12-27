package advent20

import parsek.*
import parsek.Parsers.string
import java.io.File
import kotlin.math.absoluteValue

data class Point(val x: Int, val y: Int, val z: Int)

fun Point.mag(): Int = x.absoluteValue + y.absoluteValue + z.absoluteValue

data class Particle(val p: Point, val v: Point, val a: Point)

val pointParser: Parser<Point> = (string("<").ignore() * Parsers.int * string(",").ignore() * Parsers.int * string(",").ignore() * Parsers.int * string(">").ignore()).map { Point(it.first.first, it.first.second, it.second) }
val particleParser: Parser<Particle> = (string("p=").ignore() * pointParser * string(", v=").ignore() * pointParser * string(", a=").ignore() * pointParser).map {
    Particle(it.first.first, it.first.second, it.second)
}

fun main(args: Array<String>) {
    val particles = File("src/main/kotlin/advent20/input.txt").readLines().map { particleParser.parseFully(it).get().value }
    val magComparator = compareBy<Point> { it.mag() }
    val particleComparator = compareBy<Particle, Point>(magComparator, { it.a }).thenBy(magComparator, { it.v }).thenBy(magComparator, { it.p })
    println("Result part 1: ${particles.withIndex().minWith(compareBy(particleComparator, { it.value }))!!.index}")
}
