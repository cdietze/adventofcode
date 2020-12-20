package advent2020

import advent2020.day19.isValid
import advent2020.day19.parserMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day19Test {
    val demoRules = """0: 4 1 5
1: 2 3 | 3 2
2: 4 4 | 5 5
3: 4 5 | 5 4
4: "a"
5: "b""""
    val demoInput = """$demoRules

ababbb
bababa
abbbab
aaabbb
aaaabbb"""

    @Test
    fun testPart1() {
        val parserMap = parserMap(demoRules)
        assertTrue(parserMap.isValid("ababbb"))
    }

    @Test
    fun testPart1b() {
        assertEquals(2, advent2020.day19.resultPart1(demoInput))
    }
}
