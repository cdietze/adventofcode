package advent2020

fun Iterable<Int>.product(): Int = fold(1, { acc, v -> acc * v })
fun Iterable<Long>.product(): Long = fold(1, { acc, v -> acc * v })
