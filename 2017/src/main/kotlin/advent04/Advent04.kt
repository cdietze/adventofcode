package advent04

import java.io.File
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent04/input.txt")
    println("Result part 1: ${input.readLines().count { it.split(" ").isValidPassphrase() }}")
    println("Result part 2: ${input.readLines()
            .map { it.split(" ").map { it.normalize() } }
            .count { it.isValidPassphrase() }}")
}

fun List<String>.isValidPassphrase(): Boolean = when {
    this.isEmpty() -> true
    this.drop(1).contains(this.first()) -> false
    else -> this.drop(1).isValidPassphrase()
}

fun String.normalize(): String {
    val array = this.toCharArray()
    array.sort()
    return String(array)
}

object Tests {
    @JvmStatic
    fun main(args: Array<String>) {
        assertEquals("aa bb cc dd ee".split(" ").isValidPassphrase(), true)
        assertEquals("aa bb cc dd aa".split(" ").isValidPassphrase(), false)
        assertEquals("aa bb cc dd aaa".split(" ").isValidPassphrase(), true)
    }
}
