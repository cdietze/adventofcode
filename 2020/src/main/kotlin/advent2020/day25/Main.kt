package advent2020.day25

import advent2020.AdventDay

object Main : AdventDay {
    private val cardPub = 17115212L
    private val doorPub = 3667832L

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(cardPub, doorPub)
}

fun resultPart1(cardPub: Long, doorPub: Long): Long {
    fun Long.step(subject: Long): Long = (this * subject).rem(20201227L)
    fun Long.findLoopSize(): Long {
        tailrec fun impl(n: Long, value: Long): Long = if (value == this) n else impl(n + 1, value.step(7))
        return impl(1, 7)
    }

    val cardLoopSize = cardPub.findLoopSize()
    return (1..cardLoopSize).fold(1L) { acc, _ -> acc.step(doorPub) }
}
