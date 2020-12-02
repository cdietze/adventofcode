package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    @Test
    fun testPart1() {
        assertEquals(
            2, advent2020.day02.resultPart1(
                "1-3 a: abcde\n1-3 b: cdefg\n2-9 c: ccccccccc"
            )
        )
    }
    @Test
    fun testPart2() {
        assertEquals(
            1, advent2020.day02.resultPart2(
                "1-3 a: abcde\n1-3 b: cdefg\n2-9 c: ccccccccc"
            )
        )
    }
}
