package advent2020.day04

import advent2020.AdventDay
import parsek.*

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val passports = passportList.parse(input).getOrFail().value
    return passports.count { it.isValidPart1() }
}

fun resultPart2(input: String): Int {
    val passports = passportList.parse(input).getOrFail().value
    return passports.count { it.isValidPart2() }
}

data class Passport(val entries: Map<String, String>)

fun Passport.isValidPart1(): Boolean {
    val expectedFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    return expectedFields.all { field -> this.entries.containsKey(field) }
}

fun Passport.isValidPart2(): Boolean {
    fun validBirth(): Boolean = (1920..2002).contains(entries["byr"]?.toInt())
    fun validIssueYear(): Boolean = (2010..2020).contains(entries["iyr"]?.toInt())
    fun validExpiration(): Boolean = (2020..2030).contains(entries["eyr"]?.toInt())
    fun validHeight(): Boolean = entries["hgt"]?.let { s ->
        when {
            s.endsWith("cm") -> ((150..193).contains(s.dropLast(2).toInt()))
            s.endsWith("in") -> ((59..76).contains(s.dropLast(2).toInt()))
            else -> false
        }
    } ?: false

    fun validHair(): Boolean = "#[0-9a-f]{6}".toRegex().matches(entries["hcl"] ?: "")
    fun validEye(): Boolean = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(entries["ecl"])
    fun validPassportId(): Boolean = "[0-9]{9}".toRegex().matches(entries["pid"] ?: "")
    return validBirth() && validIssueYear() && validExpiration() && validHeight() && validHair() && validEye() && validPassportId()
}

val str: Parser<String> = CharPred { it !in " \n:" }.rep(min = 1).capture()
val itemSep: Parser<*> = CharIn(" \n")
val item: Parser<Pair<String, String>> = (str * P(":") * str)
val passport: Parser<Passport> = item.rep(min = 1, sep = itemSep).map { Passport(it.toMap()) }
val passportList: Parser<List<Passport>> = passport.rep(sep = P("\n\n"))
