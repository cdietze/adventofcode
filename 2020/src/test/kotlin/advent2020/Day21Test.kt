package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {

    @Test
    fun testPart1() {
        assertEquals(5, advent2020.day21.resultPart1(demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals("mxmxvkd,sqjhc,fvjkl", advent2020.day21.resultPart2(demoInput))
    }

    val demoInput = """mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)"""
}
