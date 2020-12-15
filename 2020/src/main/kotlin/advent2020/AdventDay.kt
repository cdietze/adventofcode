package advent2020

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

interface AdventDay {
    fun resultPart1(): Any = "Part 1 not implemented"
    fun resultPart2(): Any = "Part 2 not implemented"

    @OptIn(ExperimentalTime::class)
    fun run() {
        println("Running ${this.javaClass.name}")
        println("Result part 1: ${measureTimedValue { resultPart1() }.run { "$value in $duration" }}")
        println("Result part 2: ${measureTimedValue { resultPart2() }.run { "$value in $duration" }}")
        println()
    }
}
