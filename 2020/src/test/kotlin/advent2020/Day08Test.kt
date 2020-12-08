package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {

    val demoInput = """nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6"""

    @Test
    fun testPart1() {
        assertEquals(5, advent2020.day08.resultPart1(demoInput))
    }
}
