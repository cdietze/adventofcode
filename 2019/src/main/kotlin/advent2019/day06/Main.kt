package advent2019.day06

import parsek.P
import parsek.Parser
import parsek.WhileCharIn
import parsek.capture
import parsek.getOrFail
import parsek.map
import parsek.times

val inputText = object {}.javaClass.getResource("input.txt").readText()

fun main() {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

val name: Parser<String> = WhileCharIn(('0'..'9') + ('A'..'Z')).capture()

val orbitParser: Parser<Orbit> = (name * P(")") * name).map(::Orbit)

data class Orbit(
    val center: String,
    val orbiter: String
)

fun solvePart1(): Int {
    val orbits = inputText.lines().map { orbitParser.parse(it).getOrFail().value }
    // maps centers to orbiters
    val map = mutableMapOf<String, MutableList<String>>()
    orbits.forEach { o ->
        map[o.center] = map.getOrDefault(o.center, mutableListOf())
        map[o.center]!!.add(o.orbiter)
    }
    fun sumOrbits(center: String, depth: Int): Int =
        map[center]?.sumBy { orbiter -> sumOrbits(orbiter, depth + 1) + depth } ?: 0
    return sumOrbits("COM", 1)
}

fun solvePart2(): Int {
    val orbits = inputText.lines().map { orbitParser.parse(it).getOrFail().value }
    // maps orbiters to centers
    val map = orbits.map { Pair(it.orbiter, it.center) }.toMap()
    fun parentChain(n: String): List<String> = when (n) {
        "COM" -> listOf()
        else -> map.getValue(n).let { parent -> parentChain(parent) + parent }
    }

    val santaChain = parentChain("SAN")
    val youChain = parentChain("YOU")
    val common = santaChain.intersect(youChain).size
    return santaChain.size + youChain.size - 2 * common
}
