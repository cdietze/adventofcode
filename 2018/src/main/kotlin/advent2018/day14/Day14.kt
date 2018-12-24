package advent2018.day14

const val input = "513401"

const val zeroCharValue = '0'.toInt()

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

data class State(val recipes: StringBuilder, val firstIndex: Int, val secondIndex: Int)

fun State.step(): State {
    val firstVal: Int = recipes[firstIndex].toInt() - zeroCharValue
    val secondVal: Int = recipes[secondIndex].toInt() - zeroCharValue
    recipes.append(firstVal + secondVal)
    return copy(
        firstIndex = (firstIndex + firstVal + 1) % recipes.length,
        secondIndex = (secondIndex + secondVal + 1) % recipes.length
    )
}

fun solvePart1(): String {
    val inputAsInt = input.toInt()
    var state = State(StringBuilder("37"), firstIndex = 0, secondIndex = 1)
    while (state.recipes.length < inputAsInt + 10) state = state.step()
    return state.recipes.drop(inputAsInt).take(10).toString()
}

fun solvePart2(): Int {
    tailrec fun loop(state: State): Int {
        val index = state.recipes.takeLast(input.length + 2).indexOf(input)
        return if (index >= 0) state.recipes.indexOf(input)
        else loop(state.step())
    }
    return loop(State(StringBuilder("37"), firstIndex = 0, secondIndex = 1))
}
