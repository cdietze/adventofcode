package advent2020.day19

import advent2020.AdventDay
import parsek.*

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }
    private val inputText2 by lazy { this.javaClass.getResource("input2.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText2)
}

fun resultPart1(input: String): Int {
    val parts = input.split("\n\n")
    val rules = parserMap(parts[0])
    return parts[1].lines().count {
        rules.isValid(it)
    }
}

fun resultPart2(input: String): Int {
    val parts = input.split("\n\n")
    val rules = parserMap(parts[0])
    return parts[1].lines().count { line ->
        rules.isValid(line)
    }
}

fun parserMap(
    input: String
): Map<Int, Parser<Unit>> {
    val rules = mutableMapOf<Int, Parser<Unit>>()
    input.lines().forEach { line ->
        line.split(": ").let { (idString, def) ->
            idString.toInt()
                .let { id -> rules[id] = parser(id, def, rules) }
        }
    }
    return rules
}

private fun parser(id: Int, def: String, rules: Map<Int, Parser<Unit>>): Parser<Unit> =
    Rule("Rule$id") {
        def.split("|").map { term ->
            term.trim().split(" ").map { symbol ->
                symbol.toIntOrNull().let { rules[it] } ?: P(symbol.filter { c -> c.isLetter() })
            }.let { it.reduce { acc, parser -> acc * parser } }
        }.let { it.reduce { acc, parser -> acc + parser } }
    }

fun Map<Int, Parser<Unit>>.isValid(text: String): Boolean = this[0]!!.parseComplete(text).isSuccess

fun <T> Parser<T>.parseComplete(input: String, index: Int = 0): ParseResult<T> =
    (this * End).parse(input, index)
