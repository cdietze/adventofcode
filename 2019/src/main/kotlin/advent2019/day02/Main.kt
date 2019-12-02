package advent2019.day02

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val mem = inputText.split(",").map { it.toInt() }.toMutableList()
    mem[1] = 12
    mem[2] = 2
    return mem.run()
}

fun MutableList<Int>.run(): Int {
    val mem = this
    var pc = 0
    fun step(): Boolean = when (mem[pc]) {
        1 -> {
            mem[mem[pc + 3]] = mem[mem[pc + 1]] + mem[mem[pc + 2]]; pc += 4; true
        }
        2 -> {
            mem[mem[pc + 3]] = mem[mem[pc + 1]] * mem[mem[pc + 2]]; pc += 4; true
        }
        99 -> false
        else -> throw RuntimeException("Unknown opcode ${mem[pc]}, pc: $pc, mem: $mem")
    }
    do {
        // println("pc:$pc, mem:$mem")
    } while (step())
    // println("DONE\npc:$pc, mem:$mem")
    return mem[0]
}

fun solvePart2(): Int {
    val initialMem = inputText.split(",").map { it.toInt() }
    return (0..99).asSequence().flatMap { v0 ->
        (0..99).asSequence().map { v1 ->
            Pair(Pair(v0, v1), initialMem.toMutableList().let {
                it[1] = v0
                it[2] = v1
                it.run()
            })
        }
    }.first { it.second == 19690720 }
        .let { it.first.first * 100 + it.first.second }
}
