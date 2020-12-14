package advent2020.day14

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

val maskPattern = Regex("^mask = (\\w+).*")
val memPattern = Regex("^mem\\D*(\\d+)\\D*(\\d+).*")

fun resultPart1(input: String): Long {
    var mask = ""
    val mem = mutableMapOf<Long, Long>()

    input.lines().map { line ->
        maskPattern.matchEntire(line)?.let {
            mask = it.groupValues[1]
        } ?: memPattern.matchEntire(line)?.let {
            val address = it.groupValues[1].toLong()
            val value = it.groupValues[2].toLong().toString(2).padStart(36, '0')
            val maskedValue = List(36) { i ->
                when (mask[i]) {
                    '0' -> '0'
                    '1' -> '1'
                    else -> value[i]
                }
            }
            mem[address] = maskedValue.joinToString(separator = "").toLong(2)
        } ?: error("Failed to parse $line")
    }
    return mem.values.sum()
}

fun resultPart2(input: String): Long {
    fun String.floatingMasks(value: String): Sequence<String> {
        val mask = this
        return sequence {
            suspend fun SequenceScope<String>.impl(s: String) {
                val index = s.length
                if (s.length >= 36) yield(s)
                else {
                    when (mask[index]) {
                        '0' -> impl(s + value[index])
                        '1' -> impl(s + '1')
                        else -> {
                            impl(s + '0')
                            impl(s + '1')
                        }
                    }
                }
            }
            impl("")
        }
    }

    var mask = ""
    val mem = mutableMapOf<Long, Long>()

    input.lines().map { line ->
        maskPattern.matchEntire(line)?.let {
            mask = it.groupValues[1]
        } ?: memPattern.matchEntire(line)?.let {
            val address = it.groupValues[1].toLong().toString(2).padStart(36, '0')
            val value = it.groupValues[2].toLong()
            mask.floatingMasks(address).forEach { m ->
                mem[m.toLong(2)] = value
            }
        } ?: error("Failed to parse $line")
    }
    return mem.values.sum()
}
