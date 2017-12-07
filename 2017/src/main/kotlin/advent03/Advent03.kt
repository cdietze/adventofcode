package advent03

import kotlin.math.absoluteValue
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    println("Result part 1: ${manhattan(325489)}")
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

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(0, manhattan(1))
        assertEquals(3, manhattan(12))
        assertEquals(2, manhattan(23))
        assertEquals(31, manhattan(1024))
    }
}
