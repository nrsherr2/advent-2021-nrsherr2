import kotlin.math.pow
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
//        val maxStackSize = grid.flatten().size.toDouble() / 4.9
//        val end = grid[grid.lastIndex][grid[0].lastIndex]
//        return listySearch(grid)
        return permySearch(grid)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun permySearch(grid: List<List<Node>>): Int {
        val shortestPathLen = grid.size + grid[0].size - 2
        val numPermutations = 2.0.pow(shortestPathLen).toLong()
        println(numPermutations)

        var leastCost = Int.MAX_VALUE
        for (i in 0..numPermutations) {
            val directions = i.toString(2).padStart(shortestPathLen, '0')
            if (directions.contains(listOfOnes(grid.size - 1)) || directions.contains(listOfZeroes(grid.size - 1))) continue
//            println("$i -> $directions")
            val walkScore = walkThroughGrid(grid, directions)
//            if (walkScore < Int.MAX_VALUE) println("$directions -> $walkScore")
            if (walkScore < leastCost) leastCost = walkScore
        }
        return leastCost
    }

    private fun listOfOnes(size: Int) = List(size) { '1' }.joinToString("")
    private fun listOfZeroes(size: Int) = List(size) { '0' }.joinToString("")

    private fun walkThroughGrid2(grid: List<List<Node>>, directions: Long, pathLen: Int): Int {
        var currentCost = 0
        var rowNum = 0
        var colNum = 0

        if (directions == 197309L)
            println("ok")

        for (i in 0..pathLen) {
            println("$i ${((directions shr i) and 0x1).toString(2)} ${directions.toString(2)}")
            when ((directions shr i) and 0x1) {
                1L -> {
                    rowNum++
                    if (rowNum > grid.lastIndex) return Int.MAX_VALUE
                }
                else -> {
                    colNum++
                    if (colNum > grid[0].lastIndex) return Int.MAX_VALUE
                }
            }
            currentCost += grid[rowNum][colNum].costToEnter
        }
        return currentCost
    }


    private fun walkThroughGrid(grid: List<List<Node>>, directions: String): Int {
        var currentCost = 0
        var rowNum = 0
        var colNum = 0
        directions.forEach {
            when (it) {
                '1' -> {
                    rowNum++
                    if (rowNum > grid.lastIndex) return Int.MAX_VALUE
                }
                else -> {
                    colNum++
                    if (colNum > grid[0].lastIndex) return Int.MAX_VALUE
                }
            }
            currentCost += grid[rowNum][colNum].costToEnter
        }
        if (currentCost == 40) println(directions.toInt(2))
        return currentCost
    }

    private fun listySearch(grid: List<List<Node>>): Int {
        var chains = mutableListOf(listOf(grid[0][0]))
        val end = grid[grid.lastIndex][grid[0].lastIndex]
        var minFinalChainCost = Int.MAX_VALUE
        while (chains.isNotEmpty()) {
            if (chains.map { it.chainCost() }.distinct().size > 10) {
                val avg = chains.map { it.chainCost() }.average() + 5
                chains = chains.filter { it.chainCost() < avg }.toMutableList()
            }
            chains = chains.filter { it.chainCost() < minFinalChainCost }.toMutableList()
            println(chains.size)
            chains.toList().forEach { chain ->
                chains.remove(chain)
                chains.addAll(chain.last().neighbors.filter { it !in chain }.map { newNode ->
                    val newChain = chain + newNode
//                    if(newChain.joinToString(", ") { it.costToEnter.toString() } == "1, 1, 2, 1, 3, 6, 5, 1, 1, 1, 5, 1, 1, 3, 2, 3, 2, 1, 1")
//                        throw IllegalArgumentException()
                    if (newNode == end) {
                        val cost = newChain.chainCost()
                        println("${newChain.map { it.costToEnter }} $cost")
                        if (cost < minFinalChainCost) minFinalChainCost = cost
                    }
                    newChain
                })
            }
        }
        return minFinalChainCost
    }


    private fun recursiveSearch(
        currentMinCost: Int,
        currentChain: List<Node>,
        end: Node,
        currentLevel: Int,
        maxLevel: Int
    ): Int {
        if (currentLevel == maxLevel) return Int.MAX_VALUE
        var mutableMinCost = currentMinCost
        val currentNode = currentChain.last()
        currentNode.neighbors.forEach { neighborNode ->
            val neighborCost = currentChain.chainCost() + neighborNode.costToEnter
            if (neighborNode == end) return neighborCost.also {
                println(
                    "${currentChain.plus(neighborNode).map { it.costToEnter }} $neighborCost"
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
                    listOf(rowNum + 1 to colNum, rowNum to colNum + 1)
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




