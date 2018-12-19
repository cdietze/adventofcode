package advent2018.common

import kotlin.math.max
import kotlin.math.min

data class Rect(val x: Int, val y: Int, val width: Int, val height: Int)

val Rect.area: Long get() = width.toLong() * height

val Rect.x2: Int get() = x + width
val Rect.y2: Int get() = y + height

fun Rect.enclose(p: Point): Rect {
    val minX = min(p.x, x)
    val minY = min(p.y, y)
    val maxX = max(p.x, x + width)
    val maxY = max(p.y, y + height)
    return Rect(minX, minY, maxX - minX, maxY - minY)
}

fun Rect.points(): Sequence<Point> = sequence {
    for (y in y until y2) {
        for (x in x until x2) {
            yield(Point(x, y))
        }
    }
}

fun Rect.border(): Sequence<Point> = sequence {
    for (x in x..x2) {
        yield(Point(x, y))
        yield(Point(x, y2))
    }
    for (y in y..y2) {
        yield(Point(x, y))
        yield(Point(x2, y))
    }
}

fun Rect.expand(v: Int) = Rect(x - v, y - v, width + 2 * v, height + 2 * v)
