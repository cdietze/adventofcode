package advent05

import java.io.File
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent05/input.txt").readLines().map { it.toInt() }
    println("Result part1: ${input.stepsUntilExit()}")
    println("Result part2: ${input.stepsUntilExitB()}")
}

fun List<Int>.stepsUntilExit(): Int {
    val mem = this.toMutableList()
    fun process(step: Int): Int? {
        val oldValue = mem[step]
        mem[step] = oldValue + 1
        val newStep = step + oldValue
        return if (newStep in 0 until this.size) newStep else null
    }
    return stepsUntilExit(::process)
}

fun List<Int>.stepsUntilExitB(): Int {
    val mem = this.toMutableList()
    fun process(step: Int): Int? {
        val oldValue = mem[step]
        mem[step] = oldValue + if (oldValue >= 3) -1 else 1
        val newStep = step + oldValue
        return if (newStep in 0 until this.size) newStep else null
    }
    return stepsUntilExit(::process)
}

fun stepsUntilExit(process: (step: Int) -> Int?): Int {
    var step: Int? = 0
    var count = 0
    while (step != null) {
        count += 1
        step = process(step)
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
