package advent2020.day09

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    private val part1 by lazy { resultPart1(inputText) }

    override fun resultPart1(): Any = part1
    override fun resultPart2(): Any = resultPart2(inputText, part1)
}

fun resultPart1(input: String, preambleLength: Int = 25): Long {
    fun Long.valid(preamble: List<Long>): Boolean = preamble.withIndex().any { a ->
        preamble.drop(a.index + 1).contains(this - a.value)
    }

    val numbers = input.lines().map { it.toLong() }
    return numbers.windowed(preambleLength + 1).first {
        !it.last().valid(it.dropLast(1))
    }.last()
}

fun resultPart2(input: String, findMe: Long): Long {
    val numbers = input.lines().map { it.toLong() }
    tailrec fun impl(range: IntRange, sum: Long): IntRange = when {
        sum == findMe -> range
        sum < findMe -> {
            // add next number
            impl((range.first..range.last + 1), sum + numbers[range.last + 1])
        }
        else -> {
            // remove first number
            impl((range.first + 1..range.last), sum - numbers[range.first])
        }
    }

    return numbers.slice(impl(0..0, numbers[0])).let { list ->
        list.minOrNull()!! + list.maxOrNull()!!
    }
}
