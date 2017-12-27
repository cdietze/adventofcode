package advent19

import java.io.File

data class Point(val x: Int, val y: Int)

enum class Dir {
    TOP, RIGHT, DOWN, LEFT;
}

fun Dir.move(pos: Point): Point = when (this) {
    Dir.TOP -> pos.copy(y = pos.y - 1)
    Dir.RIGHT -> pos.copy(x = pos.x + 1)
    Dir.DOWN -> pos.copy(y = pos.y + 1)
    Dir.LEFT -> pos.copy(x = pos.x - 1)
}

fun Dir.turnLeft(): Dir = when (this) {
    Dir.TOP -> Dir.LEFT
    Dir.RIGHT -> Dir.TOP
    Dir.DOWN -> Dir.RIGHT
    Dir.LEFT -> Dir.DOWN
}

fun Dir.turnRight(): Dir = turnLeft().turnLeft().turnLeft()

typealias Maze = List<String>

operator fun Maze.get(pos: Point): Char = this[pos.y][pos.x]

fun Maze.start(): Point = Point(this[0].indexOfFirst { it == '|' }, 0)

fun Maze.solve(): Cursor {
    tailrec fun loop(c: Cursor): Cursor {
        val next = c.proceed(this)
        return if (next == null) return c
        else loop(next)
    }
    return loop(Cursor(start(), Dir.DOWN))
}

data class Cursor(val pos: Point, val dir: Dir, val path: String = "", val steps: Int = 0)

fun Cursor.proceed(maze: Maze): Cursor? {
    return when (maze[pos]) {
        '+' -> {
            val newDir: Dir = if (maze[dir.turnLeft().move(pos)] != ' ') dir.turnLeft() else dir.turnRight()
            copy(pos = newDir.move(pos), dir = newDir, steps = steps + 1)
        }
        '-', '|' -> copy(pos = dir.move(pos), steps = steps + 1)
        in 'A'..'Z' -> copy(pos = dir.move(pos), path = path + maze[pos], steps = steps + 1)
        else -> null
    }
}

fun main(args: Array<String>) {
    val maze: Maze = File("src/main/kotlin/advent19/input.txt").readLines()
    println("Result part 1: ${maze.solve().path}")
    println("Result part 2: ${maze.solve().steps}")
}
