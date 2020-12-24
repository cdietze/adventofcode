package advent2020.day22

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long {
    tailrec fun impl(deck1: List<Long>, deck2: List<Long>): List<Long> = when {
        deck1.isEmpty() -> deck2
        deck2.isEmpty() -> deck1
        else -> if (deck1.first() > deck2.first()) {
            impl(deck1.drop(1) + deck1.first() + deck2.first(), deck2.drop(1))
        } else {
            impl(deck1.drop(1), deck2.drop(1) + deck2.first() + deck1.first())
        }
    }

    val initialState = input.split("\n\n").let { parts ->
        Pair(parts[0].lines().drop(1).map { it.toLong() }, parts[1].lines().drop(1).map { it.toLong() })
    }
    return impl(initialState.first, initialState.second).let { deck ->
        deck.reversed().foldIndexed(0L) { index, acc, v -> acc + v * (index + 1) }
    }
}

fun resultPart2(input: String): Int {
    val initialState: Pair<List<Int>, List<Int>> = input.split("\n\n").let { parts ->
        Pair(parts[0].lines().drop(1).map { it.toInt() }, parts[1].lines().drop(1).map { it.toInt() })
    }

    data class GameResult(val player1Wins: Boolean, val deck1: List<Int>, val deck2: List<Int>)

    tailrec fun impl(
        decks: Pair<List<Int>, List<Int>>,
        memory: MutableSet<Pair<List<Int>, List<Int>>>
    ): GameResult {
        val deck1 = decks.first
        val deck2 = decks.second
        if (memory.contains(Pair(deck1, deck2))) return GameResult(true, deck1, deck2)
        memory.add(decks)
        return when {
            deck1.isEmpty() -> GameResult(false, deck1, deck2)
            deck2.isEmpty() -> GameResult(true, deck1, deck2)
            else -> {
                val player1Wins = if (deck1.size > deck1.first() && deck2.size > deck2.first()) {
                    @Suppress("NON_TAIL_RECURSIVE_CALL")
                    impl(
                        Pair(deck1.drop(1).take(deck1.first()), deck2.drop(1).take(deck2.first())),
                        mutableSetOf()
                    ).player1Wins
                } else {
                    deck1.first() > deck2.first()
                }
                if (player1Wins) {
                    impl(Pair(deck1.drop(1) + deck1.first() + deck2.first(), deck2.drop(1)), memory)
                } else {
                    impl(Pair(deck1.drop(1), deck2.drop(1) + deck2.first() + deck1.first()), memory)
                }
            }
        }
    }
    return impl(initialState, mutableSetOf()).let { result: GameResult ->
        listOf(result.deck1, result.deck2).sumOf { deck ->
            deck.reversed().foldIndexed<Int, Int>(0) { index, acc, v -> acc + v * (index + 1) }
        }
    }
}
