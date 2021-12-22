import kotlin.math.ceil
import kotlin.math.floor
import kotlin.system.measureTimeMillis

fun main() {
//    val day18t2ExampleInput = readInput("Day18t2_Test")
    assertEquals(4140L, Day18t2.part1(Day18t2.exampleInput))
    assertEquals(3993L, Day18t2.part2(Day18t2.exampleInput))
//    assertEquals(112, Day18t2.part2(day18t2ExampleInput))
    val day18t2Input = readInput("Day18_Input")

    val timeToExecuteDay18t2 = measureTimeMillis {
        val part1Output = Day18t2.part1(day18t2Input)
        val part2Output = Day18t2.part2(day18t2Input)
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
    println("Processing time: ${timeToExecuteDay18t2}ms")
}

object Day18t2 {
    fun part1(input: List<String>): Long {
        val heads = input.map { parseInput(it).first }
        heads.forEach { println(it.toString()) }
        val result = heads.reduce { acc, newLine -> cleanUp(acc + newLine) }
        return result.magnitude()
    }

    fun part2(input: List<String>): Long {
        val allPerms = permutations(input, 2)

        return allPerms.maxOf {
            val res = cleanUp(parseInput(it[0]).first + parseInput(it[1]).first)
            println("${it[0]} + ${it[1]} -> $res (${res.magnitude()})")
            res.magnitude()
        }
    }

    private fun cleanUp(cHead: SnailfishNumber): SnailfishNumber {
        while (true) run {
            explodingRecursiveSearch(cHead)?.let {
                explode(it)
                return@run
            }
            splittingRecursiveSearch(cHead)?.let {
                split(it)
                return@run
            }
            return cHead
        }
    }

    private fun explodingRecursiveSearch(cHead: SnailfishNumber, depth: Int = 1): SnailfishNumber.NumPair? =
        when (cHead) {
            is SnailfishNumber.NumPair -> when (depth) {
                5 -> cHead
                else -> explodingRecursiveSearch(cHead.left, depth + 1)
                    ?: explodingRecursiveSearch(cHead.right, depth + 1)
            }
            is SnailfishNumber.Reg -> null
        }

    private fun splittingRecursiveSearch(cHead: SnailfishNumber): SnailfishNumber.Reg? = when (cHead) {
        is SnailfishNumber.NumPair -> splittingRecursiveSearch(cHead.left) ?: splittingRecursiveSearch(cHead.right)
        is SnailfishNumber.Reg -> cHead.takeIf { it.value > 9 }
    }

    private fun parseInput(line: String, pointer: Int = 0): Pair<SnailfishNumber, Int> {
        var pt = pointer
        val c = line[pt].also { pt++ }
        return if (c == '[') {
            val (left, pl) = parseInput(line, pt)
            pt = pl
            assertEquals(',', line[pt]).also { pt++ }
            val (right, pr) = parseInput(line, pt)
            pt = pr
            val node = SnailfishNumber.NumPair(left, right)
            assertEquals(']', line[pt].also { pt++ })
            node to pt
        } else if (c.isDigit()) {
            val node = SnailfishNumber.Reg(c.digitToInt())
            node to pt
        } else {
            throw IllegalArgumentException()
        }
    }


    sealed class SnailfishNumber(var parent: NumPair? = null) {
        operator fun plus(other: SnailfishNumber): SnailfishNumber {
            return NumPair(this, other)
        }

        data class NumPair(var left: SnailfishNumber, var right: SnailfishNumber) : SnailfishNumber() {
            init {
                left.parent = this
                right.parent = this
            }

            fun replace(old: SnailfishNumber, new: SnailfishNumber) {
                if (old === left) left = new else right = new
                new.parent = this
            }

            override fun toString() = "[$left,$right]"
        }

        data class Reg(var value: Int = 0) : SnailfishNumber() {
            override fun toString() = value.toString()
        }

        fun magnitude(): Long = when (this) {
            is Reg -> value.toLong()
            is NumPair -> left.magnitude() * 3 + right.magnitude() * 2
        }
    }

    private fun rightMost(num: SnailfishNumber): SnailfishNumber.Reg {
        return when (num) {
            is SnailfishNumber.Reg -> num
            is SnailfishNumber.NumPair -> rightMost(num.right)
        }
    }

    private fun leftMost(num: SnailfishNumber): SnailfishNumber.Reg = when (num) {
        is SnailfishNumber.Reg -> num
        is SnailfishNumber.NumPair -> leftMost(num.left)
    }

    private fun firstNonSideParent(
        num: SnailfishNumber,
        side: SnailfishNumber.NumPair.() -> SnailfishNumber
    ): SnailfishNumber.NumPair? {
        var current = num

        while (current.parent != null) {
            if (current.parent!!.side() !== current) {
                return current.parent
            } else {
                current = current.parent!!
            }
        }

        return null
    }

    private fun explode(num: SnailfishNumber.NumPair) {
        firstNonSideParent(num, SnailfishNumber.NumPair::left)?.let { rightMost(it.left) }?.apply {
            value += (num.left as SnailfishNumber.Reg).value
        }
        firstNonSideParent(num, SnailfishNumber.NumPair::right)?.let { leftMost(it.right) }?.apply {
            value += (num.right as SnailfishNumber.Reg).value
        }

        num.parent?.replace(num, SnailfishNumber.Reg())
    }

    fun split(num: SnailfishNumber.Reg) {
        val l = floor(num.value / 2.0).toInt()
        val r = ceil(num.value / 2.0).toInt()
        val newp = SnailfishNumber.NumPair(SnailfishNumber.Reg(l), SnailfishNumber.Reg(r))
        num.parent?.replace(num, newp)
    }

    val exampleInput =
        """
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
        """.trimIndent().split("\n")

    fun <E> permutations(list: List<E>, length: Int? = null): Sequence<List<E>> = sequence {
        val n = list.size
        val r = length ?: list.size

        val indices = list.indices.toMutableList()
        val cycles = (n downTo (n - r)).toMutableList()
        yield(indices.take(r).map { list[it] })

        while (true) {
            var broke = false
            for (i in (r - 1) downTo 0) {
                cycles[i]--
                if (cycles[i] == 0) {
                    val end = indices[i]
                    for (j in i until indices.size - 1) {
                        indices[j] = indices[j + 1]
                    }
                    indices[indices.size - 1] = end
                    cycles[i] = n - i
                } else {
                    val j = cycles[i]
                    val tmp = indices[i]
                    indices[i] = indices[-j + indices.size]
                    indices[-j + indices.size] = tmp
                    yield(indices.take(r).map { list[it] })
                    broke = true
                    break
                }
            }
            if (!broke) {
                break
            }
        }
    }

}



