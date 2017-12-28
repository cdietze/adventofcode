package advent20

import parsek.*
import parsek.Parsers.string
import java.io.File
import kotlin.math.absoluteValue

data class Point(val x: Int, val y: Int, val z: Int)

operator fun Point.plus(o: Point) = Point(x + o.x, y + o.y, z + o.z)

fun Point.mag(): Int = x.absoluteValue + y.absoluteValue + z.absoluteValue

data class Particle(val p: Point, val v: Point, val a: Point)

val pointParser: Parser<Point> = (string("<").ignore() * Parsers.int * string(",").ignore() * Parsers.int * string(",").ignore() * Parsers.int * string(">").ignore()).map { Point(it.first.first, it.first.second, it.second) }
val particleParser: Parser<Particle> = (string("p=").ignore() * pointParser * string(", v=").ignore() * pointParser * string(", a=").ignore() * pointParser).map {
    Particle(it.first.first, it.first.second, it.second)
}

fun Particle.step(): Particle = copy(p = p + v + a, v = v + a)

fun List<Particle>.step(): List<Particle> {
    val collisionsRemoved = this.groupBy { it.p }.filterValues { it.size == 1 }.values.flatten()
    return collisionsRemoved.map { it.step() }
}

fun main(args: Array<String>) {
    val particles = File("src/main/kotlin/advent20/input.txt").readLines().map { particleParser.parseFully(it).get().value }
    val magComparator = compareBy<Point> { it.mag() }
    val particleComparator = compareBy<Particle, Point>(magComparator, { it.a }).thenBy(magComparator, { it.v }).thenBy(magComparator, { it.p })
    println("Result part 1: ${particles.withIndex().minWith(compareBy(particleComparator, { it.value }))!!.index}")
    val end = (0..(1000 * 10)).fold(particles, { acc: List<Particle>, index: Int -> acc.step() })
    println("Result part 2: ${end.size}")
}
