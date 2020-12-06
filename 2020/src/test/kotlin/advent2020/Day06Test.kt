package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {

    val demoInput = "abc\n\na\nb\nc\n\nab\nac\n\na\na\na\na\n\nb"

    @Test
    fun testPart1() {
        assertEquals(11, advent2020.day06.resultPart1(demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals(6, advent2020.day06.resultPart2(demoInput))
    }
}
