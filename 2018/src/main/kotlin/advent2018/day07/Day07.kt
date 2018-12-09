package advent2018.day07

import parsek.*
import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day07/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

data class Relation(val dependency: Char, val subject: Char)

val relationParser: Parser<Relation> = Rule("Relations") {
    (P("Step ") * CharPred { true } * P(" must be finished before step ") * CharPred { true } * P(" can begin."))
        .map(::Relation)
}

fun solvePart1(): String {
    val relations: List<Relation> = inputFile.readLines().map { relationParser.parse(it).getOrFail().value }
    val allInstructions = relations.flatMap { listOf(it.subject, it.dependency) }.distinct().toMutableList().sorted()
    tailrec fun loop(instructions: String): String {
        val open = allInstructions.filter { !instructions.contains(it) }
        if (open.isEmpty()) return instructions
        val r = relations.filter { !instructions.contains(it.dependency) }
        val v = open.first { v ->
            r.all { it.subject != v }
        }
        return loop(instructions + v)
    }
    return loop("")
}

val Char.duration: Int get() = 61 + (this - 'A')

data class Task(val instruction: Char, val endTime: Int)

fun solvePart2(): Int {
    val workers = 5
    val relations: List<Relation> = inputFile.readLines().map { relationParser.parse(it).getOrFail().value }
    val allInstructions = relations.flatMap { listOf(it.subject, it.dependency) }.distinct().toMutableList().sorted()
    tailrec fun loop(time: Int, tasks: List<Task>): Int {
        val openInstructions: List<Char> = allInstructions.filter { i -> !tasks.map { it.instruction }.contains(i) }
        if (openInstructions.isEmpty()) return tasks.map { it.endTime }.max()!!
        val done = tasks.filter { it.endTime <= time }.map { it.instruction }
        val r = relations.filter { !done.contains(it.dependency) }
        val newInstructions: List<Char> = openInstructions.filter { v ->
            r.all { it.subject != v }
        }.take(workers)
        val updatedTasks = tasks + newInstructions.map { Task(it, time + it.duration) }
        return loop(updatedTasks.filter { it.endTime > time }.map { it.endTime }.min()!!, updatedTasks)
    }
    return loop(0, listOf())
}
