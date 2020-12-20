package advent2020.day20

import advent2020.AdventDay
import advent2020.product

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
}

fun resultPart1(input: String): Long {
    val tiles: Map<Long, List<Int>> = input.split("\n\n").map { tile ->
        val lines = tile.lines()
        val id = Regex("(\\d+)").find(lines[0])!!.groupValues[1].toLong()
        Pair(id, lines.drop(1).borders())
    }.toMap()

    fun List<Int>.isCorner(): Boolean = count { b -> tiles.values.flatten().count { it == b } == 1 } >= 4
    return tiles.filter { it.value.isCorner() }.map { it.key }.product()
}

fun List<String>.borders(): List<Int> {
    fun String.lineToInt(): Int = map { c -> if (c == '#') '1' else '0' }.joinToString(separator = "").toInt(2)
    fun String.lineToFlippedInt(): Int = reversed().lineToInt()
    val borderStrings = listOf(
        this.first(),
        this.last(),
        this.map { it.first() }.joinToString(separator = ""),
        this.map { it.last() }.joinToString(separator = "")
    )
    return borderStrings.flatMap { b -> listOf(b.lineToInt(), b.lineToFlippedInt()) }
}
