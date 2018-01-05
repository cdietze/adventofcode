package advent21

import java.io.File

typealias Pattern = List<String>

fun String.parseAsPattern(): Pattern = split("/").toList()

fun Pattern.flipDiagonal(): Pattern = this[0].mapIndexed({ i, c -> this.map { it[i] }.joinToString("") })
fun Pattern.flipHorizontal(): Pattern = this.map { it.reversed() }
fun Pattern.flipVertical(): Pattern = (this.size - 1 downTo 0).map { this[it] }

fun Pattern.variations(): List<Pattern> = listOf(this)
        .flatMap { listOf(it, it.flipDiagonal()) }
        .flatMap { listOf(it, it.flipHorizontal()) }
        .flatMap { listOf(it, it.flipVertical()) }

fun Pattern.patternToString(): String = this.joinToString("\n", "\n")

fun Pattern.step(rules: List<Pair<Pattern, Pattern>>): Pattern {
    val splitSize = if (this.size % 2 == 0) 2 else 3

    fun stepRow(row: Pattern): Pattern = (0 until row[0].length / splitSize).map { chunkX ->
        val pattern: Pattern = row.map { it.substring(chunkX * splitSize, chunkX * splitSize + splitSize) }
        rules.first { it.first == pattern }.second
    }.joinRow()

    return this.chunked(splitSize).flatMap(::stepRow)
}

fun List<Pattern>.joinRow(): Pattern =
        this[0].mapIndexed { index, _ -> this.joinToString("") { it[index] } }

fun Pattern.countPixels(): Int = sumBy { it.sumBy { if (it == '#') 1 else 0 } }

fun main(args: Array<String>) {
    val inputRules = File("src/main/kotlin/advent21/input.txt").readLines().map {
        val split = it.split(" => ")
        Pair(split[0].parseAsPattern(), split[1].parseAsPattern())
    }
    val allRules = inputRules.flatMap { t -> t.first.variations().map { t.copy(first = it) } }
    val start: Pattern = listOf(".#.", "..#", "###")
    val result1 = (1..5).fold(start, { acc, _ -> acc.step(allRules) })
    println("Result part 1: ${result1.countPixels()}")
    val result2 = (1..18).fold(start, { acc, _ -> acc.step(allRules) })
    println("Result part 2: ${result2.countPixels()}")
}
