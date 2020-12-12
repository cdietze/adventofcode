package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    val demoInput = """F10
N3
F7
R90
F11"""

    @Test
    fun testPart1() {
        assertEquals(25, advent2020.day12.resultPart1(demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals(286, advent2020.day12.resultPart2(demoInput))
    }
}
