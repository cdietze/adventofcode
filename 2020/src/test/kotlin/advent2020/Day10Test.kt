package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day10Test {
    val demoInputA = """16
10
15
5
1
11
7
19
6
12
4"""

    val demoInputB = """28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3"""

    @Test
    fun testPart1a() {
        assertEquals(35, advent2020.day10.resultPart1(demoInputA))
    }

    @Test
    fun testPart1b() {
        assertEquals(220, advent2020.day10.resultPart1(demoInputB))
    }

    @Test
    fun testPart2a() {
        assertEquals(8, advent2020.day10.resultPart2(demoInputA))
    }

    @Test
    fun testPart2b() {
        assertEquals(19208, advent2020.day10.resultPart2(demoInputB))
    }
}
