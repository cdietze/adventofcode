package advent2020.day08

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
//    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val program: List<List<String>> = input.lines().map { it.split(" ") }
    var acc = 0
    var pc = 0
    val visited = mutableSetOf<Int>()
    while (pc !in visited) {
        visited += pc
        val inst = program[pc]
        when (inst[0]) {
            "acc" -> {
                pc += 1; acc += inst[1].toInt()
            }
            "jmp" -> pc += inst[1].toInt()
            "nop" -> pc += 1
            else -> error("Unknown instruction: $inst")
        }
    }
    return acc
}
