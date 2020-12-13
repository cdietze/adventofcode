package advent2020.day13

import advent2020.AdventDay

object Main : AdventDay {
    private val inputText by lazy { this.javaClass.getResource("input.txt").readText() }

    @JvmStatic
    fun main(args: Array<String>) = run()

    override fun resultPart1(): Any = resultPart1(inputText)
    override fun resultPart2(): Any = resultPart2(inputText)
}

fun resultPart1(input: String): Long {
    val lines = input.lines()
    val arrival = lines[0].toLong()
    val busses = lines[1].split(",").mapNotNull { it.toLongOrNull() }
    fun waitTime(bus: Long): Long {
        val nextDeparture = ((arrival - 1) / bus + 1) * bus
        return nextDeparture - arrival
    }
    return busses.map { id -> Pair(id, waitTime(id)) }.minByOrNull { it.second }!!.let {
        it.first * it.second
    }
}

fun resultPart2(input: String): Long {
    return chineseRemainder(input.lines()[1].split(",")
        .mapIndexedNotNull { index, s -> s.toLongOrNull()?.let { Pair(-index.toLong(), it) } })
}

@Suppress("LocalVariableName")
fun chineseRemainder(l: List<Pair<Long, Long>>): Long {

    /** Extended euclid algorithm, without gcd output.
     * I.e. Returns a pair `p´ such that `a*p.first + b*p.second == 1`.
     * https://de.wikipedia.org/wiki/Erweiterter_euklidischer_Algorithmus#Rekursive_Variante
     */
    fun euclid(a: Long, b: Long): Pair<Long, Long> {
        if (b == 0L) return Pair(1, 0)
        val e = euclid(b, a % b)
        return Pair(e.second, e.first - e.second * (a / b))
    }

    val M = l.fold(1L, { acc, e -> acc * e.second })

    // See https://de.wikipedia.org/wiki/Chinesischer_Restsatz
    // for details
    return (l.map { elem ->
        val a = elem.first
        val mi = elem.second
        val Mi = M / mi
        val euclid = euclid(mi, Mi)
        val e = euclid.second * Mi
        e * a
    }.sum() % M + M) % M
}
