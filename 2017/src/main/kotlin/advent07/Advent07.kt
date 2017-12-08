package advent07

import java.io.File

data class Node(val name: String, val weight: Int, val children: List<String>)

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/advent07/input.txt").readLines().map { it.parseAsNode() }
    println("Result part 1: ${findRoot(input)!!.name}")
    val inputAsMap = input.map { it.name to it }.toMap()
    println("Result part 2: ${findRequiredWeightOfUnbalancedChild(findUnbalanced(inputAsMap)!!, inputAsMap)}")
}

fun findRoot(nodes: Collection<Node>): Node? {
    // root is the only node that's not a child
    val children = nodes.flatMap { it.children }.toSet()
    return nodes.find { !children.contains(it.name) }
}

fun totalWeight(node: Node, nodes: Map<String, Node>): Int =
        node.weight + node.children.map { totalWeight(nodes[it]!!, nodes) }.sum()

fun findUnbalanced(nodes: Map<String, Node>): Node? =
        nodes.values.filter { it.children.map { totalWeight(nodes[it]!!, nodes) }.toSet().size > 1 }.minBy { totalWeight(it, nodes) }

fun findRequiredWeightOfUnbalancedChild(node: Node, nodes: Map<String, Node>): Int {
    val groupedByTotalWeight = node.children.map { nodes[it]!! }.groupBy { totalWeight(it, nodes) }
    val requiredTotalWeight = groupedByTotalWeight.entries.find { it.value.size > 1 }!!.key
    val offChild = groupedByTotalWeight.entries.find { it.value.size == 1 }!!
    val realTotalWeight = offChild.key
    return requiredTotalWeight - realTotalWeight + offChild.value.single().weight
}

// Example string:
// etwpt (59) -> cejnfcx, oxzmr, sysvory
fun String.parseAsNode(): Node {
    val matches = "(\\w+)\\s*\\((\\d+)\\)\\W*(.*)".toRegex().matchEntire(this)!!
    val name = matches.groupValues[1]
    val weight = matches.groupValues[2].toInt()
    val childrenString = matches.groupValues.getOrElse(3, { "" })
    val children = childrenString.split(",\\s+".toRegex()).filter { it.isNotEmpty() }
    return Node(name, weight, children)
}
