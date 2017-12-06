package advent02

import java.io.File
import kotlin.test.assertEquals

typealias Row = List<Int>
typealias Sheet = List<Row>

fun Row.minMaxDiff(): Int? = if (this.isEmpty()) null else this.max()!! - this.min()!!

fun Row.evenlyDivisionResult(): Int = this.withIndex().flatMap { a ->
    this.withIndex().map { b ->
        when {
            a.index == b.index -> null
            a.value % b.value == 0 -> a.value / b.value
            else -> null
        }
    }
}.filterNotNull().first()

fun Sheet.checkSum(): Int = this.sumBy { row -> row.minMaxDiff()!! }
fun Sheet.checkSum2(): Int = this.sumBy { row -> row.evenlyDivisionResult() }

fun main(args: Array<String>) {
    val sheet: Sheet = File("src/main/kotlin/advent02/input.txt").readLines().map { line -> line.split("\\s+".toRegex()).map { it.toInt() } }
    println("Result part1: ${sheet.checkSum()}")
    println("Result part2: ${sheet.checkSum2()}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals(18, listOf(
                listOf(5, 1, 9, 5),
                listOf(7, 5, 3),
                listOf(2, 4, 6, 8)).checkSum())

        assertEquals(4, listOf(5, 9, 2, 8).evenlyDivisionResult())
        assertEquals(3, listOf(9, 4, 7, 3).evenlyDivisionResult())
        assertEquals(2, listOf(3, 8, 6, 5).evenlyDivisionResult())
        assertEquals(9, listOf(
                listOf(5, 9, 2, 8),
                listOf(9, 4, 7, 3),
                listOf(3, 8, 6, 5)
        ).checkSum2())
    }
}
