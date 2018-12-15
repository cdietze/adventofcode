package advent2018.day09

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Long {
    var s = GameState(
        playerCount = 463,
        lastMarbleValue = 71787
    )
    while (!s.done) {
        s = s.move()
    }
    return s.scores.values.max()!!
}

fun solvePart2(): Long {
    var s = GameState(
        playerCount = 463,
        lastMarbleValue = 71787 * 100
    )
    while (!s.done) {
        s = s.move()
    }
    return s.scores.values.max()!!
}

data class GameState(
    var currentPlayer: Int = -1,
    val playerCount: Int,
    var marble: Marble = Marble(0, null, null).apply {
        _prev = this
        _next = this
    },
    val scores: MutableMap<Int, Long> = mutableMapOf(),
    var nextMarbleValue: Int = 1,
    val lastMarbleValue: Int
)

data class Marble(val value: Int, var _prev: Marble?, var _next: Marble?) {
    var prev: Marble
        get() = _prev!!
        set(value) {
            _prev = value
        }
    var next: Marble
        get() = _next!!
        set(value) {
            _next = value
        }

    override fun toString(): String {
        return "Marble(value=$value, prev=${_prev?.value}, next=${_next?.value})"
    }
}

fun Marble.remove(): Marble {
    prev.next = next
    next.prev = prev
    return this
}

val GameState.done: Boolean get() = nextMarbleValue > lastMarbleValue

fun GameState.move(): GameState {
    if (done) return this
    val thisPlayer = (currentPlayer + 1) % playerCount
    val thisMarble = nextMarbleValue
    if (thisMarble > 0 && thisMarble % 23 == 0) {
        val removeMarble = marble.prev.prev.prev.prev.prev.prev.prev
        scores[thisPlayer] = scores.getOrDefault(thisPlayer, 0) + thisMarble + removeMarble.value
        removeMarble.remove()
        marble = removeMarble.next
        currentPlayer = thisPlayer
        nextMarbleValue += 1
        return this
    } else {
        val m1 = marble.next
        val m2 = m1.next
        marble = Marble(nextMarbleValue, m1, m2)
        m1.next = marble
        m2.prev = marble
        currentPlayer = thisPlayer
        nextMarbleValue += 1
        return this
    }
}
