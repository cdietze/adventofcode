package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day03Test {
    val demoInput = "..##.......\n" +
            "#...#...#..\n" +
            ".#....#..#.\n" +
            "..#.#...#.#\n" +
            ".#...##..#.\n" +
            "..#.##.....\n" +
            ".#.#.#....#\n" +
            ".#........#\n" +
            "#.##...#...\n" +
            "#...##....#\n" +
            ".#..#...#.#"

    @Test
    fun testPart1() {
        assertEquals(7, advent2020.day03.resultPart1(demoInput))
    }

    @Test
    fun testPart2() {
        assertEquals(336, advent2020.day03.resultPart2(demoInput))
    }
}
