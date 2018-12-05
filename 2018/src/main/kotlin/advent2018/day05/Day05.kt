package advent2018.day05

import java.io.File
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.max

val inputFile = File("src/main/kotlin/advent2018/day05/input.txt")

val polarityDiff = ('A' - 'a').absoluteValue

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    return LinkedList(inputFile.readText().toList()).react().size
}

fun solvePart2(): Int {
    val baseList = LinkedList<Char>(inputFile.readText().toList()).react()
    return ('A'..'Z').map { c ->
        Pair(c, LinkedList(baseList).apply {
            removeAll(listOf(c, c + polarityDiff))
        }.react().size)
    }.minBy { it.second }!!.second
}

fun LinkedList<Char>.react(): LinkedList<Char> {
    tailrec fun loop(startIndex: Int = 0): LinkedList<Char> {
        for (i in startIndex until this.size - 1) {
            if ((this[i] - this[i + 1]).absoluteValue == polarityDiff) {
                this.removeAt(i)
                this.removeAt(i)
                return loop(max(0, i - 1))
            }
        }
        return if (startIndex == 0) this else loop()
    }
    return loop()
}