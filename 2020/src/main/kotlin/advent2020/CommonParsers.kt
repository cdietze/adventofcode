package advent2020

import parsek.*

object CommonParsers {
    val int: Parser<Int> = Rule("int") { (CharIn("+-").opt() * WhileCharIn("0123456789")).capture().map { it.toInt() } }
    val space = Rule("space") { WhileCharIn(" \r\n", min = 0) }
    val word: Parser<String> = Rule("word") { WhileCharIn(('a'..'z') + ('A'..'Z')) }.capture()
}
