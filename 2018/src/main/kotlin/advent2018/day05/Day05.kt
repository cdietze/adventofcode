package advent2018.day05

import java.io.File
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.max

val inputFile = File("src/main/kotlin/advent2018/day05/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
//    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val polarityDiff = ('A' - 'a').absoluteValue
    val list = LinkedList<Char>(inputFile.readText().toList())
    tailrec fun loop(startIndex: Int = 0) {
        for (i in startIndex until list.size - 1) {
            if ((list[i] - list[i + 1]).absoluteValue == polarityDiff) {
                list.removeAt(i)
                list.removeAt(i)
                return loop(max(0, i - 1))
            }
        }
        if (startIndex == 0) return else loop()
    }
    loop()
    return list.size
}
