package advent2020.day02

import advent2020.AdventDay
import advent2020.CommonParsers.int
import advent2020.CommonParsers.space
import parsek.*

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val lines = input.lines().map { lineParser.parse(it).getOrFail().value }
    return lines.count { it.validPasswordPolicy1() }
}

fun resultPart2(input: String): Int {
    val lines = input.lines().map { lineParser.parse(it).getOrFail().value }
    return lines.count { it.validPasswordPolicy2() }
}

data class Line(val min: Int, val max: Int, val c: Char, val password: String)

val charParser: Parser<Char> = CharIn('a'..'z')
val passwordParser: Parser<String> = WhileCharIn('a'..'z').capture()
val lineParser: Parser<Line> =
    (int * P("-") * int * space * charParser * P(":") * space * passwordParser).map(::Line)

fun Line.validPasswordPolicy1(): Boolean = (min..max).contains(password.count { it == c })
fun Line.validPasswordPolicy2(): Boolean = listOf(min, max).count { password.getOrNull(it - 1) == c } == 1
