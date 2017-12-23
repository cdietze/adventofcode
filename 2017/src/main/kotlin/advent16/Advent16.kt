package advent16

import parsek.*
import parsek.Parsers.char
import parsek.Parsers.int
import parsek.Parsers.string
import java.io.File

sealed class Move : (String) -> String

data class Spin(val n: Int) : Move() {
    override fun invoke(s: String): String = s.takeLast(n) + s.take(s.length - n)
}

data class Exchange(val a: Int, val b: Int) : Move() {
    override fun invoke(s: String): String =
            s.replaceRange(a..a, s[b].toString())
                    .replaceRange(b..b, s[a].toString())
}

data class Partner(val a: Char, val b: Char) : Move() {
    override fun invoke(s: String): String =
            s.replace(a, 'X').replace(b, a).replace('X', b)
}

fun String.dance(moves: List<Move>) = moves.fold(this, { s, move -> move(s) })

val spinParser: Parser<Spin> = (string("s").ignore() * int).map { Spin(it) }
val exchangeParser: Parser<Exchange> = (string("x").ignore() * int * string("/").ignore() * int).map { Exchange(it.first, it.second) }
val partnerParser: Parser<Partner> = (string("p").ignore() * char * string("/").ignore() * char).map { Partner(it.first, it.second) }
val moveParser: Parser<Move> = spinParser or exchangeParser or partnerParser

fun period(start: String, moves: List<Move>): List<String> {
    val period = mutableListOf<String>()
    var s = start
    while (true) {
        if (period.contains(s)) return period
        period.add(s)
        s = s.dance(moves)
    }
}

fun main(args: Array<String>) {
    val start = "abcdefghijklmnop"
    val moves = File("src/main/kotlin/advent16/input.txt").readText().split(",").map { moveParser.parseFully(it).get().value }
    println("Result part 1: ${start.dance(moves)}")

    val count = 1000 * 1000 * 1000
    val period = period(start, moves)
    println("Result part 2: ${period[count % period.size]}")
}
