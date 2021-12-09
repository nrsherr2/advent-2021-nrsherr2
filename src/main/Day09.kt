import kotlin.system.measureTimeMillis

fun main() {
    val day9ExampleInput = readInput("Day09_Test")
    assertEquals(15, Day09.part1(day9ExampleInput))

    val day9Input = readInput("Day09_Input")

    val timeToExecuteDay9 = measureTimeMillis {
        val part1Output = Day09.part1(day9Input)
        val part2Output = Day09.part2(day9Input)
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
    println("Processing time: ${timeToExecuteDay9}ms")
}

object Day09 {
    fun part1(input: List<String>): Int {
        val coords = input.map { it.toCharArray().map { c -> c.digitToInt() } }
        val lowPoints = coords.flatMapIndexed { rowNum, row ->
            row.filterIndexed { colNum, num ->
                listOf(
                    rowNum - 1 to colNum,
                    rowNum + 1 to colNum,
                    rowNum to colNum - 1,
                    rowNum to colNum + 1
                ).map { coords.getOrMaxInt(it.first, it.second) }
                    .all { it > num }
            }
        }
        return lowPoints.sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        val coords = input.map { it.toCharArray().map { c -> c.digitToInt() } }
        return 0
    }

    private fun List<List<Int>>.getOrMaxInt(row: Int, col: Int): Int {
        return if (!this.indices.contains(row) || !this.first().indices.contains(col)) Int.MAX_VALUE
        else this[row][col]
    }
}



