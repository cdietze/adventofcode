package advent05

import java.io.File
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent05/input.txt").readLines().map { it.toInt() }
    println("Result part1: ${input.stepsUntilExit()}")
}

fun List<Int>.stepsUntilExit(): Int {
    fun MutableList<Int>.process(step: Int): Int {
        val oldValue = this[step]
        this[step] = oldValue + 1
        return step + oldValue
    }

    val mem = this.toMutableList()
    var step = 0
    var count = 0
    while (step >= 0 && step < mem.size) {
        count += 1
        step = mem.process(step)
    }
    return count
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        val input = listOf<Int>(0, 3, 0, 1, -3)
        assertEquals(input.stepsUntilExit(), 5)
    }
}
