package advent2020.day15

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText = "9,3,1,0,8,4"

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val list = input.split(",").map { it.toInt() }.toMutableList()
    while (list.size < 2020) {
        val next = list.dropLast(1).lastIndexOf(list.last()).let { i -> if (i < 0) 0 else list.size - i - 1 }
        list.add(next)
    }
    return list.last()
}

fun resultPart2(input: String, targetIndex: Long = 30000000L): Long {
    val numbers = input.split(",").map { it.toLong() }
    val map = numbers.dropLast(1).mapIndexed { index, v ->
        Pair(v, index.toLong())
    }.toMap().toMutableMap()
    var next = numbers.last()
    (numbers.size - 1 until targetIndex - 1).forEach { index ->
        val tmp = map[next]?.let { index - it } ?: 0
        map[next] = index
        next = tmp
    }
    return next
}
