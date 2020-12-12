package advent2020.day12

import advent2020.AdventDay
import advent2020.day12.Dir.*
import kotlin.math.absoluteValue

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    data class State(val pos: Point = Point(0, 0), val dir: Dir = RIGHT)

    fun String.apply(s: State): State {
        val v = substring(1).toInt()
        return when (this[0]) {
            'N' -> s.copy(pos = s.pos.move(UP, v))
            'S' -> s.copy(pos = s.pos.move(DOWN, v))
            'E' -> s.copy(pos = s.pos.move(RIGHT, v))
            'W' -> s.copy(pos = s.pos.move(LEFT, v))
            'L' -> s.copy(dir = s.dir.rotateClockwiseDeg(-v))
            'R' -> s.copy(dir = s.dir.rotateClockwiseDeg(v))
            'F' -> s.copy(pos = s.pos.move(s.dir, v))
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
        val wayPoint: Point = Point(10, -1) // Position of waypoint *relative* to ship
    )

    fun Point.rotateClockwise(): Point = Point(-y, x)

    fun State.rotateClockwiseDeg(deg: Int): State = (0 until degToClockwiseRotations(deg)).fold(this, { acc, _ ->
        acc.copy(wayPoint = acc.wayPoint.rotateClockwise())
    })

    fun String.apply(s: State): State {
        val v = substring(1).toInt()
        return when (this[0]) {
            'N' -> s.copy(wayPoint = s.wayPoint.move(UP, v))
            'S' -> s.copy(wayPoint = s.wayPoint.move(DOWN, v))
            'E' -> s.copy(wayPoint = s.wayPoint.move(RIGHT, v))
            'W' -> s.copy(wayPoint = s.wayPoint.move(LEFT, v))
            'L' -> s.rotateClockwiseDeg(-v)
            'R' -> s.rotateClockwiseDeg(v)
            'F' -> s.copy(pos = Point(s.pos.x + v * s.wayPoint.x, s.pos.y + v * s.wayPoint.y))
            else -> error("Unexpected command: $this")
        }
    }
    return input.lines().fold(
        State(), { s, line -> line.apply(s) }
    ).pos.manhattanDist
}

data class Point(val x: Int, val y: Int)

val Point.manhattanDist get(): Int = x.absoluteValue + y.absoluteValue

enum class Dir {
    LEFT, UP, RIGHT, DOWN;
}

fun Point.move(dir: Dir, dist: Int = 1): Point = when (dir) {
    LEFT -> copy(x = x - dist)
    UP -> copy(y = y - dist)
    RIGHT -> copy(x = x + dist)
    DOWN -> copy(y = y + dist)
}

val Dir.right: Dir
    get() = when (this) {
        LEFT -> UP
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
    }

val Dir.left: Dir get() = right.right.right

/** Converts given angle from [deg] degrees to its equivalent number of quarterly clockwise rotations (between 0 and 3). */
fun degToClockwiseRotations(deg: Int): Int = ((deg % 360) + 360) % 360 / 90

fun Dir.rotateClockwiseDeg(deg: Int) = (0 until degToClockwiseRotations(deg)).fold(this, { acc, _ -> acc.right })
