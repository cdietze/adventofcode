package advent2019.day01

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
}

fun solvePart1(): String =
    inputText.lines().map { (it.toInt() / 3) - 2 }.sum().toString()
