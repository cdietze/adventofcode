package advent2020.day08

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val program = input.lines().map { it.split(" ") }
    return Computer().run(program).acc
}

fun resultPart2(input: String): Int {
    val program = input.lines().map { it.split(" ") }
    val programCandidates: List<Program> =
        program.mapIndexedNotNull { index: Int, inst: List<String> ->
            fun patchOp(c: String): Program {
                return program.toMutableList().apply {
                    this[index] = this[index].toMutableList().apply {
                        this[0] = c
                    }
                }
            }
            when (inst[0]) {
                "nop" -> patchOp("jmp")
                "jmp" -> patchOp("nop")
                else -> null
            }
        }
    return programCandidates.map { p ->
        Computer().run(p)
    }.first { c -> c.pc >= program.size }.acc
}

typealias Instruction = List<String>
typealias Program = List<Instruction>

data class Computer(var acc: Int = 0, var pc: Int = 0, val visited: MutableSet<Int> = mutableSetOf())

fun Computer.run(p: Program): Computer {
    while (pc < p.size && pc !in visited) {
        visited += pc
        val inst = p[pc]
        when (inst[0]) {
            "acc" -> {
                pc += 1; acc += inst[1].toInt()
            }
            "jmp" -> pc += inst[1].toInt()
            "nop" -> pc += 1
            else -> error("Unknown instruction: $inst")
        }
    }
    return this
}
