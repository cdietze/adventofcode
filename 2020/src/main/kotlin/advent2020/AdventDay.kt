package advent2020

interface AdventDay {
    fun resultPart1(): Any = "Part 1 not implemented"
    fun resultPart2(): Any = "Part 2 not implemented"
    fun run() {
        println("Running ${this.javaClass.name}")
        println("Result part 1: ${resultPart1()}")
        println("Result part 2: ${resultPart2()}")
        println()
    }
}
