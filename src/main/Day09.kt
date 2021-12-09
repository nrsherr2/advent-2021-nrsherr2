import kotlin.system.measureTimeMillis

fun main() {
    val day9ExampleInput = readInput("Day09_Test")
    assertEquals(15, Day09.part1(day9ExampleInput))
    assertEquals(1134, Day09.part2(day9ExampleInput))
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
        val lowPoints = lowPoints(coords)
        return lowPoints.sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        val coords = input.map { it.toCharArray().map { c -> c.digitToInt() } }
        val basins = coords.flatMapIndexed { rowNum, row ->
            List(row.size) { colNum ->
                Basin().growFrom(coords, rowNum, colNum)
            }
        }
        val (b1, b2, b3) = basins.sortedByDescending { it.size }.take(3)
        return b1.size * b2.size * b3.size
    }

    private fun lowPoints(coords: List<List<Int>>) = coords.flatMapIndexed { rowNum, row ->
        row.filterIndexed { colNum, num ->
            listOf(
                rowNum - 1 to colNum,
                rowNum + 1 to colNum,
                rowNum to colNum - 1,
                rowNum to colNum + 1
            ).map { BasinPoint(it.first, it.second, coords.getOrMaxInt(it.first, it.second)) }
                .all { it.value > num }
        }
    }


    private fun List<List<Int>>.getOrMaxInt(row: Int, col: Int): Int {
        return if (!this.indices.contains(row) || !this.first().indices.contains(col)) Int.MAX_VALUE
        else this[row][col]
    }

    class Basin {
        val points: MutableList<BasinPoint> = mutableListOf()
        fun markAllAsRead() = points.forEach { it.newPoint = false }
        fun newPoints() = points.filter { it.newPoint }
        fun growFrom(coords: List<List<Int>>, startingRow: Int, startingCol: Int): Basin {
            val initialValue = coords[startingRow][startingCol]
            if (initialValue == 9) {
                return this
            } else {
                this.points.add(BasinPoint(startingRow, startingCol, initialValue))
            }
            while (this.points.any { it.newPoint }) {
                val newerPoints = newPoints().flatMap { point ->
                    listOf(
                        point.rowNum - 1 to point.colNum,
                        point.rowNum + 1 to point.colNum,
                        point.rowNum to point.colNum - 1,
                        point.rowNum to point.colNum + 1
                    ).mapNotNull { loc ->
                        BasinPoint(
                            loc.first,
                            loc.second,
                            coords.getOrMaxInt(loc.first, loc.second)
                        ).takeIf {
                            val a = it.value > point.value
                            val b = it.value < 9
                            val c = points.none { ex -> it.rowNum == ex.rowNum && it.colNum == ex.colNum }
                            a && b && c
                        }
                    }
                }.distinct()
                markAllAsRead()
                this.points.addAll(newerPoints)
            }
            return this
        }

        val size: Int
            get() = points.size
    }

    data class BasinPoint(val rowNum: Int, val colNum: Int, val value: Int, var newPoint: Boolean = true)
}



