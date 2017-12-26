package advent18

import advent18.Instruction.*
import advent18.Instruction.Set
import parsek.*
import parsek.Either.Left
import parsek.Parsers.char
import parsek.Parsers.either
import parsek.Parsers.long
import parsek.Parsers.string
import java.io.File
import java.math.BigDecimal

data class State(val pc: Int = 0, val mem: Map<Char, BigDecimal> = mapOf(), val sndQueue: List<BigDecimal> = listOf(), val rcvQueue: List<BigDecimal> = listOf(), val waiting: Boolean = false)

sealed class Instruction : (State) -> State {

    data class Snd(val x: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1, sndQueue = s.sndQueue.toMutableList().apply { add(s.mem.load(x)) })
    }

    data class Rcv(val x: Char) : Instruction() {
        override fun invoke(s: State): State =
                if (s.rcvQueue.isEmpty()) s.copy(waiting = true)
                else s.copy(
                        pc = s.pc + 1,
                        mem = s.mem.toMutableMap().apply { this[x] = s.rcvQueue.first() },
                        rcvQueue = s.rcvQueue.drop(1),
                        waiting = false
                )
    }

    data class Set(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1, mem = s.mem.toMutableMap().apply { this[x] = s.mem.load(y) })
    }

    data class Add(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1,
                        mem = s.mem.toMutableMap().apply {
                            this[x] = (this[x] ?: BigDecimal.ZERO) + this.load(y)
                        })
    }

    data class Mul(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1,
                        mem = s.mem.toMutableMap().apply {
                            this[x] = (this[x] ?: BigDecimal.ZERO) * this.load(y)
                        })
    }

    data class Mod(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1,
                        mem = s.mem.toMutableMap().apply {
                            this[x] = (this[x] ?: BigDecimal.ZERO) % this.load(y)
                        })
    }

    data class Jgz(val x: Source, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = if (s.mem.load(x) > BigDecimal.ZERO) s.pc + s.mem.load(y).toInt() else s.pc + 1)
    }
}

typealias Source = Either<BigDecimal, Char>
fun Map<Char, BigDecimal>.load(s: Source): BigDecimal = when (s) {
    is Left -> s.value
    is Either.Right -> this[s.value] ?: BigDecimal.ZERO
}

val sourceParser: Parser<Source> = either(long.map { BigDecimal(it) }, char)
val sndParser: Parser<Snd> = (string("snd ").ignore() * sourceParser).map { Snd(it) }
val setParser: Parser<Instruction.Set> = (string("set ").ignore() * char * string(" ").ignore() * sourceParser).map { Set(it.first, it.second) }
val addParser: Parser<Add> = (string("add ").ignore() * char * string(" ").ignore() * sourceParser).map { Add(it.first, it.second) }
val mulParser: Parser<Mul> = (string("mul ").ignore() * char * string(" ").ignore() * sourceParser).map { Mul(it.first, it.second) }
val modParser: Parser<Mod> = (string("mod ").ignore() * char * string(" ").ignore() * sourceParser).map { Mod(it.first, it.second) }
val rcvParser: Parser<Rcv> = (string("rcv ").ignore() * char).map { Rcv(it) }
val jgzParser: Parser<Jgz> = (string("jgz ").ignore() * sourceParser * string(" ").ignore() * sourceParser).map { Jgz(it.first, it.second) }

val instructionParser: Parser<Instruction> = sndParser or
        setParser or
        addParser or
        mulParser or
        modParser or
        rcvParser or
        jgzParser

fun solve1(instructions: List<Instruction>): BigDecimal {
    var state = State()
    while (!state.waiting) {
        state = instructions[state.pc].invoke(state)
    }
    return state.sndQueue.last()
}

fun solve2(instructions: List<Instruction>): Int {
    var state0 = State(mem = mapOf('p' to BigDecimal.valueOf(0L)))
    var state1 = State(mem = mapOf('p' to BigDecimal.valueOf(1L)))
    var sendCounter = 0
    while (!state0.waiting || !state1.waiting) {
        state0 = instructions[state0.pc].invoke(state0)
        state1 = instructions[state1.pc].invoke(state1)
        sendCounter += state1.sndQueue.size
        val snd0 = state0.sndQueue
        val snd1 = state1.sndQueue
        state0 = state0.copy(
                rcvQueue = state0.rcvQueue.toMutableList().apply { addAll(snd1) },
                sndQueue = listOf())
        state1 = state1.copy(
                rcvQueue = state1.rcvQueue.toMutableList().apply { addAll(snd0) },
                sndQueue = listOf())
    }
    return sendCounter

}

fun main(args: Array<String>) {
    val instructions = File("src/main/kotlin/advent18/input.txt").readLines().map { instructionParser.parseFully(it).get().value }
    println("Result part 1: ${solve1(instructions)}")
    println("Result part 2: ${solve2(instructions)}")
}

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        val instructions = listOf(
                Snd(Left(BigDecimal(1L))),
                Snd(Left(BigDecimal(2L))),
                Snd(Either.Right('p')),
                Rcv('a'),
                Rcv('b'),
                Rcv('c'),
                Rcv('d')
        )
        println("solve2: ${solve2(instructions)}")
    }
}
