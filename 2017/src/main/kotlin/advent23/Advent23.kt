package advent23

import java.io.File
import kotlin.collections.set

typealias Instruction = List<String>

data class State(val pc: Int = 0, val mem: MutableMap<String, Int> = mutableMapOf(), val mulCounter: Int = 0)

fun State.step(instr: Instruction): State {
    return when (instr[0]) {
        "set" -> copy(pc = pc + 1).apply { mem[instr[1]] = getValue(instr[2], mem) }
        "sub" -> copy(pc = pc + 1).apply { mem[instr[1]] = (mem[instr[1]] ?: 0) - getValue(instr[2], mem) }
        "mul" -> copy(pc = pc + 1, mulCounter = mulCounter + 1).apply { mem[instr[1]] = (mem[instr[1]] ?: 0) * getValue(instr[2], mem) }
        "jnz" -> copy(pc = if (getValue(instr[1], mem) == 0) pc + 1 else pc + getValue(instr[2], mem))
        else -> error("unknown instruction: $instr")
    }
}

tailrec fun State.run(instructions: List<Instruction>): State {
    val i = instructions.getOrNull(pc)
    return if (i == null) this else {
        step(i).run(instructions)
    }
}

fun getValue(s: String, mem: Map<String, Int>) = s.toIntOrNull() ?: mem.getOrDefault(s, 0)

fun solve2(): Int {
    val a = 1
    var c = 0
    var d = 0
    var e = 0
    var f = 0
    var g = 0
    var h = 0
//    set b 99
    var b = 99
//    set c b
//    mul b 100
    b *= 100
//    sub b -100000
    b += 100000
//    set c b
    c = b
//    sub c -17000
    c += 17000
    do {
//    set f 1
        f = 1
//    set d 2
        d = 2
        do {
//    set e 2
            e = 2
            do {
//    set g d
                g = d
//    mul g e
                g *= e
//    sub g b
                g -= b
//    jnz g 2
//    set f 0
                if (g == 0) f = 0
//    sub e -1
                e += 1
//    set g e
                g = e
//    sub g b
                g -= b
//    jnz g -8
            } while (g != 0)
//    sub d -1
            d += 1
//    set g d
            g = d
//    sub g b
            g -= b
//    jnz g -13
        } while (g != 0)
//    jnz f 2
//    sub h -1
        if (f == 0) h += 1
//    set g b
        g = b
//    sub g c
        g -= c
//    jnz g 2
//    jnz 1 3
        if (g == 0) return h
//    sub b -17
        b += 17
//    jnz 1 -23
        println("b:$b,c:$c,d:$d,e:$e,f:$f,g:$g,h:$h")
    } while (true)
}

fun main(args: Array<String>) {
    val instructions = File("src/main/kotlin/advent23/input.txt").readLines().map { it.split(" ") }
    val result = State().run(instructions)
    println("Result part 1: ${result.mulCounter}")
    val result2 = solve2()
    println("result2: $result2")
}
