package advent2020.day16

import advent2020.AdventDay
import advent2020.product

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val data = input.parseInput()
    return data.nearbyTickets.flatMap { ticket -> ticket.filter { !data.constraints.any { c -> it.isValid(c) } } }.sum()
}

fun resultPart2(input: String): Long {
    val data = input.parseInput()
    val validTickets =
        data.nearbyTickets.filter { ticket -> ticket.all { n -> data.constraints.any { c -> n.isValid(c) } } }
    // Maps constraintIndex to its set of valid columns
    val conMap = data.constraints.mapIndexed { index, c ->
        Pair(
            index,
            (data.nearbyTickets[0].indices).filter { column ->
                validTickets.all { it[column].isValid(c) }
            }.toSet()
        )
    }

    fun impl(assignments: Map<Int, Int>): Map<Int, Int> {
        if (assignments.size == conMap.size) return assignments
        val next = conMap.map { Pair(it.first, (it.second - assignments.values)) }.first { it.second.size == 1 }
        return impl(assignments + (next.first to next.second.first()))
    }

    val result = impl(mapOf())
    return result.filter { it.key < 6 }.map { data.myTicket[it.value].toLong() }.product()
}

typealias Constraint = List<IntRange>

typealias Ticket = List<Int>

data class InputData(
    val constraints: List<Constraint>,
    val myTicket: Ticket,
    val nearbyTickets: List<Ticket>,
)

fun Int.isValid(c: Constraint): Boolean = c.any { it.contains(this) }

fun String.parseInput(): InputData {
    val regions = this.split("\n\n")
    val constraints = regions[0].lines().map { it.parseConstraint() }
    val myTicket = regions[1].lines().drop(1).first().parseTicket()
    val nearbyTickets = regions[2].lines().drop(1).map { it.parseTicket() }
    return InputData(constraints, myTicket, nearbyTickets)
}

fun String.parseConstraint(): Constraint =
    Regex("(\\d+)-(\\d+)").findAll(this).map { IntRange(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }.toList()

fun String.parseTicket(): Ticket = split(",").map { it.toInt() }
