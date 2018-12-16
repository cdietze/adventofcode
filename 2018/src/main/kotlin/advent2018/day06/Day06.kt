package advent2018.day06

import advent2018.common.*
import parsek.*
import java.io.File
import kotlin.math.abs

val inputFile = File("src/main/kotlin/advent2018/day06/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val points: List<Point> = inputFile.readLines().map { pointParser.parse(it).getOrFail().value }
    val bounds = points.fold(Rect(points.first().x, points.first().y, 0, 0)) { bounds, p -> bounds.enclose(p) }
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

fun solvePart2(): Int {
    val maxTotalDist = 10000
    val points: List<Point> = inputFile.readLines().map { pointParser.parse(it).getOrFail().value }
    val bounds = points.fold(Rect(points.first().x, points.first().y, 0, 0)) { bounds, p -> bounds.enclose(p) }
    val expandedBounds = bounds.expand(maxTotalDist / points.size + 1)
    return expandedBounds.points()
        .filter { it.totalDistanceLessThan(points, maxTotalDist) }
        .count()
}

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

fun Point.totalDistanceLessThan(points: List<Point>, maxTotalDist: Int): Boolean =
    points.map { it.dist(this) }.sum() < maxTotalDist

val pointParser: Parser<Point> = Rule("Point") {
    (int * P(", ") * int).map(::Point)
}
