package advent2018.day12

import parsek.*
import java.io.File
import java.util.*

val inputFile = File("src/main/kotlin/advent2018/day12/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
//    println("Result part 2: ${solvePart2()}")
}

typealias State = Set<Int>

data class Input(
    val initialState: State,
    val transformations: BitSet
)

val inputParser: Parser<Input> = Rule("Input") {
    val initialStateParser: Parser<State> = Rule("InitialState") {
        P("initial state: ") * CharIn(".#").rep().map {
            it.foldIndexed(mutableSetOf<Int>()) { i, acc, e ->
                acc.apply {
                    if (e == '#') add(i)
                }
            }
        }
    }
    val transformationParser: Parser<Pair<Int, Boolean>> = Rule("Transformation") {
        (CharIn(".#").rep(5, 5)
            .map { it.fold(0) { acc, e -> if (e == '#') (acc shl 1) + 1 else (acc shl 1) } }) *
                P(" => ") * (CharIn(".#").map { it == '#' })
    }
    (initialStateParser * P("\n\n") * transformationParser.rep(sep = P("\n")).map {
        it.fold(BitSet()) { acc, e ->
            acc.apply { set(e.first, e.second) }
        }
    }).map(::Input)
}

fun State.step(transformations: BitSet): State =
    ((this.min()!! - 2)..(this.max()!! + 2)).fold(mutableSetOf()) { resultSetAcc, i ->
        val bits = ((i - 2)..(i + 2)).fold(0) { acc, ii ->
            if (this.contains(ii)) ((acc shl 1) + 1) else acc shl 1
        }
        resultSetAcc.apply {
            if (transformations[bits]) add(i)
        }
    }

fun solvePart1(): Int {
    val input = inputFile.readText().let { inputParser.parse(it).getOrFail().value }
    val finalState = (0 until 20).fold(input.initialState) { acc, i -> acc.step(input.transformations) }
    return finalState.sum()
}
