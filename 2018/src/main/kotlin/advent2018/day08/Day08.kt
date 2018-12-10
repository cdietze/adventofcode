package advent2018.day08

import advent2018.common.int
import parsek.*
import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day08/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

data class Node(val children: List<Node>, val metadata: List<Int>)

fun <A, B, R> Parser<Pair<A, B>>.flatMap(f: (A, B) -> Parser<R>): Parser<R> =
    flatMap { p -> f(p.first, p.second) }

val nodeParser: Parser<Node> = Rule("Node") {
    (int * P(" ") * int * P(" ")).flatMap { childCount, metadataSize ->
        (nodeListParser(childCount) * metadataParser(metadataSize)).map(
            ::Node
        )
    }
}.cut()

fun nodeListParser(count: Int): Parser<List<Node>> =
    if (count == 0) {
        Terminals.Pass.map { listOf<Node>() }
    } else {
        nodeParser.rep(count, count, P(" ")) * P(" ")
    }

fun metadataParser(count: Int): Parser<List<Int>> = Rule("Metadata") {
    int.rep(count, count, P(" "))
}

fun Node.metadataSum(): Int = children.sumBy { it.metadataSum() } + metadata.sum()

fun Node.value(): Int = if (children.isEmpty()) metadata.sum() else
    metadata.map { index -> children.getOrNull(index - 1)?.value() ?: 0 }.sum()

fun solvePart1(): Int {
    return inputFile.readText().let { nodeParser.parse(it).getOrFail().value }.metadataSum()
}

fun solvePart2(): Int {
    return inputFile.readText().let { nodeParser.parse(it).getOrFail().value }.value()
}
