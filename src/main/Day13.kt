import kotlin.system.measureTimeMillis

fun main() {
//    Day13.warn()
    val day13ExampleInput = readInput("Day13_Test")
    assertEquals(17, Day13.part1(day13ExampleInput))
    println(Day13.part2(day13ExampleInput)) //this should be a square
    val day13Input = readInput("Day13_Input")

    val timeToExecuteDay13 = measureTimeMillis {
        val part1Output = Day13.part1(day13Input)
        val part2Output = Day13.part2(day13Input)
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
    println("Processing time: ${timeToExecuteDay13}ms")
}

object Day13 {
    fun part1(input: List<String>): Int {
        val pair = parseInput(input)
        var paper = pair.first
        val moves = pair.second
        paper = paper.foldY(moves.first().degree)
        return paper.sumOf { row -> row.count { it.marked } }
    }

    fun part2(input: List<String>): String {
        val pair = parseInput(input)
        var paper = pair.first
        val moves = pair.second
        moves.forEach {
            paper = when (it.axis) {
                'x' -> paper.foldX(it.degree)
                'y' -> paper.foldY(it.degree)
                else -> throw IllegalArgumentException()
            }
        }
        return paper.stringRep()
    }

    private fun parseInput(input: List<String>): Pair<TransparentPaper, List<Fold>> {
        val dotCoords = input.subList(0, input.indexOf("")).map { s ->
            s.split(",").let { Point(it[1].toInt(), it[0].toInt()) }
        }
        var paper = buildPaper(dotCoords)
        val moves = input.subList(input.indexOf("") + 1, input.size)
            .map { line -> line.takeLastWhile { it != ' ' }.split("=").let { Fold(it[0][0], it[1].toInt()) } }
        return Pair(paper, moves)
    }

    private fun TransparentPaper.foldY(degree: Int): TransparentPaper {
        val page2 = subList(degree + 1, size)
        val page1 = subList(0, degree).let {
            if (it.size >= page2.size) it
            else {
                val difference = page2.size - it.size
                (0 until difference).map { List(page2[0].size) { GridSpot() } } + page2
            }
        }
        page2.indices.forEach { rowNum ->
            page2[0].indices.forEach { colNum ->
                page1[page1.size - 1 - rowNum][colNum].marked =
                    page1[page1.size - 1 - rowNum][colNum].marked || page2[rowNum][colNum].marked
            }
        }
        return page1
    }

    fun TransparentPaper.foldX(degree: Int): TransparentPaper {
        val a = size
        val b = get(0).size
        val rightSide = map { it.subList(degree + 1, it.size) }
        val leftSide = map { it.subList(0, degree) }.let {
            if (it.size >= rightSide.size) it
            else {
                val difference = rightSide.size - it.size
                it.map { ln ->
                    val m = ln.toMutableList()
                    m.addAll(0, List(difference) { GridSpot() })
                    m.toList()
                }
            }
        }
        rightSide.indices.forEach { rowNum ->
            rightSide[0].indices.forEach { colNum ->
                leftSide[rowNum][leftSide[0].size - 1 - colNum].marked =
                    leftSide[rowNum][leftSide[0].size - 1 - colNum].marked || rightSide[rowNum][colNum].marked
            }
        }
        return leftSide
    }

    private fun TransparentPaper.stringRep() =
        this.joinToString("\n") { ln -> ln.joinToString("") { if (it.marked) "# " else "  " } }.plus("\n")

    private fun buildPaper(coords: List<Point>): TransparentPaper =
        (0..coords.maxOf { it.rowNum }).map { List(coords.maxOf { it.colNum } + 1) { GridSpot() } }.apply {
            coords.forEach { this[it.rowNum][it.colNum].marked = true }
        }


    data class GridSpot(var marked: Boolean = false)
    data class Fold(val axis: Char, val degree: Int)
}

typealias TransparentPaper = List<List<Day13.GridSpot>>



