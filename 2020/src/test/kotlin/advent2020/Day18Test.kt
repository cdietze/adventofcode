package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun testPart1() {
        assertEquals(71, advent2020.day18.resultPart1("1 + 2 * 3 + 4 * 5 + 6"))
        assertEquals(51, advent2020.day18.resultPart1("1 + (2 * 3) + (4 * (5 + 6))"))
        assertEquals(26, advent2020.day18.resultPart1("2 * 3 + (4 * 5)"))
        assertEquals(13632, advent2020.day18.resultPart1("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }

    @Test
    fun testPart2() {
        assertEquals(51, advent2020.day18.resultPart2("1 + (2 * 3) + (4 * (5 + 6))"))
        assertEquals(46, advent2020.day18.resultPart2("2 * 3 + (4 * 5)"))
        assertEquals(23340, advent2020.day18.resultPart2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }
}
