import kotlin.system.measureTimeMillis

fun main() {
    val day11ExampleInput = readInput("Day11_Test")
    assertEquals(1656, Day11.part1(day11ExampleInput))
    Day11.part2(day11ExampleInput)
    val day11Input = readInput("Day11_Input")

    val timeToExecuteDay11 = measureTimeMillis {
        val part1Output = Day11.part1(day11Input)
        val part2Output = Day11.part2(day11Input)
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
    println("Processing time: ${timeToExecuteDay11}ms")
}

object Day11 {
    fun part1(input: List<String>): Int {
        val takoGrid = input.map { line -> line.map { Takodachi(it.digitToInt()) } }
        var wah = 0
        (1..100).forEach { i ->
            var points =
                takoGrid.mapIndexed { rowNum, row -> List(row.size) { colNum -> Point(rowNum, colNum) } }.flatten()
            do {
                val newPoints = mutableListOf<Point>()
                points.forEach { (rowNum, colNum) ->
                    if (takoGrid[rowNum][colNum].bonk()) {
                        wah++
                        newPoints.addAll(
                            listOf(
                                Point(rowNum - 1, colNum),
                                Point(rowNum + 1, colNum),
                                Point(rowNum, colNum - 1),
                                Point(rowNum, colNum + 1),
                                Point(rowNum - 1, colNum - 1),
                                Point(rowNum - 1, colNum + 1),
                                Point(rowNum + 1, colNum - 1),
                                Point(rowNum + 1, colNum + 1)
                            ).filter {
                                it.rowNum >= 0 &&
                                        it.rowNum < takoGrid.size &&
                                        it.colNum >= 0 &&
                                        it.colNum < takoGrid[0].size
                            }
                        )
                    }
                }
                points = newPoints
            } while (points.isNotEmpty())
            takoGrid.forEach { row -> row.forEach { it.reset() } }
        }
        return wah
    }

    fun part2(input: List<String>): Int {
        val takoGrid = input.map { line -> line.map { Takodachi(it.digitToInt()) } }
        var wah = 0
        (1..195).forEach { _ ->
            var points =
                takoGrid.mapIndexed { rowNum, row -> List(row.size) { colNum -> Point(rowNum, colNum) } }.flatten()
            do {
                val newPoints = mutableListOf<Point>()
                points.forEach { (rowNum, colNum) ->
                    if (takoGrid[rowNum][colNum].bonk()) {
                        wah++
                        newPoints.addAll(
                            listOf(
                                Point(rowNum - 1, colNum),
                                Point(rowNum + 1, colNum),
                                Point(rowNum, colNum - 1),
                                Point(rowNum, colNum + 1),
                                Point(rowNum - 1, colNum - 1),
                                Point(rowNum - 1, colNum + 1),
                                Point(rowNum + 1, colNum - 1),
                                Point(rowNum + 1, colNum + 1)
                            ).filter {
                                it.rowNum >= 0 &&
                                        it.rowNum < takoGrid.size &&
                                        it.colNum >= 0 &&
                                        it.colNum < takoGrid[0].size
                            }
                        )
                    }
                }
                points = newPoints
            } while (points.isNotEmpty())
            takoGrid.forEach { row -> row.forEach { it.reset() } }
        }
        return wah
    }


    private data class Takodachi(var cookieLevel: Int) {
        fun bonk(): Boolean = ++cookieLevel == 10

        fun cookie() = if (cookieLevel > 9) 0 else cookieLevel

        fun reset() {
            if (cookieLevel > 9) cookieLevel = 0
        }
    }

    private fun List<List<Takodachi>>.unravel() =
        this.joinToString("\n") { ln -> ln.joinToString("") { it.cookie().toString() } }

    private data class Point(val rowNum: Int, val colNum: Int)
}



