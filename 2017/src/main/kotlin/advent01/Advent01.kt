package advent01

import java.io.File
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent01/input.txt").readText()
    println("Result part1: " + calcMatchingDigitSum(input))
    println("Result part2: " + calcMatchingDigitSumPart2(input))
}

fun calcMatchingDigitSum(s: String): Long {
    return s.foldIndexed(0L, { index, acc, c ->
        val nextChar = s[(index + 1) % s.length]
        acc + if (nextChar == c) c.toString().toInt() else 0
    })
}

fun calcMatchingDigitSumPart2(s: String): Long {
    return s.foldIndexed(0L, { index, acc, c ->
        val nextChar = s[(index + s.length / 2) % s.length]
        acc + if (nextChar == c) c.toString().toInt() else 0
    })
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(3L, calcMatchingDigitSum("1122"))
        assertEquals(4, calcMatchingDigitSum("1111"))
        assertEquals(0, calcMatchingDigitSum("1234"))
        assertEquals(9L, calcMatchingDigitSum("91212129"))

        assertEquals(6L, calcMatchingDigitSumPart2("1212"))
        assertEquals(0L, calcMatchingDigitSumPart2("1221"))
        assertEquals(4L, calcMatchingDigitSumPart2("123425"))
        assertEquals(12L, calcMatchingDigitSumPart2("123123"))
        assertEquals(4L, calcMatchingDigitSumPart2("12131415"))
    }
}
