package advent12

import parsek.*
import parsek.Parsers.string
import java.io.File

data class Program(val id: Int, val links: List<Int>)

val numberParser: Parser<Int> = Parsers.regex("^-?\\d+".toRegex()).map { it.value.toInt() }

val programParser: Parser<Program> =
        (numberParser.ignore(string(" <-> ")) * (numberParser.ignore(string(", ").optional())).rep())
                .map { Program(it.first, it.second) }

fun groupOfElem(programMap: Map<Int, Program>, elem: Int): Set<Int> {
    tailrec fun loop(todo: Set<Int>, seen: MutableSet<Int> = mutableSetOf()): Set<Int> {
        if (todo.isEmpty()) return seen
        val nextTodo = mutableSetOf<Int>()
        todo.forEach { id ->
            if (!seen.contains(id)) {
                seen.add(id)
                nextTodo.addAll(programMap[id]!!.links)
            }
        }
        return loop(nextTodo, seen)
    }
    return loop(mutableSetOf(elem))
}

fun groups(programMap: Map<Int, Program>): Set<Set<Int>> {
    fun Program.allElems(): List<Int> = links.toMutableList().apply { add(this@allElems.id) }
    val groups: MutableSet<Set<Int>> = programMap.keys.map { setOf(it) }.toMutableSet()

    programMap.values.forEach { program ->
        val elemGroups = program.allElems().map { id -> groups.first { it.contains(id) } }
        val newGroup: Set<Int> = mutableSetOf<Int>().apply { elemGroups.forEach { this.addAll(it) } }
        groups.removeAll(elemGroups)
        groups.add(newGroup)
    }

    return groups
}

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent12/input.txt").readLines().map { programParser.parseFully(it).get().value }
    val programMap = input.map { it.id to it }.toMap()
    println("Result part 1: ${groupOfElem(programMap, 0).size}")
    println("Result part 2: ${groups(programMap).size}")
}
