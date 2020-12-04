package advent2020.day04

import advent2020.AdventDay
import parsek.*

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
//    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val passports = passportList.parse(input).getOrFail().value
    return passports.count { it.isValid() }
}

typealias Item = Pair<String, String>

data class Passport(val items: List<Item>)

val expectedFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun Passport.isValid(): Boolean = expectedFields.all { field -> items.any { it.first == field } }

val str: Parser<String> = CharPred { it !in " \n:" }.rep(min = 1).capture()
val itemSep: Parser<*> = CharIn(" \n")
val item: Parser<Item> = (str * P(":") * str)
val passport: Parser<Passport> = item.rep(min = 1, sep = itemSep).map(::Passport)
val passportList: Parser<List<Passport>> = passport.rep(sep = P("\n\n"))
