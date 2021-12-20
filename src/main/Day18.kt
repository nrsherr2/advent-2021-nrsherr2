import kotlin.system.measureTimeMillis

fun main() {
//    val day18ExampleInput = readInput("Day18_Test")
//    assertEquals(45, Day18.part1(day18ExampleInput))
//    assertEquals(112, Day18.part2(day18ExampleInput))
    Day18.inputs.forEach { Day18.part1(it) }
    val day18Input = readInput("Day18_Input")

    val timeToExecuteDay18 = measureTimeMillis {
        val part1Output = Day18.part1(day18Input)
        val part2Output = Day18.part2(day18Input)
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
    println("Processing time: ${timeToExecuteDay18}ms")
}

object Day18 {
    fun part1(input: List<String>): Int {
        input.forEach {
            val inp = parseInput(it)
            println(inp.first.toString())
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    fun parseInput(line: String, pointer: Int = 0, parent: BinTreeNode? = null): Pair<BinTreeNode, Int> {
        var pt = pointer
        val c = line[pt].also { pt++ }
        if (c == '[') {
            val node = BinTreeNode(parent)
            val (left, pl) = parseInput(line, pt, node)
            pt = pl
            node.left = left
            assertEquals(',', line[pt]).also { pt++ }
            val (right, pr) = parseInput(line, pt, node)
            pt = pr
            node.right = right
            assertEquals(']', line[pt].also { pt++ })
            return node to pt
        } else if (c.isDigit()) {
            val node = BinTreeNode(parent, c.digitToInt())
            return node to pt
        } else {
            throw IllegalArgumentException()
        }
    }

    class BinTreeNode(var parent: BinTreeNode? = null, var value: Int? = null) {

        var left: BinTreeNode? = null
        var right: BinTreeNode? = null

        override fun toString(): String {
            return if (value != null) value.toString()
            else "[${left.toString()},${right.toString()}]"
        }

        fun numbersOnlyPair(): Boolean = left?.value != null && right?.value != null

        fun split() {
            val l = value!! / 2
            val r = value!! - l
            value = null
            left = BinTreeNode(this, l)
            right = BinTreeNode(this, r)
        }

        fun explode() {
            firstLeft()?.let { it.value = it.value?.plus(left!!.value!!) }
            firstRight()?.let { it.value = it.value?.plus(right!!.value!!) }
            right = null
            left = null
            value = 0
        }

        fun fourDeep(curDepth: Int = 0): Boolean {
            return if (curDepth >= 4) true
            else {
                if (parent == null) false
                else parent!!.fourDeep(curDepth + 1)
            }
        }

        fun firstLeft(caller: BinTreeNode? = null): BinTreeNode? {
            return if (caller != null) {
                if (caller == left) {
                    if (parent == null) BinTreeNode(null, 0)
                    else parent!!.firstLeft(this)
                } else {
                    left!!.rightMost()
                }
            } else {
                if (parent == null) throw IllegalArgumentException()
                else parent!!.firstLeft(this)
            }
        }

        fun firstRight(caller: BinTreeNode? = null): BinTreeNode? {
            return if (caller != null) {
                if (caller == right) {
                    if (parent == null) BinTreeNode(null, 0)
                    else parent!!.firstRight(this)
                } else {
                    right!!.leftMost()
                }
            } else {
                if (parent == null) throw IllegalArgumentException()
                else parent!!.firstRight(this)
            }
        }

        fun leftMost() = if (value != null) this else left
        fun rightMost() = if (value != null) this else right
    }

    val inputs = listOf(
        """
            [[[[4,3],4],4],[7,[[8,4],9]]]
            [1,1]
        """.trimIndent(),
        """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
        """.trimIndent(),
        """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
            [5,5]
        """.trimIndent(),
        """
            [1,1]
            [2,2]
            [3,3]
            [4,4]
            [5,5]
            [6,6]
        """.trimIndent(),
        """
            [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
            [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
            [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
            [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
            [7,[5,[[3,8],[1,4]]]]
            [[2,[2,2]],[8,[8,1]]]
            [2,9]
            [1,[[[9,3],9],[[9,0],[0,7]]]]
            [[[5,[7,4]],7],1]
            [[[[4,2],2],6],[8,7]]
        """.trimIndent()
    ).map { it.split("\n") }
}

