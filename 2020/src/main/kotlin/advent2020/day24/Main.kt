package advent2020.day24

import advent2020.AdventDay
import parsek.*

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int =
    input.lines().map { lineParser.parse(it).getOrFail().value }
        .fold<Coord, Set<Coord>>(emptySet()) { acc, s -> if (s in acc) acc - s else acc + s }.size

fun resultPart2(input: String): Int {
    /** Returns a map that contains how many black tiles neighbor given `key` coord */
    fun Set<Coord>.toTileCountMap(): Map<Coord, Int> {
        val map = mutableMapOf<Coord, Int>()
        this.forEach { c -> directions.forEach { dir -> map.compute(c + dir) { _, i -> (i ?: 0) + 1 } } }
        return map
    }

    fun Set<Coord>.step(): Set<Coord> {
        val countMap = toTileCountMap()
        val tiles = mutableSetOf<Coord>()
        forEach { c -> countMap.getOrDefault(c, 0).let { i -> if (i != 0 && i <= 2) tiles.add(c) } }
        tiles.addAll(countMap.filterValues { it == 2 }.filterKeys { it !in this }.keys)
        return tiles
    }

    val initalTiles: Set<Coord> = input.lines().map { lineParser.parse(it).getOrFail().value }
        .fold(emptySet()) { acc, s -> if (s in acc) acc - s else acc + s }
    return (1..100).fold(initalTiles) { acc, _ -> acc.step() }.size
}

/** https://www.redblobgames.com/grids/hexagons/#coordinates-axial */
data class Coord(val q: Int, val r: Int)

operator fun Coord.plus(o: Coord): Coord = Coord(q + o.q, r + o.r)

val e = Coord(1, 0)
val se = Coord(0, 1)
val sw = Coord(-1, 1)
val w = Coord(-1, 0)
val nw = Coord(0, -1)
val ne = Coord(1, -1)

val directions = listOf(e, se, sw, w, nw, ne)

val coordParser: Parser<Coord> = Rule("coord") {
    P("e").map { e } + P("se").map { se } + P("sw").map { sw } + P("w").map { w } + P("nw").map { nw } + P("ne").map { ne }
}

val lineParser: Parser<Coord> =
    Rule("line") { coordParser.rep().map { it.fold(Coord(0, 0)) { acc, coord -> acc + coord } } * End }
