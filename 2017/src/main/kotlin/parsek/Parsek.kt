package parsek

import parsek.Parsers.const
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

        override val isSuccess: Boolean get() = true
    }

    /**
     * @param index The index where this parse failed.
     */
    data class Failure(val index: Int, val lastParser: Parser<*>, val input: String) : Parsed<Nothing>() {
        init {
            // println("Failure: ${this}")
        }

        override val isSuccess: Boolean get() = false
        override fun toString(): String =
                """Parse error while processing $lastParser:
$input
${"^".padStart(index + 1)}"""
    }

    abstract val isSuccess: Boolean
    val isFailure: Boolean
        get() = !isSuccess
}

fun <T, R> Parsed<T>.match(
        onSuccess: (Parsed.Success<T>) -> R,
        onFailure: (Parsed.Failure) -> R
): R = when (this) {
    is Parsed.Success -> onSuccess(this)
    is Parsed.Failure -> onFailure(this)
}

fun <T, R> Parsed<T>.flatMap(
        f: (Parsed.Success<T>) -> Parsed<R>
): Parsed<R> = match(f, { it })

fun <T, R> Parsed<T>.map(f: (T) -> R): Parsed<R> = this.match({ s -> s.map(f) }, { it })
fun <T, R> Parsed.Success<T>.map(f: (T) -> R): Parsed.Success<R> = Parsed.Success(f(value), index)

fun <T> Parsed<T>.get(): Parsed.Success<T> = when (this) {
    is Parsed.Success -> this
    else -> fail("Parse error: ${this}")
}

interface Parser<out T> {
    fun parse(input: String, index: Int = 0): Parsed<T>
}

fun <T> Parser<T>.parseFully(input: String): Parsed<T> = parse(input, 0).flatMap {
    if (it.index == input.length) it else Parsed.Failure(it.index, this, input)
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

data class CharParser(val c: Char) : Parser<Char> {
    override fun parse(input: String, index: Int): Parsed<Char> {
        return if (input[index] == c) Parsed.Success(c, index + 1)
        else Parsed.Failure(index, this, input)
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
    fun <T> const(c: T) = object : Parser<T> {
        override fun parse(input: String, index: Int): Parsed<T> = Parsed.Success(c, index)
    }

    fun string(s: String) = StringParser(s)

    fun regex(re: Regex) = RegexParser(re)

    fun <A, B> cons(a: Parser<A>, b: () -> Parser<B>): Parser<Pair<A, B>> =
            a.flatMap { aValue -> b().map { bValue -> Pair(aValue, bValue) } }

    fun <A, B> either(a: Parser<A>, b: Parser<B>): Parser<Either<A, B>> = object : Parser<Either<A, B>> {
        override fun parse(input: String, index: Int): Parsed<Either<A, B>> {
            return a.parse(input, index).match(
                    { successA -> Parsed.Success(Either.Left(successA.value), successA.index) },
                    { _ -> b.parse(input, index).map { Either.Right<A, B>(it) } }
            )
        }
    }

    fun <T> or(a: Parser<T>, b: () -> Parser<T>): Parser<T> = object : Parser<T> {
        override fun parse(input: String, index: Int): Parsed<T> {
            return a.parse(input, index).match(
                    { it },
                    { _ -> b().parse(input, index) }
            )
        }
    }

    val int: Parser<Int> = Parsers.regex("^-?\\d+".toRegex()).map { it.value.toInt() }
    fun char(c: Char): Parser<Char> = CharParser(c)
    val char: Parser<Char> = object : Parser<Char> {
        override fun parse(input: String, index: Int): Parsed<Char> =
                Parsed.Success(input[index], index + 1)
    }
}

/**
 * Wraps a parser whose output shall be ignored.
 *
 * This is a distinct type not implementing [Parser<Unit>] to help type inference.
 */
class IgnoringParser(val p: Parser<Any?>)

operator fun <B> IgnoringParser.times(b: () -> Parser<B>): Parser<B> = Parsers.cons(this.p, b).map { it.second }

operator fun <B> IgnoringParser.times(b: Parser<B>): Parser<B> = Parsers.cons(this.p, { b }).map { it.second }

operator fun <A> Parser<A>.times(b: IgnoringParser): Parser<A> = Parsers.cons(this, { b.p }).map { it.first }

operator fun <A, B> Parser<A>.times(b: () -> Parser<B>) = Parsers.cons(this, b)
operator fun <A, B> Parser<A>.times(b: Parser<B>) = Parsers.cons(this, { b })

infix fun <T> Parser<T>.or(o: () -> Parser<T>): Parser<T> = or(this, o)
infix fun <T> Parser<T>.or(o: Parser<T>): Parser<T> = this.or({ o })

fun <T> Parser<T>.rep(): Parser<List<T>> = object : Parser<List<T>> {
    override fun parse(input: String, index: Int): Parsed<List<T>> {
        val result = mutableListOf<T>()
        var lastIndex = index
        while (true) {
            val r = this@rep.parse(input, lastIndex)
            when (r) {
                is Parsed.Success -> {
                    lastIndex = r.index
                    result.add(r.value)
                }
                else ->
                    return Parsed.Success(result, lastIndex)
            }
        }
    }
}

fun <A> Parser<A>.optional(): Parser<A?> = this.map<A, A?> { it }.or(const<A?>(null))

fun <A> Parser<A>.ignore(ws: Parser<Any?>): Parser<A> = (this * ws).map { it.first }
fun <A> Parser<A>.ignore(): IgnoringParser = IgnoringParser(this)

object Tests {
    fun main(args: Array<String>) {
        assertEquals("abc", string("abc").parse("abc").get().value)

        assertEquals("abc", regex("\\w+".toRegex()).parse("abc").get().value.value)
        assertEquals("abc", regex("\\w+".toRegex()).parse("abc").map { it.value }.get().value)

        assertEquals(Parsed.Success(Pair("a", "b"), 2), (string("a") * string("b")).parse("ab").get())

        val intParser: Parser<Int> = regex("\\d+".toRegex()).map { result -> result.value.toInt() }

        assertEquals(17, intParser.parse("17").get().value)

        val aOrB = string("a").or(string("b"))

        assertEquals("a", aOrB.parse("a").get().value)
        assertEquals("b", aOrB.parse("b").get().value)
    }
}
