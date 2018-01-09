package advent24

import parsek.*
import java.io.File

typealias Component = Pair<Int, Int>

val componentParser: Parser<Component> = (Parsers.int * Parsers.char('/').ignore() * Parsers.int)
fun Component.strength(): Int = first + second

typealias Result = Pair<List<Int>, Int>
fun Result.print(components: List<Component>): String = "length:${this.first.size},strength:$second,chain:${first.map { components[it] }}"

object ResultComparators {
    val byStrength = compareBy<Result> { it.second }
    val byLengthThenStrength = Comparator<Result> { a, b -> compareValues(a.first.size, b.first.size) }.then(compareBy { it.second })
}

fun List<Component>.bestChain(
        chain: List<Int> = listOf(),
        port: Int = 0,
        strength: Int = 0,
        resultComparator: Comparator<Result> = ResultComparators.byStrength): Result {
    val x = this.withIndex().filter { p -> !chain.contains(p.index) }.mapNotNull { p ->
        when (port) {
            p.value.first -> bestChain(chain.toMutableList().apply { add(p.index) }, p.value.second, strength + p.value.strength(), resultComparator)
            p.value.second -> bestChain(chain.toMutableList().apply { add(p.index) }, p.value.first, strength + p.value.strength(), resultComparator)
            else -> null
        }
    }
    return x.maxWith(resultComparator) ?: Pair(chain, strength)
}

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent24/input.txt").readLines().map { componentParser.parse(it).get().value }
    println("Result part 1: ${input.bestChain().second}")
    println("Result part 2: ${input.bestChain(resultComparator = ResultComparators.byLengthThenStrength).second}")
}
