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
        foldDirs(0) { acc, xo, yo -> acc + if (char(x + xo, y + yo) == c) 1 else 0 }

    return runSim(input.lines()) { x: Int, y: Int, c: Char ->
        when (c) {
            'L' -> if (neighborCount(x, y, '#') == 0) '#' else 'L'
            '#' -> if (neighborCount(x, y, '#') >= 4) 'L' else '#'
            else -> c
        }
    }
}

fun resultPart2(input: String): Int {
    tailrec fun List<String>.canSeeOccupiedSeat(x: Int, y: Int, xo: Int, yo: Int): Boolean {
        val xx = x + xo
        val yy = y + yo
        return when (val n = char(xx, yy)) {
            '#' -> true
            null, 'L' -> false
            '.' -> canSeeOccupiedSeat(xx, yy, xo, yo)
            else -> error("Unknown char: $n")
        }
    }

    fun List<String>.occupiedSeatCount(x: Int, y: Int): Int =
        foldDirs(0) { acc, xo, yo -> acc + if (canSeeOccupiedSeat(x, y, xo, yo)) 1 else 0 }

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
    return impl(initial).map { it.count { c -> c == '#' } }.sum()
}

fun List<String>.char(x: Int, y: Int): Char? = getOrNull(y)?.getOrNull(x)

/** Folds over all orthogonal and diagonal directions - there are 8 of these */
fun <R> foldDirs(initial: R, f: (acc: R, xo: Int, yo: Int) -> R): R {
    var acc: R = initial
    acc = f(acc, 0, -1)
    acc = f(acc, 1, -1)
    acc = f(acc, 1, 0)
    acc = f(acc, 1, 1)
    acc = f(acc, 0, 1)
    acc = f(acc, -1, +1)
    acc = f(acc, -1, 0)
    acc = f(acc, -1, -1)
    return acc
}
