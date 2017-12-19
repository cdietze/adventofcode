package advent13

import parsek.*
import parsek.Parsers.string
import java.io.File
import kotlin.test.assertEquals

data class Scanner(val depth: Int, val range: Int)

val numberParser: Parser<Int> = Parsers.regex("^-?\\d+".toRegex()).map { it.value.toInt() }

val scannerParser: Parser<Scanner> =
        (numberParser.ignore(string(": ")) * numberParser)
                .map { Scanner(it.first, it.second) }

fun severity(scanners: List<Scanner>, delay: Int = 0): Int {
    val maxDepth = scanners.maxBy { it.depth }!!.depth
    tailrec fun loop(t: Int = 0, accSeverity: Int = 0): Int {
        val packetDepth = t - delay
        if (packetDepth > maxDepth) return accSeverity
        val score = scanners.find { it.depth == packetDepth }?.let { scanner ->
            val scannerPos = t % (scanner.range * 2 - 2)
            if (scannerPos == 0) scanner.range * scanner.depth else 0
        } ?: 0
        return loop(t + 1, accSeverity + score)
    }
    return loop(0, 0)
}

fun collides(scanners: List<Scanner>, delay: Int = 0): Boolean {
    return scanners.any { scanner ->
        val t = scanner.depth + delay
        val scannerPos = t % (scanner.range * 2 - 2)
        scannerPos == 0
    }
}

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent13/input.txt").readLines().map { scannerParser.parseFully(it).get().value }
    println("Result part 1: ${severity(input)}")
    println("Result part 2: ${(0..Int.MAX_VALUE).first { !collides(input, it) }}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        val testData = listOf(
                Scanner(0, 3),
                Scanner(1, 2),
                Scanner(4, 4),
                Scanner(6, 4)
        )
        assertEquals(10, (0..100).first { !collides(testData, it) })
    }
}
