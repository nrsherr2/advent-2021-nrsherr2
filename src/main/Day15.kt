import kotlin.system.measureTimeMillis

fun main() {
    val day15ExampleInput = readInput("Day15_Test")
    assertEquals(40, Day15.part1(day15ExampleInput))
    assertEquals(315, Day15.part2(day15ExampleInput))
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
        val end = grid[grid.lastIndex][grid.lastIndex]
        return dijkstra(grid.flatten().toSet(), source, end)
    }

    private fun dijkstra(
        grid: Set<Node>,
        source: Node,
        end: Node
    ): Int {
        val unusedNodes = mutableSetOf<Node>()
        val dist = mutableMapOf<Node, Int>()
        val hops = mutableMapOf<Node, Node?>()
        grid.forEach { n ->
            dist[n] = Int.MAX_VALUE
            hops[n] = null
            unusedNodes.add(n)
        }
        dist[source] = 0

        while (unusedNodes.isNotEmpty()) {
            if(unusedNodes.size % 10 == 0) println(unusedNodes.size)
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
        return dist[end]!!
    }

    fun part2(input: List<String>): Int {
        val (source, grid, end) = parseInput2(input)
        return dijkstra(grid, source, end)
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

    private fun parseInput2(input: List<String>): NodeGroup {
        val grid = (0 until 5).flatMap { iFactor ->
            input.map { row ->
                (0 until 5).flatMap { jFactor ->
                    row.map { num -> Node((num.digitToInt() + iFactor + jFactor).let { if (it > 9) it - 9 else it }) }
                }
            }
        }
//        val grid = input.map { it.map { num -> Node(num.digitToInt()) } }
        grid.forEachIndexed { rowNum, nodes ->
            nodes.forEachIndexed { colNum, node ->
                node.neighbors =
                    listOf(rowNum - 1 to colNum, rowNum + 1 to colNum, rowNum to colNum - 1, rowNum to colNum + 1)
                        .filter { it.first in grid.indices && it.second in nodes.indices }
                        .map { grid[it.first][it.second] }
                        .toSet()
            }
        }
        return NodeGroup(grid[0][0], grid.flatten().toSet(), grid.last().last())
    }

    private data class NodeGroup(val start: Node, val nodes: Set<Node>, val end: Node)

    private class Node(val costToEnter: Int) {
        var neighbors: Set<Node> = setOf()
    }

}




