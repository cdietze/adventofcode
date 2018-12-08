package advent2018.common

import parsek.*

val int: Parser<Int> = Rule("int") { (CharIn("+-").opt() * WhileCharIn("0123456789")).capture().map { it.toInt() } }
