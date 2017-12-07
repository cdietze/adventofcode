package advent06

import java.io.File
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent06/input.txt").readText().split("\\s+".toRegex()).map { it.toInt() }
    println("Result part1: ${input.cycleCount()}")
    println("Result part2: ${input.loopSize()}")
}

data class Result(val cycles: Int, val loopSize: Int)

tailrec fun impl(mem: MutableList<Int>, count: Int, seen: MutableMap<List<Int>, Int>): Result {
    seen[mem]?.let { return Result(count, count - it) }
    seen[mem] = count
    val maxCount = mem.max()!!
    val maxIndex = mem.indexOf(maxCount)
    mem[maxIndex] = 0
    for (i in (maxIndex + 1) until (maxIndex + 1 + maxCount)) mem[i % mem.size] += 1
    return impl(mem, count + 1, seen)
}

fun List<Int>.cycleCount(): Int {
    return impl(this.toMutableList(), 0, mutableMapOf()).cycles
}

fun List<Int>.loopSize(): Int {
    return impl(this.toMutableList(), 0, mutableMapOf()).loopSize
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(5, mutableListOf(0, 2, 7, 0).cycleCount())
    }
}
