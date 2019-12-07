package advent2019.day05

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
}

fun solvePart1(): Int {
    val mem = inputText.split(",").map { it.toInt() }.toMutableList()
    return State(mem = mem).run().output.first { it != 0 }
}

data class State(
    var pc: Int = 0,
    val input: Int = 1,
    val output: MutableList<Int> = mutableListOf(),
    var done: Boolean = false,
    val mem: MutableList<Int>
)

fun State.run(): State {
    var state = this
    while (!state.done) {
        // println(this)
        state = state.step()
    }
    // println("DONE\n$this")
    return this
}

fun State.step(): State = when (mem[pc].opcode()) {
    1 -> { // ADD
        mem[mem[pc + 3]] = getParam(0) + getParam(1); pc += 4; this
    }
    2 -> { // MUL
        mem[mem[pc + 3]] = getParam(0) * getParam(1); pc += 4; this
    }
    3 -> { // INPUT
        mem[mem[pc + 1]] = input; pc += 2; this
    }
    4 -> { // OUTPUT
        output.add(getParam(0)); pc += 2; this
    }
    99 -> {
        done = true; this
    }
    else -> throw RuntimeException("Unknown opcode: ${mem[pc]}, pc: $pc, mem: $mem")
}

fun State.getParam(index: Int): Int = when (val mode = mem[pc].parameterMode(index)) {
    0 -> mem[mem[pc + index + 1]]
    1 -> mem[pc + index + 1]
    else -> throw RuntimeException("Unknown parameter mode: $mode, pc: $pc, mem: $mem")
}

fun Int.opcode(): Int = this % 100
fun Int.parameterMode(paramIndex: Int): Int {
    var mode = this
    repeat(paramIndex + 2) { mode /= 10 }
    return mode % 10
}