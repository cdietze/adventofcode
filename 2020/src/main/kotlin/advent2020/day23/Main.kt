package advent2020.day23

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText = "538914762"

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): String {
    data class Cup(val value: Int, var next: Cup?) {
        override fun toString(): String = "Cup(value=$value,next=${next?.value}"

        fun toChainString(): String {
            val sb = StringBuilder()
            var c: Cup? = this
            while (c != null && c.next != this) {
                sb.append(c.value)
                c = c.next
            }
            c?.let { sb.append(it.value) }
            return sb.toString()
        }
    }

    fun String.parseCups(): Cup {
        val cups = this.map { c -> Cup((c - '0'), null) }
        cups.zipWithNext().forEach { (a, b) -> a.next = b }
        cups.last().next = cups.first()
        return cups.first()
    }

    fun Cup.move(): Cup {
        val grabbed = next
        val lastGrabbed = next!!.next!!.next!!
        next = lastGrabbed.next
        tailrec fun findDest(start: Cup, current: Cup, v: Int): Cup {
            if (v < 1) return findDest(start, start.next!!, 9)
            if (current.value == v) return current
            if (current == start) return findDest(start, start.next!!, v - 1)
            return findDest(start, current.next!!, v)
        }

        val dest = findDest(this, this.next!!, value - 1)
        lastGrabbed.next = dest.next
        dest.next = grabbed
        return this.next!!
    }

    val initial = input.parseCups()
    val result = (1..100).fold(initial) { acc, i ->
        acc.move().also {
            //      println("Move $i, Cups: ${it.toChainString()}")
        }
    }

    tailrec fun Cup.find(v: Int): Cup = if (value == v) this else next!!.find(v)
    val one = result.find(1)
    return one.toChainString().drop(1)
}

fun resultPart2(input: String, cupCount: Int = 1_000_000, moveCount: Int = 10_000_000): Long {
    // An array where the index is the cups value and the content is the value of the next cup
    val cups = IntArray(cupCount) { i: Int -> (i + 1) % cupCount }
    val init = input.map { it - '1' }
    cups[cupCount - 1] = init.first()
    (0 until init.size - 1).forEach { i ->
        cups[init[i]] = init[i + 1]
    }
    cups[init.last()] = if (init.size == cupCount) init.first() else init.size
    var currentVal = init.first()

    fun move() {
        val grab1 = cups[currentVal]
        val grab2 = cups[grab1]
        val grab3 = cups[grab2]
        tailrec fun findDest(d: Int): Int = when {
            d < 0 -> findDest(cupCount - 1)
            d == grab1 || d == grab2 || d == grab3 -> findDest(d - 1)
            else -> d
        }

        val dest = findDest(currentVal - 1)
        cups[currentVal] = cups[grab3]
        currentVal = cups[grab3]
        cups[grab3] = cups[dest]
        cups[dest] = grab1
    }
    repeat(moveCount) { move() }
    return (cups[0] + 1L) * (cups[cups[0]] + 1L)
}
