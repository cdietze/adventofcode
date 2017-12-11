package parsek

import parsek.Parsers.or
import parsek.Parsers.regex
import parsek.Parsers.string
import kotlin.test.assertEquals
import kotlin.test.fail

sealed class Parsed<out T> {
    /**
     * @param index The index *after* this parse completed.
     */
    data class Success<T>(val value: T, val index: Int) : Parsed<T>() {
        init {
            // println("Matched: ${this}")
        }
    }

    /**
     * @param index The index where this parse failed.
     */
    data class Failure(val index: Int, val lastParser: Parser<*>, val input: String) : Parsed<Nothing>() {
        override fun toString(): String =
                """Parse error while processing $lastParser:
$input
${"^".padStart(index + 1)}"""
    }
}

fun <T, R> Parsed<T>.match(
        onSuccess: (Parsed.Success<T>) -> R,
        onFailure: (Parsed.Failure) -> R
): R = when (this) {
    is Parsed.Success -> onSuccess(this)
    is Parsed.Failure -> onFailure(this)
}

fun <T, R> Parsed<T>.map(f: (T) -> R): Parsed<R> = this.match({ s -> s.map(f) }, { it })
fun <T, R> Parsed.Success<T>.map(f: (T) -> R): Parsed.Success<R> = Parsed.Success(f(value), index)

fun <T> Parsed<T>.get(): Parsed.Success<T> = when (this) {
    is Parsed.Success -> this
    else -> fail("Parse error: ${this}")
}

interface Parser<T> {
    fun parse(input: String, index: Int = 0): Parsed<T>
}

typealias P0 = Parser<Unit>

fun <T, R> Parser<T>.flatMap(f: (T) -> Parser<R>): Parser<R> = object : Parser<R> {
    override fun parse(input: String, index: Int): Parsed<R> {
        val parsed = this@flatMap.parse(input, index)
        return when (parsed) {
            is Parsed.Success -> f(parsed.value).parse(input, parsed.index)
            is Parsed.Failure -> parsed
        }
    }
}

fun <T, R> Parser<T>.map(f: (T) -> R) = object : Parser<R> {
    override fun parse(input: String, index: Int): Parsed<R> {
        val parsed = this@map.parse(input, index)
        return when (parsed) {
            is Parsed.Success -> parsed.map(f)
            is Parsed.Failure -> parsed
        }
    }
}

data class StringParser(val s: String) : Parser<String> {
    override fun parse(input: String, index: Int): Parsed<String> {
        return when {
            input.startsWith(s, index) -> Parsed.Success(s, index + s.length)
            else -> Parsed.Failure(index, this, input)
        }
    }
}

data class RegexParser(val re: Regex) : Parser<MatchResult> {
    override fun parse(input: String, index: Int): Parsed<MatchResult> {
        val result = re.find(input.substring(index))
        return when (result) {
            null -> Parsed.Failure(index, this, input)
            else -> Parsed.Success(result, index + result.value.length)
        }
    }
}

sealed class Either<L, R> {
    data class Left<L, R>(val value: L) : Either<L, R>()
    data class Right<L, R>(val value: R) : Either<L, R>()
}

object Parsers {
    fun string(s: String) = StringParser(s)

    fun regex(re: Regex) = RegexParser(re)

    fun <A, B> cons(a: Parser<A>, b: Parser<B>): Parser<Pair<A, B>> =
            a.flatMap { aValue -> b.map { bValue -> Pair(aValue, bValue) } }

    fun <A, B> either(a: Parser<A>, b: Parser<B>): Parser<Either<A, B>> = object : Parser<Either<A, B>> {
        override fun parse(input: String, index: Int): Parsed<Either<A, B>> {
            return a.parse(input, index).match(
                    { successA -> Parsed.Success(Either.Left(successA.value), successA.index) },
                    { _ -> b.parse(input, index).map { Either.Right<A, B>(it) } }
            )
        }
    }

    fun <T> or(a: Parser<T>, b: Parser<T>): Parser<T> = object : Parser<T> {
        override fun parse(input: String, index: Int): Parsed<T> {
            return a.parse(input, index).match(
                    { it },
                    { _ -> b.parse(input, index) }
            )
        }
    }
}

operator fun <A, B> Parser<A>.times(b: Parser<B>) = Parsers.cons(this, b)

infix fun <T> Parser<T>.or(o: Parser<T>): Parser<T> = or(this, o)

fun <A> Parser<A>.ignoreWs(ws: P0): Parser<A> = flatMap { aValue -> ws.map { aValue } }

object Tests {
    fun main(args: Array<String>) {
        assertEquals("abc", string("abc").parse("abc").get().value)

        assertEquals("abc", regex("\\w+".toRegex()).parse("abc").get().value.value)
        assertEquals("abc", regex("\\w+".toRegex()).parse("abc").map { it.value }.get().value)

        assertEquals(Parsed.Success(Pair("a", "b"), 2), (string("a") * string("b")).parse("ab").get())

        val intParser: Parser<Int> = regex("\\d+".toRegex()).map { result -> result.value.toInt() }

        assertEquals(17, intParser.parse("17").get().value)

        val aOrB = or(string("a"), string("b"))

        assertEquals("a", aOrB.parse("a").get().value)
        assertEquals("b", aOrB.parse("b").get().value)
    }
}