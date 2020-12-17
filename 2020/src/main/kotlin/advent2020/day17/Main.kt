package advent2020.day17

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

typealias World = Set<Triple<Int, Int, Int>>

fun World.activeNeighbors(p: Triple<Int, Int, Int>): Int =
    (p.first - 1..p.first + 1).sumOf { x ->
        (p.second - 1..p.second + 1).sumOf { y ->
            (p.third - 1..p.third + 1).count { z ->
                Triple(x, y, z).let { it != p && contains(it) }
            }
        }
    }

fun Iterable<Int>.toRange(): IntRange = IntRange(minOrNull()!! - 1, maxOrNull()!! + 1)

fun resultPart1(input: String): Int {
    fun World.step(): World {
        val xRange = map { it.first }.toRange()
        val yRange = map { it.second }.toRange()
        val zRange = map { it.third }.toRange()
        return xRange.flatMap { x ->
            yRange.flatMap { y ->
                zRange.mapNotNull { z ->
                    val p = Triple(x, y, z)
                    val active = contains(p)
                    val nextActive = if (active) (activeNeighbors(p) in 2..3) else (activeNeighbors(p) == 3)
                    // println("Looking at $p, active: $active, nextActive: $nextActive, neighbors: ${activeNeighbors(p)}")
                    if (nextActive) p else null
                }
            }
        }.toSet().also {
            //    println("old world: $this")
            //  println("New world: $it")
        }
    }

    val initialWorld: World =
        input.lines().flatMapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') Triple(x, y, 0) else null } }
            .toSet()

    return (1..6).fold(initialWorld) { acc: World, _: Int -> acc.step() }.size
}

typealias World4D = Set<List<Int>>

val dims = 4

fun World4D.activeNeighbors(p: List<Int>): Int {
    fun impl(d: Int, state: List<Int>): Int =
        if (d == dims) {
            if (p != state && contains(state)) 1 else 0
        } else (p[d] - 1..p[d] + 1).sumBy { impl(d + 1, state + it) }
    return impl(0, listOf())
}

fun resultPart2(input: String): Int {
    fun World4D.step(): World4D {
        val ranges = (0 until dims).map { dim -> map { p -> p[dim] }.toRange() }
        return ranges[0].flatMap { x ->
            ranges[1].flatMap { y ->
                ranges[2].flatMap { z ->
                    ranges[3].mapNotNull { w ->
                        val p = listOf(x, y, z, w)
                        val active = contains(p)
                        val nextActive = if (active) (activeNeighbors(p) in 2..3) else (activeNeighbors(p) == 3)
                        //println("Looking at $p, active: $active, nextActive: $nextActive, neighbors: ${activeNeighbors(p)}")
                        if (nextActive) p else null
                    }
                }
            }
        }.toSet().also {
//            println("New world: $it")
        }
    }

    val initialWorld: World4D =
        input.lines()
            .flatMapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') listOf(x, y, 0, 0) else null } }
            .toSet()

    return (1..6).fold(initialWorld) { acc, _ -> acc.step() }.size
}
