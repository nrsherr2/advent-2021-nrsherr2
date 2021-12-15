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
        val maxStackSize = grid.flatten().size / 2
        val end = grid[grid.lastIndex][grid[0].lastIndex]
        return recursiveSearch(Int.MAX_VALUE, listOf(grid[0][0]), end, 0, maxStackSize)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun recursiveSearch(
        currentMinCost: Int,
        currentChain: List<Node>,
        end: Node,
        currentLevel: Int,
        maxLevel: Int
    ): Int {
        if (currentLevel == maxLevel) return Int.MAX_VALUE.also { println("too far!") }
        var mutableMinCost = currentMinCost
        val currentNode = currentChain.last()
        currentNode.neighbors.forEach { neighborNode ->
            val neighborCost = currentChain.chainCost() + neighborNode.costToEnter
            if (neighborNode == end) return neighborCost.also {
                println(
                    "${
                        currentChain.plus(neighborNode).map { it.costToEnter }
                    } $neighborCost"
                )
            }
            if (neighborCost > mutableMinCost || currentChain.contains(neighborNode)) return@forEach
            val curseDown =
                recursiveSearch(mutableMinCost, currentChain + neighborNode, end, currentLevel + 1, maxLevel)
            if (curseDown < mutableMinCost) mutableMinCost = curseDown
        }
        return mutableMinCost
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

    private fun List<Node>.chainCost() = if (this.size <= 1) 0 else this.subList(1, size).sumOf { it.costToEnter }
}




