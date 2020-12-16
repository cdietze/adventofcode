package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {
    val demoInput = """class: 1-3 or 5-7
row: 6-11 or 33-44
seat: 13-40 or 45-50

your ticket:
7,1,14

nearby tickets:
7,3,47
40,4,50
55,2,20
38,6,12"""

    @Test
    fun testPart1() {
        assertEquals(71, advent2020.day16.resultPart1(demoInput))
    }
}
