package advent2020.day21

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Int {
    val foods = input.parseFoods()
    val allergenToIngredientsMap: Map<String, Set<String>> = foods.allergenToIngredientsMap()
    val unsafeIngredients = allergenToIngredientsMap.values.flatten().toSet()
    return foods.flatMap { it.ingredients }.filter { it !in unsafeIngredients }.size
}

fun resultPart2(input: String): String {
    val foods = input.parseFoods()
    val allergenToIngredientsMap: Map<String, Set<String>> = foods.allergenToIngredientsMap()
    tailrec fun impl(open: Map<String, Set<String>>, resolved: Map<String, String>): Map<String, String> {
        if (open.isEmpty()) return resolved
        val e = open.entries.first { it.value.size == 1 }
        return impl((open - e.key).mapValues { v -> v.value - e.value }, resolved + Pair(e.key, e.value.single()))
    }
    return impl(allergenToIngredientsMap, mapOf()).toSortedMap().entries.joinToString(separator = ",") { it.value }
}

data class Food(val ingredients: Set<String>, val allergens: Set<String>)

fun String.parseFoods(): List<Food> = lines().map { line ->
    val parts = line.filter { it.isLetter() || it == ' ' }.split(" contains ")
    Food(parts[0].split(" ").toSet(), parts[1].split(" ").toSet())
}

fun List<Food>.allergenToIngredientsMap(): Map<String, Set<String>> {
    fun findIngredients(allergen: String): Set<String> =
        this.filter { allergen in it.allergens }.map { it.ingredients }.reduce { acc, f -> acc intersect f }
    return this.flatMap { it.allergens }.toSet().map { allergen -> Pair(allergen, findIngredients(allergen)) }.toMap()
}
