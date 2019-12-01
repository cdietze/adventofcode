package advent2019.day01

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): String =
    inputText.lines().map { (it.toInt() / 3) - 2 }.sum().toString()

fun solvePart2(): String =
    inputText.lines().map { it.toInt().fuelRecursive() }.sum().toString()

fun Int.fuelRecursive(): Int {
    val fuel = (this / 3) - 2
    return if (fuel <= 0) 0 else (fuel + fuel.fuelRecursive())
}
