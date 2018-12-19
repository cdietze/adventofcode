package advent2018.day11

import advent2018.common.Point
import advent2018.common.Rect
import advent2018.common.points

val input = 5034
val gridWidth = 300
val gridHeight = 300

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

val Point.powerLevel: Int
    get() {
        val rackId = x + 10
        val tmp = (rackId * y + input) * rackId
        return tmp.div(100) % 10 - 5
    }

fun Point.powerLevelOf3x3Square(rect: Rect, powerLevels: List<Int>): Int {
    val squareSize = 3
    return (0 until squareSize).flatMap { xx ->
        (0 until squareSize).map { yy ->
            powerLevels[index(rect, xx + x, yy + y)]
        }
    }.sum()
}

data class PowerLevels(val size: Int, val localRect: Rect, val sums: List<Int>)

fun PowerLevels.next(rect: Rect, powerLevels: List<Int>): PowerLevels {
    val newSize = size + 1
    val newRect = localRect.copy(width = localRect.width - 1, height = localRect.height - 1)
    return PowerLevels(
        newSize, newRect, newRect.points().map { it.powerLevelOfSquare(rect, newSize, powerLevels, this) }.toList()
    )
}

fun Point.powerLevelOfSquare(
    rect: Rect,
    squareSize: Int,
    powerLevels: List<Int>,
    powerLevelsOfLastSize: PowerLevels
): Int {
    return powerLevelsOfLastSize.sums[
            index(powerLevelsOfLastSize.localRect, x, y)
    ] + (0 until (squareSize - 1)).map { xx ->
        powerLevels[index(rect, x + xx, y + squareSize - 1)]
    }.sum() + (0 until squareSize).map { yy ->
        powerLevels[index(rect, x + squareSize - 1, y + yy)]
    }.sum()
}


fun index(rect: Rect, x: Int, y: Int): Int = (x - rect.x) + (y - rect.y) * rect.width

fun solvePart1(): Point {
    val rect = Rect(1, 1, gridWidth, gridHeight)
    val powerLevels = rect.points().map { it.powerLevel }.toList()
    return Rect(1, 1, gridWidth - 2, gridHeight - 2).points().maxBy {
        it.powerLevelOf3x3Square(rect, powerLevels)
    }!!
}

data class Row(val p: Point, val size: Int, val value: Int)

fun solvePart2(): Row {
    val rect = Rect(1, 1, gridWidth, gridHeight)
    val powerLevels = rect.points().map { it.powerLevel }.toList()
    val firstLevelPowerLevels = PowerLevels(1, rect, powerLevels)
    var currentPowerLevels = firstLevelPowerLevels.next(rect, powerLevels)
    return (3..300).map { size ->
        currentPowerLevels = currentPowerLevels.next(rect, powerLevels)
        val best = currentPowerLevels.localRect.points()
            .map { Pair(it, currentPowerLevels.sums[index(currentPowerLevels.localRect, it.x, it.y)]) }
            .maxBy { it.second }!!
        Row(best.first, size, best.second)
    }.maxBy { it.value }!!
}
