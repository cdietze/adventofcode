package advent2020.day20

import advent2020.AdventDay
import advent2020.product

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
//     override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long {
    val tiles: List<Pair<Long, List<Int>>> = input.split("\n\n").map { tile ->
        val lines = tile.lines()
        val id = Regex("(\\d+)").find(lines[0])!!.groupValues[1].toLong()
        Pair(id, lines.drop(1).borders())
    }

    val counts = tiles.map { it.second }.flatten().groupingBy { it }.eachCount().filter { it.value > 2 }.let {
        println("Counts: $it")
    }

    fun List<Int>.isCorner(): Boolean = count { b -> tiles.map { it.second }.flatten().count { it == b } == 1 } >= 4
    return tiles.filter { it.second.isCorner() }.map { it.first }.product()
}

fun List<String>.borders(): List<Int> {
    fun String.lineToInt(): Int = map { c -> if (c == '#') '1' else '0' }.joinToString(separator = "").toInt(2)
    fun String.lineToFlippedInt(): Int = reversed().lineToInt()
    val borderStrings = listOf(
        this.first(),
        this.map { it.last() }.joinToString(separator = ""),
        this.last(),
        this.map { it.first() }.joinToString(separator = ""),
    )
    return borderStrings.flatMap { b -> listOf(b.lineToInt(), b.lineToFlippedInt()) }
}

fun resultPart2(input: String): Long {
    val tiles: List<Pair<Long, List<Int>>> = input.split("\n\n").map { tile ->
        val lines = tile.lines()
        val id = Regex("(\\d+)").find(lines[0])!!.groupValues[1].toLong()
        Pair(id, lines.drop(1).borders())
    }

    fun List<Int>.isCorner(): Boolean = count { b -> tiles.map { it.second }.flatten().count { it == b } == 1 } >= 4
//    return tiles.first { it.second.isCorner() }.map { it.first }

    TODO()
}

fun List<String>.rotateRight(): List<String> =
    (0 until size).map { x ->
        (0 until size).map { y ->
            this[size - y - 1][x]
        }.joinToString(separator = "")
    }

fun List<String>.flipVertically(): List<String> =
    (0 until size).map { y ->
        this[size - y - 1]
    }
