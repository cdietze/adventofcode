package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {
    val demoInput = """939
7,13,x,x,59,x,31,19"""

    @Test
    fun testPart1() {
        assertEquals(295, advent2020.day13.resultPart1(demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals(1068781L, advent2020.day13.resultPart2(demoInput))
    }

    @Test
    fun testChineseRemainder1() {
        assertEquals(47L, advent2020.day13.chineseRemainder(listOf(Pair(2, 3), Pair(3, 4), Pair(2, 5))))
    }

    @Test
    fun testChineseRemainder2() {
        assertEquals(39L, advent2020.day13.chineseRemainder(listOf(Pair(0, 3), Pair(3, 4), Pair(4, 5))))
    }

    @Test
    fun testChineseRemainder3() {
        assertEquals(23L, advent2020.day13.chineseRemainder(listOf(Pair(2, 3), Pair(3, 5), Pair(2, 7))))
    }
}
