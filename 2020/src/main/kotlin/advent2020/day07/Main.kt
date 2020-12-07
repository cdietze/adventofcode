package advent2020.day07

import advent2020.AdventDay
import advent2020.CommonParsers.int
import advent2020.CommonParsers.word
import parsek.*

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val openSet = mutableSetOf("shiny gold")
    val resultSet = mutableSetOf("shiny gold")

    while (openSet.isNotEmpty()) {
//        println("openSet: $openSet")
//        println("resultSet: $resultSet")
        openSet.clear()
        input.lines().filter {
            val words = it.split(" ")
            val color = words.take(2).joinToString(" ")
            val base: String = words.drop(2).joinToString(" ")
//            val matches = resultSet.any { v -> base.contains(v) }
//            println("color: $color, base: $base, matches: $matches")
            resultSet.any { v -> base.contains(v) }
                .also {
                    if (it && !resultSet.contains(color)) {
                        openSet.add(color)
                        resultSet.add(color)
                    }
                }
        }
    }
    return resultSet.size - 1
}

fun resultPart2(input: String): Long {
    val bagMap = input.lines().map { lineP.parse(it).getOrFail().value }.toMap()
    return bagCount(bagMap, "shiny gold") - 1
}

fun bagCount(bagMap: BagMap, color: String): Long =
    (bagMap[color]?.map { entry -> bagCount(bagMap, entry.color) * entry.count.toLong() }?.sum() ?: 0) + 1

data class BagCount(val count: Int, val color: String)

typealias BagMap = Map<String, List<BagCount>>
typealias Line = Pair<String, List<BagCount>>

val colorP: Parser<String> = Rule("color") { word * P(" ") * word }.map { p -> p.toList().joinToString(" ") }
val bagCountP: Parser<BagCount> = Rule("bagCount") { int * P(" ") * colorP }.map(::BagCount)
val lineP: Parser<Line> =
    Rule("line") { colorP * (CharPred { it !in '0'..'9' }.map { }.rep() * bagCountP).rep() }.map(::Line)
