import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val day15ExampleInput = readInput("Day15_Test")
    assertEquals(40, Day15.part1(day15ExampleInput))
    val day15Input = readInput("Day15_Input")

    val timeToExecuteDay15 = measureTimeMillis {
        val part1Output = Day15.part1(day15Input)
        val part2Output = Day15.part2(day15Input)
        println(
            """
                *** PART 1 ***
                $part1Output
                *** PART 2 ***
                $part2Output
                ***  END  ***
            """.trimIndent()
        )
    }
    println("Processing time: ${timeToExecuteDay15}ms")
}

object Day15 {
    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        val source = grid[0][0]
        val unusedNodes = mutableSetOf<Node>()
        val dist = mutableMapOf<Node, Int>()
        val hops = mutableMapOf<Node, Node?>()
        grid.flatten().forEach { n ->
            dist[n] = Int.MAX_VALUE
            hops[n] = null
            unusedNodes.add(n)
        }
        dist[source] = 0

        while (unusedNodes.isNotEmpty()) {
            val minDist = dist.filter { it.key in unusedNodes }.minByOrNull { it.value }!!.key
            unusedNodes.remove(minDist)
            val unusedNeighbors = minDist.neighbors.filter { it in unusedNodes }
            unusedNeighbors.forEach {
                val alt = dist[minDist]!! + it.costToEnter
                if (alt < dist[it]!!) {
                    dist[it] = alt
                    hops[it] = minDist
                }
            }
        }
        val end = grid[grid.lastIndex][grid.lastIndex]
        return dist[end]!!
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun parseInput(input: List<String>): List<List<Node>> {
        val grid = input.map { it.map { num -> Node(num.digitToInt()) } }
        grid.forEachIndexed { rowNum, nodes ->
            nodes.forEachIndexed { colNum, node ->
                node.neighbors =
                    listOf(rowNum - 1 to colNum, rowNum + 1 to colNum, rowNum to colNum - 1, rowNum to colNum + 1)
                        .filter { it.first in grid.indices && it.second in nodes.indices }
                        .map { grid[it.first][it.second] }
                        .toSet()
            }
        }
        return grid
    }

    private class Node(val costToEnter: Int) {
        var neighbors: Set<Node> = setOf()
    }

}




