package advent2020.day10

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val list: List<Long> = listOf(0L) + input.lines().map { it.toLong() }.sorted()
    val jolt1 = list.zipWithNext().filter { (a, b) -> b - a == 1L }.count()
    val jolt3 = list.zipWithNext().filter { (a, b) -> b - a == 3L }.count() + 1
    return jolt1 * jolt3
}

fun resultPart2(input: String): Long {
    val list: List<Long> = listOf(0L) + input.lines().map { it.toLong() }.sorted()
    val pathMap = mutableMapOf(0L to 1L)
    fun pathCount(num: Long): Long = when {
        num < 0 -> 0
        !list.contains(num) -> 0
        else -> pathMap.getOrPut(num) {
            (1L..3L).sumOf {
                pathCount(num - it)
            }
        }
    }
    return pathCount(list.maxOrNull()!!)
}
