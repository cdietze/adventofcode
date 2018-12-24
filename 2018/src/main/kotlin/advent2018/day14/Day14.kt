package advent2018.day14

val input = 513401
//val input = 9

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
//    println("Result part 2: ${solvePart2()}")
}

data class State(val recipes: MutableList<Int>, val firstIndex: Int, val secondIndex: Int)

fun State.step(): State {
    val firstVal = recipes[firstIndex]
    val secondVal = recipes[secondIndex]
    val sum = firstVal + secondVal
    if (sum / 10 == 1) recipes.add(1)
    recipes.add(sum % 10)
    return copy(
        firstIndex = (firstIndex + firstVal + 1) % recipes.size,
        secondIndex = (secondIndex + secondVal + 1) % recipes.size
    )
}

fun solvePart1(): String {
    var state = State(mutableListOf(3, 7), firstIndex = 0, secondIndex = 1)
    while (state.recipes.size < input + 10) state = state.step()
    return state.recipes.drop(input).take(10).joinToString("")
}
