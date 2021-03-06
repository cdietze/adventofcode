package advent03

import kotlin.coroutines.experimental.buildSequence
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = 325489
    println("Result part 1: ${manhattan(input)}")
    println("Result part 1 version 2: ${manhattan2(input)}")
    println("Result part 2: ${accValues().find { it.second > input }!!.second}")
}

fun maxSquareLoc(i: Int) = 4 * i * i + 4 * i
fun edgeLen(square: Int) = 2 * square

fun squareIndex(loc: Int): Int {
    var i = 0
    while (loc > maxSquareLoc(i)) {
        ++i
    }
    return i
}

fun manhattan(loc: Int): Int = manhattanImpl(loc - 1)

fun manhattan2(loc: Int): Int = spiral().elementAt(loc - 1).let { it.x.absoluteValue + it.y.absoluteValue }

fun manhattanImpl(loc: Int): Int {
    val square = squareIndex(loc)
    val maxSquareLoc = maxSquareLoc(square)
    val edgeLen = edgeLen(square)
    val cornerLocs = (0..4).map { i ->
        maxSquareLoc - i * edgeLen
    }.toList()
    val minCornerDist = cornerLocs.map { (it - loc).absoluteValue }.min()!!
    //  println("#manhattan, loc:$loc, square:$square, maxSquareLoc:$maxSquareLoc, edgeLen:$edgeLen, minCornerDist:$minCornerDist")
    return 2 * square - minCornerDist
}

data class Point(val x: Int, val y: Int)

fun accValues(): Sequence<Pair<Point, Int>> {
    val map: MutableMap<Point, Int> = mutableMapOf<Point, Int>().withDefault { 0 }
    fun calcAndStore(p: Point): Int {
        val sum = map.getValue(p.copy(x = p.x + 1)) +
                map.getValue(p.copy(x = p.x + 1, y = p.y + 1)) +
                map.getValue(p.copy(y = p.y + 1)) +
                map.getValue(p.copy(x = p.x - 1, y = p.y + 1)) +
                map.getValue(p.copy(x = p.x - 1)) +
                map.getValue(p.copy(x = p.x - 1, y = p.y - 1)) +
                map.getValue(p.copy(y = p.y - 1)) +
                map.getValue(p.copy(x = p.x + 1, y = p.y - 1))
        map[p] = sum
        return sum
    }
    map[Point(0, 0)] = 1
    return sequenceOf(Pair(Point(0, 0), 1)) + spiral().drop(1).map { Pair(it, calcAndStore(it)) }
}

fun spiral(): Sequence<Point> = buildSequence {
    var x = 0
    var y = 0
    yield(Point(x, y))
    var steps = 1
    while (true) {
        // move steps right
        (1..steps).forEach { yield(Point(++x, y)) }
        // move steps up
        (1..steps).forEach { yield(Point(x, ++y)) }
        // inc steps
        steps += 1
        // move steps left
        (1..steps).forEach { yield(Point(--x, y)) }
        // move steps down
        (1..steps).forEach { yield(Point(x, --y)) }
        // inc steps and repeat
        steps += 1
    }
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(0, manhattan(1))
        assertEquals(3, manhattan(12))
        assertEquals(2, manhattan(23))
        assertEquals(31, manhattan(1024))
    }
}
