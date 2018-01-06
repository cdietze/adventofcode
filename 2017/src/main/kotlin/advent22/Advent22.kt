package advent22

import advent22.NodeState.*
import java.io.File
import kotlin.test.fail

typealias Direction = Int
fun Direction.right() = (this + 1) % 4
fun Direction.left() = (this + 3) % 4
val UP = 0
val RIGHT = 1
val DOWN = 2
val LEFT = 3

fun Point.move(dir: Direction): Point = when (dir) {
    UP -> copy(y = y - 1)
    RIGHT -> copy(x = x + 1)
    DOWN -> copy(y = y + 1)
    LEFT -> copy(x = x - 1)
    else -> fail()
}

data class Point(val x: Int, val y: Int)

data class State(val infected: MutableSet<Point>, val pos: Point = Point(0, 0), val dir: Direction = UP, val infectionCount: Int = 0)

fun State.step(): State {
    val newDir = if (infected.contains(pos)) dir.right() else dir.left()
    val newCount = if (infected.contains(pos)) infectionCount else infectionCount + 1
    if (infected.contains(pos)) infected.remove(pos) else infected.add(pos)
    return copy(pos = pos.move(newDir), dir = newDir, infectionCount = newCount)
}

enum class NodeState {
    CLEAN,
    WEAKENED,
    INFECTED,
    FLAGGED
}

fun NodeState.next() = when (this) {
    CLEAN -> WEAKENED
    WEAKENED -> INFECTED
    INFECTED -> FLAGGED
    FLAGGED -> CLEAN
}

fun NodeState.nextDir(dir: Direction) = when (this) {
    CLEAN -> dir.left()
    WEAKENED -> dir
    INFECTED -> dir.right()
    FLAGGED -> dir.right().right()
}

data class State2(val nodes: MutableMap<Point, NodeState>, val pos: Point = Point(0, 0), val dir: Direction = UP, val infectionCount: Int = 0)

fun State2.step(): State2 {
    val nodeState = nodes.getOrDefault(pos, CLEAN)
    val newDir = nodeState.nextDir(dir)
    val newCount = if (nodeState == WEAKENED) infectionCount + 1 else infectionCount
    nodes[pos] = nodeState.next()
    return copy(pos = pos.move(newDir), dir = newDir, infectionCount = newCount)
}

fun main(args: Array<String>) {
    val input = mutableSetOf<Point>()
    File("src/main/kotlin/advent22/input.txt").readLines().forEachIndexed { y, row ->
        row.forEachIndexed { x, c -> if (c == '#') input.add(Point(x, y)) }
    }
    val start = Point(12, 12)
    val state = (0 until 10000).fold(State(input.toMutableSet(), pos = start), { acc, _ -> acc.step() })
    println("Result part 1: ${state.infectionCount}")
    val state2 = (0 until 10000000).fold(State2(input.map { Pair(it, INFECTED) }.toMap().toMutableMap(), pos = start), { acc, _ -> acc.step() })
    println("Result part 2: ${state2.infectionCount}")
}
