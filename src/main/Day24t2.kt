import kotlin.system.measureTimeMillis

fun main() {
    val day24Input = readInput("Day24_Input")

    val timeToExecuteDay24 = measureTimeMillis {
        val part1Output = Day24t2.part1(day24Input)
        val part2Output = Day24t2.part2(day24Input)
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
    println("Processing time: ${timeToExecuteDay24}ms")
}

/**
 * https://github.com/dphilipson/advent-of-code-2021/blob/master/src/days/day24.rs
 */
object Day24t2 {
    fun part1(input: List<String>): Long {
        val conditions = comparators(input)
        val maxes = conditions.mapNotNull { cmp ->
            val pots = (1..9).mapNotNull { lhs ->
                val rhs = lhs - cmp.checkOff
                if (rhs !in 1..9) null
                else IndexValue(cmp.idxLhs, lhs, cmp.idxRhs, rhs)
            }
            pots.maxByOrNull { it.value1 + it.value2 }
        }
        return finalNumber(maxes)
    }

    fun part2(input: List<String>): Long {
        val conditions = comparators(input)
        val mins = conditions.mapNotNull { cmp ->
            val pots = (1..9).mapNotNull { lhs ->
                val rhs = lhs - cmp.checkOff
                if (rhs !in 1..9) null
                else IndexValue(cmp.idxLhs, lhs, cmp.idxRhs, rhs)
            }
            pots.minByOrNull { it.value1 + it.value2 }
        }
        return finalNumber(mins)
    }

    private fun finalNumber(
        numIndexes: List<IndexValue>
    ): Long {
        val inOrder =
            numIndexes.flatMap { listOf(Pair(it.index1, it.value1), Pair(it.index2, it.value2)) }.sortedBy { it.first }
        val finalNumber = inOrder.joinToString("") { it.second.toString() }
        return finalNumber.toLong()
    }

    private fun comparators(input: List<String>): MutableList<Comparator> {
        val chunks = input.chunked(18)
        val stack = mutableListOf<ComparatorStep>()
        val conditions = mutableListOf<Comparator>()
        chunks.forEachIndexed { index, strings ->
            val check = strings[5].split(" ").last().toInt()
            val offset = strings[15].split(" ").last().toInt()

            if (check >= 0) stack.add(ComparatorStep(index, offset))
            else {
                val lasto = stack.removeAt(stack.lastIndex)
                conditions.add(Comparator(index, lasto.index, lasto.offset + check))
            }
        }
        return conditions
    }

    data class IndexValue(val index1: Int, val value1: Int, val index2: Int, val value2: Int)
    data class ComparatorStep(val index: Int, val offset: Int)
    data class Comparator(val idxLhs: Int, val idxRhs: Int, val checkOff: Int)
}



