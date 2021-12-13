import kotlin.system.measureTimeMillis

fun main() {
    val day12ExampleInput = readInput("Day12_Test")
    assertEquals(10, Day12.part1(Day12.testInputA.split("\n")))
    assertEquals(19, Day12.part1(Day12.testInputB.split("\n")))
    assertEquals(226, Day12.part1(day12ExampleInput))
//    assertEquals(195, Day12.part2(day12ExampleInput))
    Day12.part2(day12ExampleInput)
    val day12Input = readInput("Day12_Input")

    val timeToExecuteDay12 = measureTimeMillis {
        val part1Output = Day12.part1(day12Input)
        val part2Output = Day12.part2(day12Input)
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
    println("Processing time: ${timeToExecuteDay12}ms")
}

object Day12 {
    fun part1(input: List<String>): Int {
        val mapHead = constructMap(input)
        val initialPath = Path(mapHead)
        val allPaths = pathFindRecurse(initialPath)
        allPaths.forEach { path -> println(path.path.joinToString(",") { it.name }) }
        return allPaths.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    private fun pathFindRecurse(currentPath: Path): List<Path> {
        return currentPath.path.last().neighborNodes.mapNotNull { nextNode ->
            if (nextNode.name == "end") listOf(Path(currentPath, nextNode))
            else if (!nextNode.isBig && currentPath.path.contains(nextNode)) return@mapNotNull null
            else pathFindRecurse(Path(currentPath, nextNode))
        }.flatten()
    }

    private fun constructMap(input: List<String>): Node {
        val nodes = mutableSetOf<Node>()
        input.forEach { line ->
            val (nodeA, nodeB) = line.split("-").map { nodes.findOrAddByName(it) }
            nodeA.neighborNodes.add(nodeB)
            nodeB.neighborNodes.add(nodeA)
        }
        require(nodes.any { it.name == "start" } && nodes.any { it.name == "end" })
        return nodes.find { it.name == "start" }!!
    }

    class Path {
        constructor(initialNode: Node) {
            path = mutableListOf(initialNode)
        }

        constructor(currentPath: Path, newNode: Node) {
            path = (currentPath.path + newNode).toMutableList()
        }

        val path: MutableList<Node>
    }

    data class Node(val name: String) {
        val isBig
            get() = name.all { it.isUpperCase() }
        val neighborNodes = mutableSetOf<Node>()
    }

    private fun MutableSet<Node>.findOrAddByName(name: String): Node {
        this.find { it.name == name }?.let { return it }
        val newNode = Node(name)
        this.add(newNode)
        return newNode
    }

    val testInputA =
        """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent()
    val testInputB =
        """
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
        """.trimIndent()
}



