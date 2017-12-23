package advent09

import parsek.*
import parsek.Parsers.regex
import parsek.Parsers.string
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

val garbageParser: Parser<Unit> = regex("^<(([^!>]|(!.))*)?>".toRegex()).map { Unit }

// Returns the total score of this and contained groups
fun scoreParser(depth: Int = 1): Parser<Int> = (string("{").ignore() * { itemList(depth + 1).optional() } * string("}").ignore()).map { depth + (it ?: 0) }

fun item(depth: Int): Parser<Int> = scoreParser(depth).or(garbageParser.map { 0 })

fun itemList(depth: Int): Parser<Int> = (item(depth) * { (string(",").ignore() * item(depth)).rep().map { it.map { it }.sum() }.optional().map { it ?: 0 } }).map { it.first + it.second }

val countGarbageItem: Parser<Int> = (string("<") * ((regex("^[^!>]+".toRegex()).map { it.value.length }).or((regex("^(!.)+".toRegex()).map { 0 }))).rep() * string(">"))
        .map { it.first.second.sum() }

val countGarbageList: Parser<Int> = (regex("^[^<]*".toRegex()) * (countGarbageItem.ignore(regex("^[^<]*".toRegex()))).rep().map { it.sum() }).map { it.second }

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent09/input.txt").readText()
    println("Result part 1: ${scoreParser(1).parse(input).get().value}")
    println("Result part 2: ${countGarbageList.parse(input).get().value}")
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertTrue { garbageParser.parse("<>").isSuccess }
        assertTrue { garbageParser.parse("<random characters>").isSuccess }
        assertTrue { garbageParser.parse("<<<<<>").isSuccess }
        assertTrue { garbageParser.parse("<{!>}>").isSuccess }
        assertTrue { garbageParser.parse("<!!>").isSuccess }
        assertTrue { garbageParser.parse("<!!!>>").isSuccess }
        assertTrue { garbageParser.parse("<{o\"i!a,<{i<a>").isSuccess }

        assertEquals(1, scoreParser().parse("{}").get().value)
        assertEquals(6, scoreParser().parse("{{{}}}").get().value)
        assertEquals(5, scoreParser().parse("{{},{}}").get().value)
        assertEquals(16, scoreParser().parse("{{{},{},{{}}}}").get().value)
        assertEquals(1, scoreParser().parse("{<a>,<a>,<a>,<a>}").get().value)
        assertEquals(3, scoreParser().parse("{{<ab>}}").get().value)
        assertEquals(5, scoreParser().parse("{{<ab>},{}}").get().value)
        assertEquals(2, itemList(2).parse("{<ab>}").get().value)
        assertEquals(Parsed.Success(2, 6), item(2).parse("{<ab>},{<ab>}").get())
        assertEquals(4, itemList(2).parse("{<ab>},{<ab>}").get().value)
        assertEquals(5, scoreParser().parse("{{<ab>},{<ab>}}").get().value)
        assertEquals(9, scoreParser().parse("{{<ab>},{<ab>},{<ab>},{<ab>}}").get().value)
        assertEquals(9, scoreParser().parse("{{<!!>},{<!!>},{<!!>},{<!!>}}").get().value)
        assertEquals(3, scoreParser().parse("{{<a!>},{<a!>},{<a!>},{<ab>}}").get().value)

        assertEquals(17, countGarbageList.parse("<random characters>").get().value)
        assertEquals(3, countGarbageList.parse("<<<<>").get().value)

        assertEquals(2, countGarbageList.parse("<{!>}>").get().value)
        assertEquals(0, countGarbageList.parse("<!!!>>").get().value)
        assertEquals(10, countGarbageList.parse("<{o\"i!a,<{i<a>").get().value)
    }
}