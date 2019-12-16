package advent2019.intcode

class State(
    val mem: Memory,
    var pc: Long = 0,
    var relativeBaseOffset: Long = 0,
    val read: suspend () -> Long,
    val write: suspend (Long) -> Unit
)

fun List<Long>.toMem(): Memory =
    Memory(this.mapIndexed { index, value -> Pair(index.toLong(), value) }.toMap().toMutableMap())

class Memory(private val store: MutableMap<Long, Long>) {
    operator fun get(index: Long): Long = store.getOrDefault(index, 0L)
    operator fun set(index: Long, value: Long): Unit {
        store[index] = value
    }
}

suspend fun State.run(): Unit {
    while (step()) {
        // noop
    }
}

private suspend fun State.step(): Boolean {
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
            val input = read()
            setParam(1, input)
            pc += 2
        }
        4 -> { // OUTPUT
            val out = getParam(1)
            pc += 2
            write(out)
        }
        5 -> { // jump-if-true
            if (getParam(1) != 0L) {
                pc = getParam(2)
            } else {
                pc += 3
            }
        }
        6 -> { // jump-if-false
            if (getParam(1) == 0L) {
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
        9 -> { // adjust relative base offset
            val value = getParam(1)
            pc += 2
            relativeBaseOffset += value.toInt()
        }
        99 -> {
            return false
        }
        else -> throw RuntimeException("Unknown opcode in instruction: ${mem[pc]}, pc: $pc, mem: $mem")
    }
    return true
}

private fun State.setParam(index: Int, value: Long): State = apply {
    mem[parameterAddress(index)] = value
}

private fun State.getParam(index: Int): Long = mem[parameterAddress(index)]

private fun Long.opcode(): Int = (this % 100).toInt()
private fun Long.parameterMode(paramIndex: Int): Int {
    var mode = this
    repeat(paramIndex + 1) { mode /= 10 }
    return (mode % 10).toInt()
}

private fun State.parameterAddress(index: Int): Long = when (val mode = mem[pc].parameterMode(index)) {
    // Position mode
    0 -> mem[pc + index]
    // Immediate mode
    1 -> pc + index
    // Relative mode
    2 -> mem[pc + index] + relativeBaseOffset
    else -> throw RuntimeException("Unknown parameter mode: $mode, pc: $pc, mem: $mem")
}