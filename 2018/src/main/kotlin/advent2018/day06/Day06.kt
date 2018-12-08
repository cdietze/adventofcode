package advent2018.day06

import advent2018.common.int
import parsek.*
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val inputFile = File("src/main/kotlin/advent2018/day06/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
//    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val points: List<Point> = inputFile.readLines().map { pointParser.parse(it).getOrFail().value }
    val bounds = points.fold(Rect(points.first().x, points.first().y, 0, 0)) { bounds, p -> bounds.expand(p) }
    val infiniteIndexes = bounds.border().map { it.closestIndex(points) }.toSet()
    return bounds.points()
        .map { it.closestIndex(points) }
        .filterNotNull()
        .groupBy { it }
        .filterKeys { !infiniteIndexes.contains(it) }
        .mapValues { it.value.size }
        .maxBy { it.value }!!
        .value
}

data class Point(val x: Int, val y: Int)

fun Point.dist(p: Point): Int = abs(x - p.x) + abs(y - p.y)

fun Point.closestIndex(points: List<Point>): Int? {
    var bestIndex: Int? = null
    var bestDist: Int = -1
    points.forEachIndexed { i, p ->
        val dist = p.dist(this)
        when {
            bestDist < 0 -> {
                bestIndex = i
                bestDist = dist
            }
            dist < bestDist -> {
                bestIndex = i
                bestDist = dist
            }
            dist == bestDist -> bestIndex = null
        }
    }
    return bestIndex
}

data class Rect(val x: Int, val y: Int, val width: Int, val height: Int)

val Rect.x2: Int get() = x + width
val Rect.y2: Int get() = y + height

fun Rect.expand(p: Point): Rect {
    val minX = min(p.x, x)
    val minY = min(p.y, y)
    val maxX = max(p.x, x + width)
    val maxY = max(p.y, y + height)
    return Rect(minX, minY, maxX - minX, maxY - minY)
}

fun Rect.points(): Sequence<Point> = sequence {
    for (x in x..x2) {
        for (y in y..y2) {
            yield(Point(x, y))
        }
    }
}

fun Rect.border(): Sequence<Point> = sequence {
    for (x in x..x2) {
        yield(Point(x, y))
        yield(Point(x, y2))
    }
    for (y in y..y2) {
        yield(Point(x, y))
        yield(Point(x2, y))
    }
}

val pointParser: Parser<Point> = Rule("Point") {
    (int * P(", ") * int).map(::Point)
}
