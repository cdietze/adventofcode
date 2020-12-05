package advent2020.day05

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long {
    return input.lines().map { it.seatId }.maxOrNull()!!
}

fun resultPart2(input: String): Long {
    val list = input.lines().map { it.seatId }.sorted()
    return list.zipWithNext().first { (a, b) -> b - a != 1L }.first + 1
}

val String.seatId: Long
    get() {
        val binary = this.map {
            when (it) {
                'B' -> '1'
                'F' -> '0'
                'R' -> '1'
                'L' -> '0'
                else -> error("Unexpected char: '$it'")
            }
        }.joinToString(separator = "")
        return binary.toLong(2)
    }
