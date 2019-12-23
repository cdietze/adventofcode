@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package advent2019.day18

import java.util.LinkedList
import java.util.PriorityQueue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main() {
    println(
        "took: ${measureTime {
            println("Result part 1: ${solvePart1()}")
        }}"
    )
    println(
        "took: ${measureTime {
            println("Result part 2: ${solvePart2()}")
        }}"
    )
}

fun solvePart1(): Int {
    val inputText = object {}.javaClass.getResource("input.txt").readText()

    data class Connection(val target: Point, val c: Char, val cost: Int)
    data class Node(val p: Point, val c: Char, val connections: List<Connection>)
    data class State(
        val node: Node,
        val keys: Set<Char>,
        val cost: Int
    )

    val bestFinalStateComparator: Comparator<State> = compareBy<State> { it.keys.size }.thenByDescending { it.cost }
    fun State.isBetter(other: State): Boolean =
        other.node.p == this.node.p && other.keys.containsAll(this.keys) && other.cost <= this.cost

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

fun solvePart2(): Int {
    val inputText = object {}.javaClass.getResource("input_part2.txt").readText()

    data class Node(val p: Point, val c: Char)
    data class Edge(val target: Node, val cost: Int)
    data class State(
        val bots: List<Point>,
        val keys: Set<Char>,
        val cost: Int
    )

    val map = inputText.lines()
    operator fun Point.invoke(): Char = map[y][x]
    val points = map.indices.flatMap { y ->
        map[y].indices.map { x -> Point(x, y) }
    }
    val nodes = points.filter { it() != '#' && it() != '.' }.map { Node(it, it()) }

    val nodeMap: Map<Point, Node> = nodes.associateBy({ it.p }, { it })

    val edgeMap = nodes.associateBy({ it.p }) { node ->
        val costMap = dijkstra(node.p) { it() == '.' }
        nodes.filter { costMap.containsKey(it.p) && it.p != node.p }.map {
            Edge(it, costMap[it.p]!!)
        }
    }

    fun reachableKeys(pos: Point, keys: Set<Char>): List<Pair<Node, Int>> {
        data class P(val cost: Int, val p: Point)

        val frontier = PriorityQueue<P>(compareBy { it.cost })
        frontier.add(P(0, pos))
        val costSoFar = HashMap<Point, Int>()
        costSoFar[pos] = 0
        while (frontier.isNotEmpty()) {
            val current = frontier.poll()!!
            edgeMap[current.p]!!.forEach { next ->
                val nextCost = costSoFar[current.p]!! + next.cost
                if (!costSoFar.containsKey(next.target.p) || nextCost < costSoFar[next.target.p]!!) {
                    costSoFar[next.target.p] = nextCost
                    if (next.target.c.isUpperCase() && !keys.contains(next.target.c.toLowerCase())) {
                        // println("cannot pass through door")
                    } else {
                        frontier.add(P(nextCost, next.target.p))
                    }
                }
            }
        }
        return costSoFar.toList()
            .map { Pair(nodeMap[it.first]!!, it.second) }
            .filter { it.second > 0 }
            .filter { it.first.c.isLowerCase() && !keys.contains(it.first.c) }
            .apply {
                //   println("reachableKeys($pos,$keys) -> $this")
            }
    }

    val initialState = State(
        bots = nodes.filter { it.c == '@' }.map { it.p },
        keys = setOf('@'),
        cost = 0
    )
    val frontier = LinkedList<State>()
    frontier.add(initialState)

    val cache = mutableMapOf<Pair<List<Point>, Set<Char>>, Int>()
    cache[Pair(initialState.bots, initialState.keys)] = 0
    var loopCount = 0
    while (frontier.isNotEmpty()) {
        if (loopCount % 1000 == 0) {
            println("Loop: $loopCount, Frontier size: ${frontier.size}, cache.size: ${cache.size}, maxKeys: ${cache.map { it.key.second.size }.max()}")
        }
        loopCount++
        val current = frontier.poll()!!
        current.bots.mapIndexed { botIndex, botPos ->
            reachableKeys(botPos, current.keys).forEach { reachedKey ->
                val nextState = current.copy(
                    bots = current.bots.updated(botIndex, reachedKey.first.p),
                    keys = current.keys + reachedKey.first.c,
                    cost = current.cost + reachedKey.second
                )
                val cacheKey = Pair(nextState.bots, nextState.keys)
                if (cache[cacheKey]?.let { it <= nextState.cost } == true) {
                } else {
                    cache[cacheKey] = nextState.cost
                    frontier.add(nextState)
                }
            }
        }
    }
    println("final loopCount: $loopCount")
    return cache.maxWith(compareBy({ it.key.second.size }, { -it.value }))!!.apply {
        println("Best: $this")
    }.value
}

fun <E> Iterable<E>.updated(index: Int, elem: E) = mapIndexed { i, existing -> if (i == index) elem else existing }

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
