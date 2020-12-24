package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day22Test {

    @Test
    fun testPart1() {
        assertEquals(306, advent2020.day22.resultPart1(demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals(291, advent2020.day22.resultPart2(demoInput))
    }

    @Test
    fun testPart2InfRecursion() {
        val input = """Player 1:
43
19

Player 2:
2
29
14"""
        assertEquals(183, advent2020.day22.resultPart2(input))
    }

    val demoInput = """Player 1:
9
2
6
3
1

Player 2:
5
8
4
7
10"""
}
