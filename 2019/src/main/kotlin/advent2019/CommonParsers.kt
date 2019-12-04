package advent2019

import parsek.CharIn
import parsek.Parser
import parsek.Rule
import parsek.WhileCharIn
import parsek.capture
import parsek.map
import parsek.opt
import parsek.times

object CommonParsers {
    val int: Parser<Int> = Rule("int") { (CharIn("+-").opt() * WhileCharIn("0123456789")).capture().map { it.toInt() } }
}
