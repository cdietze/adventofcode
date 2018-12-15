package advent2018.day09

import java.util.*

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
//    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    var s = GameState(
        playerCount = 463,
        lastMarble = 71787
    )
    while (!s.done) {
        s = s.move()
    }
    return s.scores.values.max()!!
}

fun solvePart2(): Int {
    var s = GameState(
        playerCount = 463,
        lastMarble = 71787 * 100
    )
    while (!s.done) {
        s = s.move()
        if (s.nextMarble % 1000 == 0) println("marble: ${s.nextMarble} / ${s.lastMarble}")
    }
    return s.scores.values.max()!!
}


data class GameState(
    val currentPlayer: Int = -1,
    val playerCount: Int,
    val currentIndex: Int = 0,
    val scores: MutableMap<Int, Int> = mutableMapOf(),
    val nextMarble: Int = 1,
    val lastMarble: Int,
    val marbles: LinkedList<Int> = LinkedList(listOf(0))
)

val GameState.done: Boolean get() = nextMarble > lastMarble

fun GameState.move(): GameState {
    if (done) return this
    val thisPlayer = (currentPlayer + 1) % playerCount
    val thisMarble = nextMarble
    if (thisMarble > 0 && thisMarble % 23 == 0) {
        val removeIndex = (currentIndex - 7 + marbles.size) % marbles.size
        scores[thisPlayer] = scores.getOrDefault(thisPlayer, 0) + thisMarble + marbles[removeIndex]
        marbles.removeAt(removeIndex)
        return this.copy(
            currentPlayer = thisPlayer,
            currentIndex = removeIndex,
            nextMarble = nextMarble + 1
        )
    } else {
        val insertIndex = (currentIndex + 1) % (marbles.size) + 1
        marbles.add(insertIndex, thisMarble)
        return this.copy(
            currentPlayer = thisPlayer,
            currentIndex = insertIndex,
            nextMarble = nextMarble + 1
        )
    }
}