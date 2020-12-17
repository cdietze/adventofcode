package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {
    val demoInput = """.#.
..#
###"""

    @Test
    fun testPart1() {
        assertEquals(112, advent2020.day17.resultPart1(demoInput))
    }
}
