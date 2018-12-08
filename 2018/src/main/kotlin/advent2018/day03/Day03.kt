package advent2018.day03

import advent2018.common.int
import parsek.*
import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day03/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

val claims: List<Claim> by lazy {
    inputFile
        .readLines()
        .map { line -> claimParser.parse(line).getOrFail().value }
}

fun dataMap(): Map<Point, Int> {
    val m = mutableMapOf<Point, Int>().withDefault { 0 }
    claims.forEach { claim ->
        claim.points().forEach { p ->
            m[p] = m.getValue(p) + 1
        }
    }
    return m
}

fun solvePart1(): Int {
    return dataMap().filterValues { it >= 2 }.size
}

fun solvePart2(): Int {
    val m = dataMap()
    return claims.first { claim -> claim.points().all { m.getValue(it) == 1 } }.id
}

data class Claim(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int)

fun Claim.points(): Sequence<Point> = sequence {
    val claim = this@points
    for (x in claim.x until claim.x + claim.width) {
        for (y in claim.y until claim.y + claim.height) {
            yield(Point(x, y))
        }
    }
}

// Example input string:
// #947 @ 245,231: 29x13
val claimParser: Parser<Claim> = Rule("Claim") {
    (P("#") * int * P(" @ ") * int * P(",") * int * P(": ") * int * P("x") * int).map(::Claim)
}

data class Point(val x: Int, val y: Int)
