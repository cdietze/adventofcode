package advent2020

import advent2020.day05.seatId
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {

    @Test
    fun testPart1() {
        assertEquals(567, "BFFFBBFRRR".seatId)
        assertEquals(119, "FFFBBBFRRR".seatId)
        assertEquals(820, "BBFFBBFRLL".seatId)
    }
}
