package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day09Test {

    val demoInput = """35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576"""

    @Test
    fun testPart1() {
        assertEquals(127L, advent2020.day09.resultPart1(demoInput, 5))
    }

    @Test
    fun testPart2() {
        assertEquals(62L, advent2020.day09.resultPart2(demoInput, 127L))
    }
}
