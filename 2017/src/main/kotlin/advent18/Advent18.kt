package advent18

import advent18.Instruction.*
import advent18.Instruction.Set
import parsek.*
import parsek.Parsers.char
import parsek.Parsers.either
import parsek.Parsers.long
import parsek.Parsers.string
import java.io.File

data class State(val pc: Int, val mem: Map<Char, Long>, val lastSound: Long, val recovered: Long)

sealed class Instruction : (State) -> State {

    data class Snd(val x: Source) : Instruction() {
        override fun invoke(s: State): State = s.copy(pc = s.pc + 1, lastSound = s.mem.get(x))
    }

    data class Set(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1, mem = s.mem.toMutableMap().apply { this[x] = s.mem.get(y) })
    }

    data class Add(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1,
                        mem = s.mem.toMutableMap().apply {
                            this[x] = (this[x] ?: 0) + this.get(y)
                        })
    }

    data class Mul(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1,
                        mem = s.mem.toMutableMap().apply {
                            this[x] = (this[x] ?: 0) * this.get(y)
                        })
    }

    data class Mod(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1,
                        mem = s.mem.toMutableMap().apply {
                            this[x] = (this[x] ?: 0) % this.get(y)
                        })
    }

    data class Rcv(val x: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = s.pc + 1, recovered = if (s.mem.get(x) == 0L) s.recovered else s.lastSound)
    }

    data class Jgz(val x: Char, val y: Source) : Instruction() {
        override fun invoke(s: State): State =
                s.copy(pc = if ((s.mem[x] ?: 0L) > 0L) s.pc + s.mem.get(y).toInt() else s.pc + 1)
    }
}

typealias Source = Either<Long, Char>
fun Map<Char, Long>.get(s: Source): Long = when (s) {
    is Either.Left -> s.value
    is Either.Right -> this[s.value] ?: 0
}

val sourceParser: Parser<Source> = either(long, char)
val sndParser: Parser<Snd> = (string("snd ").ignore() * sourceParser).map { Snd(it) }
val setParser: Parser<Instruction.Set> = (string("set ").ignore() * char * string(" ").ignore() * sourceParser).map { Set(it.first, it.second) }
val addParser: Parser<Add> = (string("add ").ignore() * char * string(" ").ignore() * sourceParser).map { Add(it.first, it.second) }
val mulParser: Parser<Mul> = (string("mul ").ignore() * char * string(" ").ignore() * sourceParser).map { Mul(it.first, it.second) }
val modParser: Parser<Mod> = (string("mod ").ignore() * char * string(" ").ignore() * sourceParser).map { Mod(it.first, it.second) }
val rcvParser: Parser<Rcv> = (string("rcv ").ignore() * sourceParser).map { Rcv(it) }
val jgzParser: Parser<Jgz> = (string("jgz ").ignore() * char * string(" ").ignore() * sourceParser).map { Jgz(it.first, it.second) }

val instructionParser: Parser<Instruction> = sndParser or
        setParser or
        addParser or
        mulParser or
        modParser or
        rcvParser or
        jgzParser

fun solve1(instructions: List<Instruction>): Long {
    var state = State(0, mutableMapOf(), 0, 0)
    while (state.recovered <= 0) {
        state = instructions[state.pc].invoke(state)
    }
    return state.recovered
}

fun main(args: Array<String>) {
    val instructions = File("src/main/kotlin/advent18/input.txt").readLines().map { instructionParser.parseFully(it).get().value }
    println("Result part 1: ${solve1(instructions)}")
}
