import kotlin.system.measureTimeMillis

fun main() {
    val day18ExampleInput = readInput("Day18_Test")
    assertEquals(45, Day18.part1(day18ExampleInput))
    assertEquals(112, Day18.part2(day18ExampleInput))
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
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
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
}



