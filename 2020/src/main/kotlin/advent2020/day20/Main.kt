package advent2020.day20

import advent2020.AdventDay
import advent2020.product

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long {
    val borderMap: Map<Long, List<Int>> = input.parseTiles().mapValues { e -> e.value.borders() }
    fun List<Int>.isCorner(): Boolean = count { b -> borderMap.values.flatten().count { it == b } == 1 } >= 4
    return borderMap.filterValues { it.isCorner() }.map { it.key }.product()
}

fun resultPart2(input: String): Int {
    val tiles: Map<Long, List<String>> = input.parseTiles()
    val borderMap: Map<Long, List<Int>> = tiles.mapValues { e -> e.value.borders() }
    fun Long.toTopLeftCorner(): Pair<Long, TileData>? {
        fun borderCounts(data: TileData): List<Int> =
            data.borders().map { b -> borderMap.values.flatten().count { it == b } }

        var data = tiles[this]!!
        var borderCounts: List<Int> = borderCounts(data)
        if (borderCounts.count { it == 1 } == 4) {
            // Rotate until top and left borders are unique
            while (true) {
                if (borderCounts[0] == 1 && borderCounts[1] == 1) return Pair(this, data)
                data = data.rotateRight()
                borderCounts = borderCounts(data)
            }
        } else return null
    }

    fun Long.align(borderIndex: Int, targetIndex: Int): TileData =
        tiles[this]!!.let {
            if (borderIndex / 4 == targetIndex / 4) it else it.flipVertically()
        }.let {
            (0 until (borderIndex - targetIndex + 8) % 4)
                .fold(it) { acc, _ -> acc.rotateRight() }
        }

    tailrec fun List<Pair<Long, TileData>>.completeRow(): List<Pair<Long, TileData>> {
        val tile = this.last()
        val borderCode = tile.second.rightBorderCode()
        val next = borderMap.filter { e -> e.key != tile.first && e.value.contains(borderCode) }.entries.singleOrNull()
            ?: return this
        val borderIndex = next.value.indexOf(borderCode).also { check(it >= 0) }
        val data = next.key.align(borderIndex, 1).flipVertically()
        return this.plusElement(Pair(next.key, data)).completeRow()
    }

    tailrec fun List<Pair<Long, TileData>>.completeColumn(): List<Pair<Long, TileData>> {
        val tile = this.last()
        val borderCode = tile.second.bottomBorderCode()
        val next = borderMap.filter { e -> e.key != tile.first && e.value.contains(borderCode) }.entries.singleOrNull()
            ?: return this
        val borderIndex = next.value.indexOf(borderCode).also { check(it >= 0) }
        val data = next.key.align(borderIndex, 0)
        return this.plusElement(Pair(next.key, data)).completeColumn()
    }

    val firstTile: Pair<Long, TileData> = tiles.keys.mapNotNull { it.toTopLeftCorner() }.first()
    val firstCol = listOf(firstTile).completeColumn()
    val grid: List<List<Pair<Long, List<String>>>> = firstCol.map { col -> listOf(col).completeRow() }
    val bigTile = grid.merge()
    val bigTilePoints: Set<Pair<Int, Int>> = bigTile.toPointSet()
    val monsterMarks = mutableSetOf<Pair<Int, Int>>()
    bigTile.forEachIndexed { y, row ->
        row.indices.forEach { x ->
            val p = Pair(x, y)
            allMonsters.forEach { monster ->
                if (bigTilePoints.occursAt(monster, p)) {
                    monster.forEach { pp -> monsterMarks.add(Pair(p.first + pp.first, p.second + pp.second)) }
                }
            }
        }
    }
    return (bigTilePoints - monsterMarks).size
}

val monster: List<String> = listOf(
    "                  # ",
    "#    ##    ##    ###",
    " #  #  #  #  #  #   "
)

val allMonsters: Set<Set<Pair<Int, Int>>> = run {
    val x = listOf(
        monster,
        monster.rotateRight(),
        monster.rotateRight().rotateRight(),
        monster.rotateRight().rotateRight().rotateRight(),
    )
    val y = x + x.map { it.flipVertically() }
    y.map { it.toPointSet() }.toSet()
}

fun List<String>.toPointSet(): Set<Pair<Int, Int>> =
    this.flatMapIndexed { y, row -> row.mapIndexedNotNull { x, c -> if (c == '#') Pair(x, y) else null } }.toSet()

typealias TileData = List<String>

fun String.parseTiles(): Map<Long, TileData> = split("\n\n").map { tile ->
    val lines = tile.lines()
    Pair(Regex("(\\d+)").find(lines[0])!!.groupValues[1].toLong(), lines.drop(1))
}.toMap()

/**
 * Returns a list of all border-codes for [this] [TileData].
 */
fun TileData.borders(): List<Int> {
    fun String.lineToFlippedInt(): Int = reversed().lineCode()
    val top = this.first()
    val right = this.map { it.last() }.joinToString(separator = "")
    val bottom = this.last()
    val left = this.map { it.first() }.joinToString(separator = "")
    return listOf(
        top.lineCode(),
        left.lineToFlippedInt(),
        bottom.lineToFlippedInt(),
        right.lineCode(),
        bottom.lineCode(),
        left.lineCode(),
        top.lineToFlippedInt(),
        right.lineToFlippedInt(),
    )
}

fun String.lineCode(): Int = map { c -> if (c == '#') '1' else '0' }.joinToString(separator = "").toInt(2)
fun TileData.rightBorder(): String = this.map { it.last() }.joinToString(separator = "")
fun TileData.rightBorderCode(): Int = rightBorder().lineCode()
fun TileData.bottomBorder(): String = this.last()
fun TileData.bottomBorderCode(): Int = bottomBorder().lineCode()

fun TileData.rotateRight(): TileData =
    first().indices.map { x ->
        (size - 1 downTo 0).map { y ->
            this[y][x]
        }.joinToString(separator = "")
    }

fun TileData.flipVertically(): TileData =
    (0 until size).map { y ->
        this[size - y - 1]
    }

fun List<List<Pair<Long, List<String>>>>.merge(): TileData = flatMap { tileRow ->
    tileRow.map { tile -> tile.second.subList(1, tile.second.size - 1).map { it.substring(1, it.length - 1) } }
        .reduce { acc, list -> acc.zip(list) { a, b -> a + b } }
}

fun Set<Pair<Int, Int>>.occursAt(s: Set<Pair<Int, Int>>, p: Pair<Int, Int>): Boolean =
    s.all { pp -> contains(Pair(p.first + pp.first, p.second + pp.second)) }
