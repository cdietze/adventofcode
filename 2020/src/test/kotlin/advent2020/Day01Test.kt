package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day01Test {
    @Test
    fun testPart1() {
        assertEquals(
            514579, advent2020.day01.resultPart1(
                "1721\n979\n366\n299\n675\n1456"
            )
        )
    }

    @Test
    fun testPart2() {
        assertEquals(
            241861950, advent2020.day01.resultPart2(
                "1721\n979\n366\n299\n675\n1456"
            )
        )
    }
}
