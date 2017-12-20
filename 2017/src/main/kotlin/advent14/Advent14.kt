package advent14

import advent10.knotHash
import java.util.*
import kotlin.coroutines.experimental.buildSequence
import kotlin.test.assertEquals

fun Byte.bitValue(bit: Int): Boolean {
    require(bit in 0..7)
    return ((1 shl bit) and this.toInt()) != 0
}

fun Char.hexToBits(): Byte =
        when (this) {
            'a' -> 10
            'b' -> 11
            'c' -> 12
            'd' -> 13
            'e' -> 14
            'f' -> 15
            else -> (this.toByte() - '0'.toByte()).toByte()
        }

fun String.hexToBits(): BitSet {
    val bits = BitSet()
    this.forEachIndexed { index, c ->
        val byte = c.hexToBits()
        (0..3).forEach { bit ->
            if (byte.bitValue(3 - bit)) {
                bits.set(index * 4 + bit)
            }
        }
    }
    return bits
}

fun String.toData(): BitSet {
    val result = BitSet()
    (0..127).forEach { rowIndex ->
        val hash = "${this}-$rowIndex".knotHash()
        hash.hexToBits().setBits().forEach { bit ->
            result.set(rowIndex * 128 + bit)
        }
    }
    return result
}

fun Int.neighbors(): Sequence<Int> = buildSequence {
    val i = this@neighbors
    val x = i % 128
    val y = i / 128
    if (x > 0) yield(i - 1)
    if (y > 0) yield(i - 128)
    if (x < 127) yield(i + 1)
    if (y < 127) yield(i + 128)
}

fun BitSet.region(start: Int): BitSet {
    val seen = BitSet()
    fun loop(i: Int) {
        if (this[i] && !seen[i]) {
            seen.set(i)
            i.neighbors().forEach { loop(it) }
        }
    }
    loop(start)
    return seen
}

fun BitSet.setBits(): Sequence<Int> = buildSequence {
    var i = nextSetBit(0)
    while (i >= 0) {
        yield(i)
        i = nextSetBit(i + 1)
    }
}

fun BitSet.regions(): Set<BitSet> {
    val result = mutableSetOf<BitSet>()
    this.setBits().forEach { i ->
        if (!result.any { it[i] }) {
            result.add(this.region(i))
        }
    }
    return result
}

fun main(args: Array<String>) {
    val input = "nbysizxe"
    println("Result part 1: ${input.toData().cardinality()}")
    println("Result part 2: ${input.toData().regions().size}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(0, "0".hexToBits().cardinality())
        assertEquals(1, "1".hexToBits().cardinality())
        assertEquals(3, "e".hexToBits().cardinality())
        assertEquals(4, "f".hexToBits().cardinality())
        assertEquals(BitSet().apply {
            set(3)
        }, "1".hexToBits())
        assertEquals(BitSet().apply {
            set(0)
            set(2)
        }, "a".hexToBits())
        assertEquals(8108, "flqrgnkx".toData().cardinality())
        assertEquals(1242, "flqrgnkx".toData().regions().size)
    }
}
