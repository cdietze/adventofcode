package advent2020.day06

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long =
    input.split("\n\n").map { group ->
        group.filter { c -> c in ('a'..'z') }.toSet().size.toLong()
    }.sum()

fun resultPart2(input: String): Long =
    input.split("\n\n").map { group ->
        group.lines().map { it.toSet() }.reduce { a, b -> a.intersect(b) }.size.toLong()
    }.sum()
