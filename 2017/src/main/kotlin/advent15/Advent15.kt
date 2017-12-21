package advent15

import kotlin.coroutines.experimental.buildSequence

fun Long.next(factor: Long): Long = (this * factor) % 2147483647

fun seq(seed: Long, factor: Long): Sequence<Long> = buildSequence {
    var v = seed
    while (true) {
        v = v.next(factor)
        yield(v)
    }
}

fun judge(a: Long, b: Long): Boolean = (a and 0xffff) == (b and 0xffff)

fun matches(seqA: Sequence<Long>, seqB: Sequence<Long>): Sequence<Boolean> =
        seqA.zip(seqB).map { judge(it.first, it.second) }

fun main(args: Array<String>) {
    val seedA = 289L
    val factorA = 16807L
    val seedB = 629L
    val factorB = 48271L
    println("Result part 1: ${matches(
            seq(seedA, factorA), seq(seedB, factorB))
            .take(40 * 1000 * 1000)
            .count { it }}")
    println("Result part 2: ${matches(
            seq(seedA, factorA).filter { it % 4L == 0L },
            seq(seedB, factorB).filter { it % 8L == 0L })
            .take(5 * 1000 * 1000)
            .count { it }}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
    }
}
