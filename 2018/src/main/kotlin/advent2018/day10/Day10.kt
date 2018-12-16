package advent2018.day10

import advent2018.common.*
import parsek.*
import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day10/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
//    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): String {
    val initialChart: Chart = inputFile
        .readLines()
        .map { lightParser.parse(it).getOrFail().value }
        .map { Pair(it.pos, it) }
        .toMap()
    val best: Chart =
        sequence {
            var chart = initialChart
            (0..Int.MAX_VALUE).forEach { i ->
                //            println("move $i, connectivity: ${chart.connectivity()}, area: ${chart.bounds().area}")
                yield(chart)
                chart = chart.move()
            }
        }
            .map { chart: Chart -> Pair(chart, chart.connectivity()) }
            .take(100000)
            .maxBy { it.second }!!
            .first
//    println("Found best. index: ${best.first.index}, connectivity: ${best.first.chart.connectivity()}, area: ${best.first.chart.bounds().area}")
    return best.toPrettyString()
}

data class Vector(val x: Int, val y: Int)

fun Point.add(v: Vector): Point = Point(x + v.x, y + v.y)

data class Light(val pos: Point, val vel: Vector)

fun Light.move(): Light = copy(pos = pos.add(vel))

val notInt: Parser<Unit> = CharPred { !it.isDigit() && it != '-' }.rep().map { Unit }

val lightParser: Parser<Light> = Rule("Light") {
    ((notInt * int * notInt * int).map(::Point) * (notInt * int * notInt * int).map(::Vector))
        .map(::Light)
}

typealias Chart = Map<Point, Light>

fun Chart.move(): Chart =
    this.map { e ->
        val newLight = e.value.move()
        Pair(newLight.pos, newLight)
    }.toMap()

fun Light.connectivity(chart: Chart): Int =
    listOf(
        pos.copy(x = pos.x - 1),
        pos.copy(y = pos.y - 1)
    ).filter { chart.containsKey(it) }
        .count()

fun Chart.bounds(): Rect = this.keys.fold(this.keys.first()
    .let { Rect(it.x, it.y, 0, 0) }) { bounds, p -> bounds.enclose(p) }

fun Chart.connectivity(): Int = values.map { it.connectivity(this) }.sum()

fun Chart.toPrettyString(): String {
    val bounds = this.bounds()
    val sb = StringBuilder()
    for (y in bounds.y..bounds.y2) {
        for (x in bounds.x..bounds.x2) {
            sb.append(if (this.containsKey(Point(x, y))) "X" else ".")
        }
        sb.append("\n")
    }
    return sb.toString()
}
