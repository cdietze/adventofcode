package advent23

import java.io.File
import kotlin.collections.set

typealias Instruction = List<String>

data class State(val pc: Int = 0, val mem: MutableMap<String, Int> = mutableMapOf(), val mulCounter: Int = 0)

fun State.step(instr: Instruction): State {
    return when (instr[0]) {
        "set" -> copy(pc = pc + 1).apply { mem[instr[1]] = getValue(instr[2], mem) }
        "sub" -> copy(pc = pc + 1).apply { mem[instr[1]] = (mem[instr[1]] ?: 0) - getValue(instr[2], mem) }
        "mul" -> copy(pc = pc + 1, mulCounter = mulCounter + 1).apply { mem[instr[1]] = (mem[instr[1]] ?: 0) * getValue(instr[2], mem) }
        "jnz" -> copy(pc = if (getValue(instr[1], mem) == 0) pc + 1 else pc + getValue(instr[2], mem))
        else -> error("unknown instruction: $instr")
    }
}

tailrec fun State.run(instructions: List<Instruction>): State {
    val i = instructions.getOrNull(pc)
    return if (i == null) this else {
        step(i).run(instructions)
    }
}

fun getValue(s: String, mem: Map<String, Int>) = s.toIntOrNull() ?: mem.getOrDefault(s, 0)

fun solve2(): Int =
        ((99 * 100 + 100000)..(99 * 100 + 100000 + 17000) step 17)
                .sumBy { b -> if ((2 until b).any { (b % it) == 0 }) 1 else 0 }

fun main(args: Array<String>) {
    val instructions = File("src/main/kotlin/advent23/input.txt").readLines().map { it.split(" ") }
    val result = State().run(instructions)
    println("Result part 1: ${result.mulCounter}")
    println("Result part 2: ${solve2()}")
}
