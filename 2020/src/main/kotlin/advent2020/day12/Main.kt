package advent2020.day12

import advent2020.AdventDay
import kotlin.math.absoluteValue

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    data class State(val pos: Point = Point(0, 0), val dir: Int = 1)

    fun String.apply(s: State): State {
        val v = substring(1).toInt()
        return when (this[0]) {
            'N', 'E', 'S', 'W' -> s.copy(pos = s.pos.move(this[0], v))
            'L' -> s.copy(dir = (s.dir + 4 - v / 90) % 4)
            'R' -> s.copy(dir = (s.dir + v / 90) % 4)
            'F' -> "${listOf('N', 'E', 'S', 'W')[s.dir]}${this.substring(1)}".apply(s)
            else -> error("Unexpected command: $this")
        }
    }
    return input.lines().fold(
        State(), { s, line -> line.apply(s) }
    ).pos.manhattanDist
}

fun resultPart2(input: String): Int {
    data class State(
        val pos: Point = Point(0, 0), // Position of the ship
        val wayPoint: Point = Point(10, 1) // Position of waypoint *relative* to ship
    )

    fun Point.rotateClockwise(): Point = Point(y, -x)

    fun State.rotateClockwise(times: Int): State = (0 until times).fold(this, { acc, _ ->
        acc.copy(wayPoint = acc.wayPoint.rotateClockwise())
    })

    fun String.apply(s: State): State {
        val v = substring(1).toInt()
        return when (this[0]) {
            'N', 'E', 'S', 'W' -> s.copy(wayPoint = s.wayPoint.move(this[0], v))
            'L' -> s.rotateClockwise(4 - v / 90)
            'R' -> s.rotateClockwise(v / 90)
            'F' -> s.copy(pos = Point(s.pos.x + v * s.wayPoint.x, s.pos.y + v * s.wayPoint.y))
            else -> error("Unexpected command: $this")
        }
    }
    return input.lines().fold(
        State(), { s, line -> line.apply(s) }
    ).pos.manhattanDist
}

data class Point(val x: Int, val y: Int)

fun Point.move(dir: Char, dist: Int) = when (dir) {
    'N' -> copy(y = y + dist)
    'E' -> copy(x = x + dist)
    'S' -> copy(y = y - dist)
    'W' -> copy(x = x - dist)
    else -> error("Unknown dir: $dir")
}

val Point.manhattanDist get(): Int = x.absoluteValue + y.absoluteValue
