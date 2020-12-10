package advent2020

val allDays = listOf(
    advent2020.day01.Main,
    advent2020.day02.Main,
    advent2020.day03.Main,
    advent2020.day04.Main,
    advent2020.day05.Main,
    advent2020.day06.Main,
    advent2020.day07.Main,
    advent2020.day08.Main,
    advent2020.day09.Main,
    advent2020.day10.Main
)

fun main() {
    allDays.forEach { it.run() }
}
