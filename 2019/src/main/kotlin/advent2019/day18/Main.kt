@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package advent2019.day18

import java.util.LinkedList
import java.util.PriorityQueue

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
}

fun solvePart1(): Int {
    val map = inputText.lines()
    operator fun Point.invoke(): Char = map[y][x]
    val points = map.indices.flatMap { y ->
        map[y].indices.map { x -> Point(x, y) }
    }
    val nodePositions = points.filter { it() != '#' && it() != '.' }

    val nodes = nodePositions.associateBy({ it }) { nodePos ->
        val costMap = dijkstra(nodePos) { it() == '.' }
        Node(nodePos, nodePos(), nodePositions.filter { costMap.containsKey(it) && it != nodePos }.map {
            Connection(it, it(), costMap[it]!!)
        })
    }

    val initialState = State(
        node = nodes.values.first { it.c == '@' },
        keys = setOf(),
        cost = 0
    )
    val stateMap = HashMap<Point, List<State>>()
    val frontier = LinkedList<State>()
    frontier.add(initialState)
    stateMap[initialState.node.p] = listOf(initialState)

    while (frontier.isNotEmpty()) {
        val current = frontier.poll()!!
        current.node.connections.forEach { con ->
            if (!con.c.isUpperCase() || current.keys.contains(con.c.toLowerCase())) {
                val nextNode = nodes[con.target]!!
                val nextState = current.copy(
                    node = nextNode,
                    keys = if (nextNode.c.isLowerCase()) current.keys + nextNode.c else current.keys,
                    cost = current.cost + con.cost
                )
                if (!stateMap[nextNode.p].orEmpty().any { other -> nextState.isBetter(other) }) {
                    frontier.add(nextState)
                    stateMap.compute(nextNode.p) { _, v -> v.orEmpty() + nextState }
                }
            }
        }
    }
    return stateMap.values.flatten().maxWith(bestFinalStateComparator)!!.cost
}

val bestFinalStateComparator: Comparator<State> = compareBy<State> { it.keys.size }.thenByDescending { it.cost }
fun State.isBetter(other: State): Boolean =
    other.node.p == this.node.p && other.keys.containsAll(this.keys) && other.cost <= this.cost

data class Node(val p: Point, val c: Char, val connections: List<Connection>)
data class Connection(val target: Point, val c: Char, val cost: Int)

data class State(
    val node: Node,
    val keys: Set<Char>,
    val cost: Int
)

inline class Point(val longValue: Long) {
    constructor(x: Int, y: Int) : this((x.toLong() shl 32) or (y.toLong() and 0xffffffff))

    override fun toString(): String = "Point(${this.x},${this.y})"
}

val Point.x: Int get() = (this.longValue shr 32).toInt()
val Point.y: Int get() = (this.longValue and 0xffffffff).toInt()

fun Point.forEachNeighbor(f: (Point) -> Unit) {
    f(Point(x, y - 1)) // NORTH
    f(Point(x + 1, y)) // EAST
    f(Point(x, y + 1)) // SOUTH
    f(Point(x - 1, y)) // WEST
}

fun dijkstra(start: Point, passable: (Point) -> Boolean): Map<Point, Int> {
    data class P(val cost: Int, val p: Point)

    val frontier = PriorityQueue<P>(compareBy { it.cost })
    frontier.add(P(0, start))
    val costSoFar = HashMap<Point, Int>()
    costSoFar[start] = 0
    while (frontier.isNotEmpty()) {
        val current = frontier.poll()!!
        val nextCost = costSoFar[current.p]!! + 1
        current.p.forEachNeighbor { next ->
            if (!costSoFar.containsKey(next) || nextCost < costSoFar[next]!!) {
                costSoFar[next] = nextCost
                if (passable(next)) {
                    frontier.add(P(nextCost, next))
                }
            }
        }
    }
    return costSoFar
}
