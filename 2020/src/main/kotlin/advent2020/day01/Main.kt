package advent2020.day01

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val numbers = input.lines().map { it.toInt() }.asSequence()
    return numbers.withIndex().mapNotNull { a ->
        numbers.drop(a.index + 1).firstOrNull { a.value + it == 2020 }?.let { a.value * it }
    }.firstOrNull() ?: error("Found no pair with sum 2020")
}

fun resultPart2(input: String): Int {
    val numbers = input.lines().map { it.toInt() }
    for (a in numbers.indices) {
        for (b in a + 1 until numbers.size) {
            for (c in b + 1 until numbers.size) {
                if (numbers[a] + numbers[b] + numbers[c] == 2020) return numbers[a] * numbers[b] * numbers[c];
            }
        }
    }
    error("Found no triple with sum 2020")
}
