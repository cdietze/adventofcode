package advent2020.day03

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long = treeCount(input, 3, 1)

fun resultPart2(input: String): Long =
    listOf(
        treeCount(input, 1, 1),
        treeCount(input, 3, 1),
        treeCount(input, 5, 1),
        treeCount(input, 7, 1),
        treeCount(input, 1, 2)
    ).fold(1, { acc, v -> acc * v })

fun treeCount(input: String, slopeRight: Int, slopeDown: Int): Long =
    input.lines()
        .filterIndexed { index, _ -> index % slopeDown == 0 }
        .mapIndexed { index, line ->
            val c = line[(index * slopeRight) % line.length]
            if (c.isTree) 1L else 0L
        }.sum()

val Char.isTree get() = this == '#'
