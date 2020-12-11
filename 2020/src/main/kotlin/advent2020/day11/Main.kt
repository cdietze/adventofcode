package advent2020.day11

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    fun List<String>.neighborCount(x: Int, y: Int, c: Char): Int =
        foldNeighbors(0, x, y, { acc, xx, yy -> acc + if (char(xx, yy) == c) 1 else 0 })

    return runSim(input.lines()) { x: Int, y: Int, c: Char ->
        when (c) {
            'L' -> if (neighborCount(x, y, '#') == 0) '#' else 'L'
            '#' -> if (neighborCount(x, y, '#') >= 4) 'L' else '#'
            else -> c
        }
    }
}

fun resultPart2(input: String): Int {
    tailrec fun List<String>.canSeeOccupiedSeat(x: Int, y: Int, xoff: Int, yoff: Int): Boolean {
        val xx = x + xoff
        val yy = y + yoff
        return when (val n = char(xx, yy)) {
            '#' -> true
            null, 'L' -> false
            '.' -> canSeeOccupiedSeat(xx, yy, xoff, yoff)
            else -> error("Unknown char: $n")
        }
    }

    fun List<String>.occupiedSeatCount(x: Int, y: Int): Int {
        fun Boolean.toInt() = if (this) 1 else 0
        return canSeeOccupiedSeat(x, y, 0, -1).toInt() +
                canSeeOccupiedSeat(x, y, 1, -1).toInt() +
                canSeeOccupiedSeat(x, y, 1, 0).toInt() +
                canSeeOccupiedSeat(x, y, 1, 1).toInt() +
                canSeeOccupiedSeat(x, y, 0, 1).toInt() +
                canSeeOccupiedSeat(x, y, -1, 1).toInt() +
                canSeeOccupiedSeat(x, y, -1, 0).toInt() +
                canSeeOccupiedSeat(x, y, -1, -1).toInt()
    }

    return runSim(input.lines()) { x: Int, y: Int, c: Char ->
        when (c) {
            'L' -> if (occupiedSeatCount(x, y) == 0) '#' else 'L'
            '#' -> if (occupiedSeatCount(x, y) >= 5) 'L' else '#'
            else -> c
        }
    }
}

fun runSim(initial: List<String>, cellStep: (List<String>.(x: Int, y: Int, c: Char) -> Char)): Int {
    // Convenience function, in the stdlib there is only [CharSequence.map] which produces a [List<R>]
    fun String.mapIndexed(f: (index: Int, c: Char) -> Char): String {
        val sb = StringBuilder()
        forEachIndexed { index, c -> sb.append(f(index, c)) }
        return sb.toString()
    }

    fun List<String>.step(): List<String> =
        this.mapIndexed { y, row -> row.mapIndexed { x, c -> cellStep(x, y, c) } }

    tailrec fun impl(state: List<String>): List<String> {
        val nextState = state.step()
        return if (nextState == state) state else impl(nextState)
    }
    return impl(initial).map { it.count { it == '#' } }.sum()
}

fun List<String>.char(x: Int, y: Int): Char? = getOrNull(y)?.getOrNull(x)

fun <R> foldNeighbors(initial: R, x: Int, y: Int, f: (acc: R, x: Int, y: Int) -> R): R {
    var acc: R = initial
    acc = f(acc, x, y - 1)
    acc = f(acc, x + 1, y - 1)
    acc = f(acc, x + 1, y)
    acc = f(acc, x + 1, y + 1)
    acc = f(acc, x, y + 1)
    acc = f(acc, x - 1, y + 1)
    acc = f(acc, x - 1, y)
    acc = f(acc, x - 1, y - 1)
    return acc
}
