package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Test {

    @Test
    fun testPart1() {
        assertEquals(436, advent2020.day15.resultPart1("0,3,6"))
        assertEquals(1, advent2020.day15.resultPart1("1,3,2"))
        assertEquals(10, advent2020.day15.resultPart1("2,1,3"))
        assertEquals(27, advent2020.day15.resultPart1("1,2,3"))
    }

    @Test
    fun testPart2Simple() {
        assertEquals(436, advent2020.day15.resultPart2("0,3,6", 2020))
        assertEquals(1, advent2020.day15.resultPart2("1,3,2", 2020))
        assertEquals(10, advent2020.day15.resultPart2("2,1,3", 2020))
        assertEquals(27, advent2020.day15.resultPart2("1,2,3", 2020))
    }

    @Test
    fun testPart2() {
        assertEquals(175594, advent2020.day15.resultPart2("0,3,6"))
        assertEquals(2578, advent2020.day15.resultPart2("1,3,2"))
        assertEquals(3544142, advent2020.day15.resultPart2("2,1,3"))
        assertEquals(261214, advent2020.day15.resultPart2("1,2,3"))
    }
}
