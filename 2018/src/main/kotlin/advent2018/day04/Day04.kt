package advent2018.day04

import parsek.*
import java.io.File
import java.time.LocalDateTime

val inputFile = File("src/main/kotlin/advent2018/day04/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

fun solvePart1(): Int {
    val sleepDurations = sleepDurations(records)
    val topGuard = sleepDurations.maxBy { it.value.values.sum() }!!.key
    val topMinute = sleepDurations[topGuard]!!.maxBy { it.value }!!.key
    return topGuard * topMinute
}

fun solvePart2(): Int {
    val sleepDurations = sleepDurations(records)
    val topEntry = sleepDurations.maxBy { e ->
        e.value.values.max()!!
    }!!
    val topGuard = topEntry.key
    val topMinute = topEntry.value.maxBy { it.value }!!.key
    return topGuard * topMinute
}

val records: List<Record> by lazy {
    inputFile
        .readLines()
        .sorted()
        .map { record.parse(it).getOrFail().value }
}

val int: Parser<Int> = Rule("int") { (CharIn("+-").opt() * WhileCharIn("0123456789")).capture().map { it.toInt() } }

val date: Parser<LocalDateTime> = Rule("date") {
    (P("[") * int * P("-") * int * P("-") * int * P(" ") * int * P(":") * int * P("]"))
        .map { year, month, day, hour, min -> LocalDateTime.of(year, month, day, hour, min) }
}

val action: Parser<Action> = Rule("Action") {
    (P("wakes up").map { Action.WakeUp }) +
            P("falls asleep").map { Action.FallAsleep } +
            (P("Guard #") * int * P(" begins shift")).map { Action.BeginShift(it) }
}

data class Record(val time: LocalDateTime, val action: Action)

// Example input lines:
// [1518-09-03 23:57] Guard #1523 begins shift
// [1518-10-05 00:59] wakes up
// [1518-08-15 00:00] falls asleep
val record: Parser<Record> = Rule("Record") {
    (date * P(" ") * action).map(::Record)
}

sealed class Action {
    data class BeginShift(val guard: Int) : Action()
    object WakeUp : Action()
    object FallAsleep : Action()
}

private fun sleepDurations(
    records: List<Record>
): MutableMap<Int, MutableMap<Int, Int>> {
    val sleepDurations = mutableMapOf<Int, MutableMap<Int, Int>>()
        .withDefault { mutableMapOf<Int, Int>().withDefault { 0 } }
    val fellAsleepTimes = mutableMapOf<Int, LocalDateTime>()
    var currentGuard = -1
    records.forEach { r ->
        when {
            r.action is Action.BeginShift -> currentGuard = r.action.guard
            r.action is Action.FallAsleep -> fellAsleepTimes[currentGuard] = r.time
            r.action is Action.WakeUp -> {
                for (min in fellAsleepTimes[currentGuard]!!.minute until r.time.minute) {
                    val x = sleepDurations.getValue(currentGuard)
                    x[min] = x.getValue(min) + 1
                    sleepDurations[currentGuard] = x
                }
            }
        }
    }
    return sleepDurations
}
