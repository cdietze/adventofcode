package advent25

data class Machine(
        val tape: MutableSet<Int> = mutableSetOf(),
        var slot: Int = 0,
        var state: Char = 'A')

fun Machine.step(): Machine {
    when (state) {
        'A' -> when (tape.contains(slot)) {
            false -> {
                tape += slot
                slot++
                state = 'B'
            }
            true -> {
                tape -= slot
                slot--
                state = 'C'
            }
        }
        'B' -> when (tape.contains(slot)) {
            false -> {
                tape += slot
                slot--
                state = 'A'
            }
            true -> {
                tape += slot
                slot--
                state = 'D'
            }
        }
        'C' -> when (tape.contains(slot)) {
            false -> {
                tape += slot
                slot++
                state = 'D'
            }
            true -> {
                tape -= slot
                slot++
                state = 'C'
            }
        }
        'D' -> when (tape.contains(slot)) {
            false -> {
                tape -= slot
                slot--
                state = 'B'
            }
            true -> {
                tape -= slot
                slot++
                state = 'E'
            }
        }
        'E' -> when (tape.contains(slot)) {
            false -> {
                tape += slot
                slot++
                state = 'C'
            }
            true -> {
                tape += slot
                slot--
                state = 'F'
            }
        }
        'F' -> when (tape.contains(slot)) {
            false -> {
                tape += slot
                slot--
                state = 'E'
            }
            true -> {
                tape += slot
                slot++
                state = 'A'
            }
        }
        else -> error("unknown state: $state")
    }
    return this
}

fun main(args: Array<String>) {
    val result = (1..12656374).fold(Machine(), { machine, _ -> machine.step() })
    println("Result part 1: ${result.tape.size}")
}
