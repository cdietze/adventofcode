package advent2018.day11

import advent2018.common.Point
import advent2018.common.Rect
import advent2018.common.points

val input = 5034
val gridWidth = 300
val gridHeight = 300

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
//    println("Result part 2: ${solvePart2()}")
}

val Point.powerLevel: Int
    get() {
        val rackId = x + 10
        val tmp = (rackId * y + input) * rackId
        return tmp.div(100) % 10 - 5
    }

fun Point.powerLevelOfSquare(rect: Rect, powerLevels: List<Int>): Int {
    return (0..2).flatMap { xx ->
        (0..2).map { yy ->
            powerLevels[index(rect, xx + x, yy + y)]
        }
    }.sum()
}

fun index(rect: Rect, x: Int, y: Int): Int = (x - rect.x) + (y - rect.y) * rect.width

fun solvePart1(): Any {
    val rect = Rect(1, 1, gridWidth, gridHeight)
    val powerLevels = rect.points().map { it.powerLevel }.toList()
    return Rect(1, 1, gridWidth - 2, gridHeight - 2).points().maxBy {
        it.powerLevelOfSquare(rect, powerLevels)
    }!!
}
