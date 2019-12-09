package advent2019.intcode

data class State(
    val mem: MutableList<Int>,
    val input: () -> Int,
    var pc: Int = 0,
    val output: MutableList<Int> = mutableListOf(),
    var done: Boolean = false
)

fun <T> Iterable<T>.toFun(): () -> T {
    val iter = iterator()
    return {
        check(iter.hasNext()) { "No more input available" }
        iter.next()
    }
}

fun State.run(debug: Boolean = false): State {
    var state = this
    while (!state.done) {
        if (debug) println(this)
        state = state.step()
    }
    if (debug) println("Done\n$this")
    return this
}

private fun State.step(): State {
    when (mem[pc].opcode()) {
        1 -> { // ADD
            setParam(3, getParam(1) + getParam(2))
            pc += 4
        }
        2 -> { // MUL
            setParam(3, getParam(1) * getParam(2))
            pc += 4
        }
        3 -> { // INPUT
            setParam(1, input())
            pc += 2
        }
        4 -> { // OUTPUT
            output.add(getParam(1))
            pc += 2
        }
        5 -> { // jump-if-true
            if (getParam(1) != 0) {
                pc = getParam(2)
            } else {
                pc += 3
            }
        }
        6 -> { // jump-if-false
            if (getParam(1) == 0) {
                pc = getParam(2)
            } else {
                pc += 3
            }
        }
        7 -> { // less-than
            setParam(3, if (getParam(1) < getParam(2)) 1 else 0)
            pc += 4
        }
        8 -> { // equals
            setParam(3, if (getParam(1) == getParam(2)) 1 else 0)
            pc += 4
        }
        99 -> {
            done = true
        }
        else -> throw RuntimeException("Unknown opcode in instruction: ${mem[pc]}, pc: $pc, mem: $mem")
    }
    return this
}

private fun State.setParam(index: Int, value: Int): State = apply {
    mem[mem[pc + index]] = value
}

private fun State.getParam(index: Int): Int = when (val mode = mem[pc].parameterMode(index)) {
    0 -> mem[mem[pc + index]]
    1 -> mem[pc + index]
    else -> throw RuntimeException("Unknown parameter mode: $mode, pc: $pc, mem: $mem")
}

private fun Int.opcode(): Int = this % 100
private fun Int.parameterMode(paramIndex: Int): Int {
    var mode = this
    repeat(paramIndex + 1) { mode /= 10 }
    return mode % 10
}