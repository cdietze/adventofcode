package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    val demoInput = """L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL"""

    @Test
    fun testPart1() {
        assertEquals(37, advent2020.day11.resultPart1(demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals(26, advent2020.day11.resultPart2(demoInput))
    }
}
