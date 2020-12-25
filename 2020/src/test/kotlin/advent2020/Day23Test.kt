package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {

    @Test
    fun testPart1() {
        assertEquals("67384529", advent2020.day23.resultPart1("389125467"))
    }

    @Test
    fun testPart2() {
        assertEquals(149245887792L, advent2020.day23.resultPart2("389125467"))
    }
}
