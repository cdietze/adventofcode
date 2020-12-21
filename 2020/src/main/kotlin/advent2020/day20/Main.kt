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
    val borderMap: Map<Long, List<Int>> = input.parseTiles().mapValues { e -> e.value.borders() }
    fun List<Int>.isCorner(): Boolean = count { b -> borderMap.map { it.value }.flatten().count { it == b } == 1 } >= 4
    return borderMap.filterValues { it.isCorner() }.map { it.key }.product()
}

fun resultPart2(input: String): Long {
    val tiles = input.parseTiles()

//    data class Alignment(val flipped: Boolean, val rotation: Int)
//
//    fun Tile.alignTop(borderInt) = ...
//    fun Tile.alignLeft(borderInt) = ...
//
//    typealias Grid = List<List<Pair<TileData,Alignment>>>
//
//    fun List<List<Pair<TileData,Alignment>>>.toSeaMap(): TileData

    TODO()
}

typealias TileData = List<String>

fun String.parseTiles(): Map<Long, TileData> = split("\n\n").map { tile ->
    val lines = tile.lines()
    Pair(Regex("(\\d+)").find(lines[0])!!.groupValues[1].toLong(), lines.drop(1))
}.toMap()

fun TileData.borders(): List<Int> {
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

fun TileData.rotateRight(): TileData =
    (0 until size).map { x ->
        (0 until size).map { y ->
            this[size - y - 1][x]
        }.joinToString(separator = "")
    }

fun TileData.flipVertically(): TileData =
    (0 until size).map { y ->
        this[size - y - 1]
    }
