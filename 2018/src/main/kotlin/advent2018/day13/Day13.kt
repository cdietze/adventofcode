package advent2018.day13

import advent2018.common.Point
import advent2018.day13.Dir.*
import java.io.File

val inputFile = File("src/main/kotlin/advent2018/day13/input.txt")

fun main(args: Array<String>) {
    println("Result part 1: ${solvePart1()}")
    println("Result part 2: ${solvePart2()}")
}

enum class Dir {
    LEFT, UP, RIGHT, DOWN;
}

operator fun Dir.invoke(p: Point): Point = when (this) {
    LEFT -> p.copy(x = p.x - 1)
    UP -> p.copy(y = p.y - 1)
    RIGHT -> p.copy(x = p.x + 1)
    DOWN -> p.copy(y = p.y + 1)
}

val Dir.right: Dir
    get() = when (this) {
        LEFT -> UP
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
    }

val Dir.left: Dir get() = right.right.right

enum class CartTurnOption {
    LEFT, STRAIGHT, RIGHT;
}

operator fun CartTurnOption.invoke(dir: Dir): Dir = when (this) {
    CartTurnOption.LEFT -> dir.left
    CartTurnOption.STRAIGHT -> dir
    CartTurnOption.RIGHT -> dir.right
}

fun CartTurnOption.next(): CartTurnOption = when (this) {
    CartTurnOption.LEFT -> CartTurnOption.STRAIGHT
    CartTurnOption.STRAIGHT -> CartTurnOption.RIGHT
    CartTurnOption.RIGHT -> CartTurnOption.LEFT
}

data class Cart(val pos: Point, val dir: Dir, val turnOption: CartTurnOption = CartTurnOption.LEFT)

data class State(val map: Map, val carts: MutableList<Cart?> = map.carts(), val currentCartIndex: Int = -1)

fun State.mostRecentCart(): Cart? = carts.getOrNull(currentCartIndex)

typealias Map = List<String>

operator fun Map.get(x: Int, y: Int): Char = this[y][x]
operator fun Map.get(pos: Point): Char = this[pos.y][pos.x]

fun <T> T?.toList(): List<T> = if (this == null) listOf() else listOf(this)

fun State.step(): State {
    if (currentCartIndex < 0 || currentCartIndex >= carts.size) {
        val newCarts: MutableList<Cart?> = carts.filterNotNull().sortedWith(Comparator<Cart> { a, b ->
            if (a.pos.y != b.pos.y) a.pos.y - b.pos.y
            else a.pos.x - b.pos.x
        }).toMutableList()
        return copy(carts = newCarts, currentCartIndex = 0)
    }
    val cart = carts[currentCartIndex] ?: return copy(currentCartIndex = currentCartIndex + 1)
    val newPos = cart.dir(cart.pos)
    val newCart = when (map[newPos]) {
        '+' -> cart.copy(pos = newPos, dir = cart.turnOption(cart.dir), turnOption = cart.turnOption.next())
        '\\' -> cart.copy(
            pos = newPos, dir = when (cart.dir) {
                LEFT -> UP
                UP -> LEFT
                RIGHT -> DOWN
                DOWN -> RIGHT
            }
        )
        '/' -> cart.copy(
            pos = newPos, dir = when (cart.dir) {
                LEFT -> DOWN
                UP -> RIGHT
                RIGHT -> UP
                DOWN -> LEFT
            }
        )
        else -> cart.copy(pos = newPos)
    }
    carts[currentCartIndex] = newCart
    return copy(currentCartIndex = currentCartIndex + 1)
}

private fun Map.carts(): MutableList<Cart?> = withIndex().flatMap { y ->
    y.value.withIndex().flatMap { x ->
        val c = this[x.index, y.index]
        val dir = when (c) {
            '<' -> LEFT
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            else -> null
        }
        dir?.let { Cart(Point(x.index, y.index), it) }.toList()
    }
}.toMutableList()

fun solvePart1(): Point {
    val map: Map = inputFile.readLines()
    var state = State(map)
    while (true) {
        state = state.step()
        state.mostRecentCart()?.let { recentCart ->
            if (state.carts.filterNotNull().count { it.pos == recentCart.pos } > 1) return recentCart.pos
        }
    }
}

fun solvePart2(): Point {
    val map: Map = inputFile.readLines()
    var state = State(map)
    while (true) {
        state = state.step()
        state.mostRecentCart()?.let { recentCart ->
            if (state.carts.filterNotNull().count { it.pos == recentCart.pos } > 1) {
                state.carts.replaceAll { if (it?.pos == recentCart.pos) null else it }
            }
        }
        if (state.currentCartIndex == 0 && state.carts.count() <= 1) {
            return state.carts.first()!!.pos
        }
    }
}
