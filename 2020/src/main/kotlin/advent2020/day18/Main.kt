package advent2020.day18

import advent2020.AdventDay
import advent2020.CommonParsers.long
import advent2020.product
import parsek.*

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long = input.filter { it != ' ' }.lines().map {
    Part1.expr.parse(it).getOrFail().value
}.sum()

fun resultPart2(input: String): Long = input.filter { it != ' ' }.lines().map {
    Part2.mul.parse(it).getOrFail().value
}.sum()

object Part1 {
    val expr: Parser<Long> = Rule("expr") {
        ((long + parens) * (CharIn("*+") * (long + parens)).rep()).map { a, list ->
            list.fold(a) { acc, pair ->
                when (pair.first) {
                    '+' -> acc + pair.second
                    '*' -> acc * pair.second
                    else -> error("unknown op: ${pair.first}")
                }
            }
        }
    }
    val parens: Parser<Long> = Rule("parens") { P("(") * expr * P(")") }
}

object Part2 {
    val mul: Parser<Long> =
        Rule("mul") { add * (P("*") * add).rep() }.map { l, list -> l * list.product() }
    val add: Parser<Long> =
        Rule("add") { (long + parens) * (P("+") * (long + parens)).rep() }.map { l, list -> l + list.sum() }
    val parens: Parser<Long> = Rule("parens") { P("(") * mul * P(")") }
}