package advent2016

import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    @Test
    fun testPart1() {
        assertEquals(5, advent2016.day01.resultPart1("R2, L3"))
    }

    @Test
    fun testPart2() {
        assertEquals(4, advent2016.day01.resultPart2("R8, R4, R4, R8"))
    }
}
