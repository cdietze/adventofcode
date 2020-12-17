package advent2020.day17

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = result(3, inputText)
    override fun resultPart2(): Any = result(4, inputText)
}

fun Iterable<Int>.toRange(): IntRange = IntRange(minOrNull()!! - 1, maxOrNull()!! + 1)

typealias World = Set<List<Int>>

fun World.activeNeighbors(p: List<Int>): Int {
    fun impl(d: Int, state: List<Int>): Int =
        if (d == p.size) {
            if (p != state && contains(state)) 1 else 0
        } else (p[d] - 1..p[d] + 1).sumBy { impl(d + 1, state + it) }
    return impl(0, listOf())
}

fun List<IntRange>.points(): List<List<Int>> {
    val ranges = this
    fun impl(d: Int, state: List<Int>): List<List<Int>> {
        return if (d >= ranges.size) listOf(state)
        else {
            ranges[d].flatMap { v -> impl(d + 1, state + v) }
        }
    }
    return impl(0, listOf())
}

fun result(dims: Int, input: String): Int {
    fun World.step(): World {
        val ranges: List<IntRange> = (0 until dims).map { dim -> map { p -> p[dim] }.toRange() }
        return ranges.points().mapNotNull { p ->
            val active = contains(p)
            val nextActive = if (active) (activeNeighbors(p) in 2..3) else (activeNeighbors(p) == 3)
            if (nextActive) p else null
        }.toSet()
    }

    val initialWorld: World =
        input.lines()
            .flatMapIndexed { y, s ->
                s.mapIndexedNotNull { x, c ->
                    if (c == '#') List(dims) {
                        when (it) {
                            0 -> x; 1 -> y; else -> 0
                        }
                    } else null
                }
            }.toSet()
    return (1..6).fold(initialWorld) { acc, _ -> acc.step() }.size
}
