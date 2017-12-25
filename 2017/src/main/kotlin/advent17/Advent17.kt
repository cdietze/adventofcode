package advent17

data class State(val pos: Int = 0, val mem: List<Int> = listOf(0))

fun State.step(input: Int): State {
    val insertPos = (pos + input) % this.mem.size + 1
    return State(insertPos, this.mem.toMutableList().apply { add(insertPos, this@step.mem.size) })
}

fun calcVal1(input: Int): Int {
    var val1 = 0
    var pos = 0
    (1..(50 * 1000 * 1000)).forEach {
        pos = (pos + input) % it + 1
        if (pos == 1) {
            val1 = it
        }
    }
    return val1
}

fun main(args: Array<String>) {
    val input = 301
    var state = State()
    (0 until 2017).forEach {
        state = state.step(input)
    }
    println("Result part 1: ${state.mem[(state.pos + 1) % state.mem.size]}")
    println("Result part 2: ${calcVal1(input)}")
}
