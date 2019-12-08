package advent2019.day08

val inputText = object {}.javaClass.getResource("input.txt").readText()

const val imageWidth = 25
const val imageHeight = 6

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val layers = inputText.chunked(imageHeight * imageWidth)
    val l = layers.minBy { it.count { it == '0' } }!!
    return l.count { it == '1' } * l.count { it == '2' }
}

fun solvePart2(): String {
    val layers = inputText.chunked(imageHeight * imageWidth)
    val result: String = layers.reduce { acc, next ->
        acc.zip(next).map {
            when (it.first) {
                '2' -> it.second
                else -> it.first
            }
        }.joinToString(separator = "")
    }
    return result.toImage()
}

fun String.toImage(): String = map {
    when (it) {
        '0' -> ' '
        '1' -> 'X'
        '2' -> '.'
        else -> '?'
    }
}.joinToString(separator = "").chunked(imageWidth).joinToString(separator = "\n", prefix = "\n")