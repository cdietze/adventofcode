package advent2018.day01

import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day01/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int =
    inputFile
        .readLines()
        .map { it.toInt() }
        .sum()

fun solvePart2(): Int {
    val seen = mutableSetOf<Int>()
    return inputFile
        .readLines()
        .map { it.toInt() }
        .asSequence()
        .loop()
        .scan { acc, elem -> acc + elem }
        .first { e ->
            seen.contains(e).apply {
                seen.add(e)
            }
        }
}

/**
 * https://youtrack.jetbrains.com/issue/KT-7657
 */
fun <T> Sequence<T>.scan(transform: (acc: T, elem: T) -> T): Sequence<T> = object : Sequence<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val it = this@scan.iterator()
        var last: T? = null
        var first = true

        override fun next(): T {
            last = if (first) {
                first = false
                it.next()
            } else transform(last!!, it.next())
            return last!!
        }

        override fun hasNext(): Boolean = it.hasNext()
    }
}

fun <T> Sequence<T>.loop() = kotlin.sequences.sequence {
    while (true) {
        yieldAll(this@loop)
    }
}
