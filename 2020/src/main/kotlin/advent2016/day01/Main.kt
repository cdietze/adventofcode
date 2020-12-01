package advent2016.day01

import advent2020.AdventDay
import kotlin.math.absoluteValue

object Main : AdventDay {
    @JvmStatic
    fun main(args: Array<String>) = run()

    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }
    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val initialState = State(Point(0, 0), Dir.Up, setOf())
    val result: State = input.split(",").map { it.trim() }.fold(
        initialState, { s, inst -> s.step(inst).last() }
    )
    return result.pos.manhattanDist
}

fun resultPart2(input: String): Int {
    val insts = input.split(",").map { it.trim() }
    return State(Point(0, 0), Dir.Up, setOf()).steps(insts).first { it.visited.contains(it.pos) }.pos.manhattanDist
}

data class Point(val x: Int, val y: Int)

val Point.manhattanDist: Int get() = x.absoluteValue + y.absoluteValue

fun Point.move(dir: Dir, dist: Int = 1): Point = when (dir) {
    Dir.Up -> copy(y = y + dist)
    Dir.Down -> copy(y = y - dist)
    Dir.Left -> copy(x = x - dist)
    Dir.Right -> copy(x = x + dist)
}

enum class Dir {
    Up, Down, Left, Right
}

val Dir.right: Dir
    get() = when (this) {
        Dir.Up -> Dir.Right
        Dir.Right -> Dir.Down
        Dir.Down -> Dir.Left
        Dir.Left -> Dir.Up
    }

val Dir.left: Dir
    get() = right.right.right

fun Dir.turn(c: Char): Dir =
    when (c) {
        'L' -> left
        'R' -> right
        else -> error("unknown turn instruction: $c")
    }

data class State(val pos: Point, val dir: Dir, val visited: Set<Point>)

fun State.turn(c: Char): State = copy(dir = dir.turn(c))
fun State.move(dist: Int): Sequence<State> = sequence {
    var updatedState = this@move
    repeat(dist) {
        updatedState = updatedState.moveOne()
        yield(updatedState)
    }
}

fun State.moveOne(): State {
    val pos = pos.move(dir)
    return copy(pos = pos, visited = visited + this.pos)
}

fun State.step(inst: String): Sequence<State> = turn(inst[0]).move(inst.substring(1).toInt())
fun State.steps(insts: List<String>): Sequence<State> = sequence {
    var state: State = this@steps
    insts.forEach {
        state.step(it).forEach {
            state = it
            yield(it)
        }
    }
}
