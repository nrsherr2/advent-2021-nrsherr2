import kotlin.system.measureTimeMillis

fun main() {
    val day25ExampleInput = readInput("Day25_Test")
    assertEquals(58, Day25.part1(day25ExampleInput))
    val day25Input = readInput("Day25_Input")

    val timeToExecuteDay25 = measureTimeMillis {
        val part1Output = Day25.part1(day25Input)
        val part2Output = Day25.part2(day25Input)
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
    println("Processing time: ${timeToExecuteDay25}ms")
}

object Day25 {
    fun part1(input: List<String>): Int {
        val game = parseInput(input)
        var nobodyMoved = false
        var step = 0
        println("Initial state:\n${game.stringRep()}")
        while (!nobodyMoved) {
            step++
            nobodyMoved = true
            val east = game.eastCukes.toMutableList()
            val newEast = mutableListOf<Point>()
            while (east.isNotEmpty()) {
                val c = east.removeAt(0)
                val nextCol = (c.colNum + 1).takeIf { it < game.numCols } ?: 0
                val newPoint = Point(c.rowNum, nextCol)
                if (newPoint in game.eastCukes || newPoint in game.southCukes) {
                    newEast.add(c)
                } else {
                    nobodyMoved = false
                    newEast.add(newPoint)
                }
            }
            game.eastCukes = newEast
            val south = game.southCukes.toMutableList()
            val newSouth = mutableListOf<Point>()
            while (south.isNotEmpty()) {
                val c = south.removeAt(0)
                val nextRow = (c.rowNum + 1).takeIf { it < game.numRows } ?: 0
                val newPoint = Point(nextRow, c.colNum)
                if (newPoint in game.eastCukes || newPoint in game.southCukes) {
                    newSouth.add(c)
                } else {
                    nobodyMoved = false
                    newSouth.add(newPoint)
                }
            }
            game.southCukes = newSouth
            if(step % 20 == 0){
                println("...")
                println("After $step steps:")
                println(game.stringRep())
            }
        }
        return step
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    fun parseInput(input: List<String>): Game {
        val eastCukes = mutableListOf<Point>()
        val southCukes = mutableListOf<Point>()
        input.forEachIndexed { rowNum, row ->
            row.forEachIndexed { colNum, c ->
                if (c == '>') eastCukes.add(Point(rowNum, colNum))
                if (c == 'v') southCukes.add(Point(rowNum, colNum))
            }
        }
        return Game(input.size, input[0].length, eastCukes, southCukes)
    }

    data class Game(val numRows: Int, val numCols: Int, var eastCukes: List<Point>, var southCukes: List<Point>) {
        fun stringRep() = List(numRows) { rn ->
            List(numCols) { cn ->
                val pt = Point(rn, cn)
                if (pt in eastCukes) '>'
                else if (pt in southCukes) 'v'
                else '.'
            }.joinToString("")
        }.joinToString("\n")
    }
}



