package advent08

import parsek.*
import parsek.Parsers.regex
import parsek.Parsers.string
import java.io.File

// Example instruction:
// iw dec -785 if app == 325
data class Instruction(val id: String, val action: Action, val value: Int, val condition: Condition)

enum class Action {
    Inc {
        override fun invoke(a: Int, b: Int): Int = a + b
    },
    Dec {
        override fun invoke(a: Int, b: Int): Int = a - b
    };

    abstract operator fun invoke(a: Int, b: Int): Int
}

enum class Operator {
    Equal {
        override fun invoke(a: Int, b: Int): Boolean = a == b
    },
    NotEqual {
        override fun invoke(a: Int, b: Int): Boolean = a != b
    },
    GreaterOrEqual {
        override fun invoke(a: Int, b: Int): Boolean = a >= b
    },
    Greater {
        override fun invoke(a: Int, b: Int): Boolean = a > b
    },
    LessOrEqual {
        override fun invoke(a: Int, b: Int): Boolean = a <= b
    },
    Less {
        override fun invoke(a: Int, b: Int): Boolean = a < b
    };

    abstract operator fun invoke(a: Int, b: Int): Boolean
}

data class Condition(val id: String, val op: Operator, val num: Int)

val ws: Parser<Unit> = regex("^\\s+".toRegex()).map { Unit }

val idParser: Parser<String> = Parsers.regex("^\\w+".toRegex()).map { it.value }

val actionParser: Parser<Action> = string("inc").map { Action.Inc } or string("dec").map { Action.Dec }

val numberParser: Parser<Int> = regex("^-?\\d+".toRegex()).map { it.value.toInt() }

val operatorParser: Parser<Operator> =
        string("==").map { Operator.Equal } or
                string("!=").map { Operator.NotEqual } or
                string(">=").map { Operator.GreaterOrEqual } or
                string(">").map { Operator.Greater } or
                string("<=").map { Operator.LessOrEqual } or
                string("<").map { Operator.Less }

val conditionParser: Parser<Condition> =
        (idParser.ignore(ws) * operatorParser.ignore(ws) * numberParser).map {
            Condition(
                    it.first.first,
                    it.first.second,
                    it.second)
        }

val instructionParser: Parser<Instruction> = (
        idParser.ignore(ws) *
                actionParser.ignore(ws) *
                numberParser.ignore(ws) *
                string("if").ignore(ws) *
                conditionParser
        ).map {
    Instruction(
            it.first.first.first.first,
            it.first.first.first.second,
            it.first.first.second,
            it.second)
}

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent08/input.txt").readLines().map { instructionParser.parse(it).get().value }
    val data = input.run()
    println("Result part 1: ${data.values.max()}")
    println("Result part 2: ${input.maxValueEver()}")
}

fun List<Instruction>.run(): Map<String, Int> {
    val data = mutableMapOf<String, Int>().withDefault { 0 }
    this.forEach { it.run(data) }
    return data
}

fun List<Instruction>.maxValueEver(): Int {
    var max = 0
    val data = mutableMapOf<String, Int>().withDefault { 0 }
    this.forEach {
        it.run(data)
        max = kotlin.math.max(max, data.values.max() ?: 0)
    }
    return max
}

fun Instruction.run(data: MutableMap<String, Int>) {
    if (condition.isTrue(data)) {
        data[id] = action(data.getValue(id), value)
    }
}

fun Condition.isTrue(data: Map<String, Int>): Boolean = op(data.getValue(id), num)
