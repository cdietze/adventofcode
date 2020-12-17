package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    val demoInput = """.#.
..#
###"""

    @Test
    fun testPart1() {
        assertEquals(112, advent2020.day17.result(3, demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals(848, advent2020.day17.result(4, demoInput))
    }
}
