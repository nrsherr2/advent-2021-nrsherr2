import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    val day20ExampleInput = readInput("Day20_Test")
    assertEquals(35, Day20.part1(day20ExampleInput))
    assertEquals(3351, Day20.part2(day20ExampleInput))
    val day20Input = readInput("Day20_Input")

    val timeToExecuteDay20 = measureTimeMillis {
        val part1Output = Day20.part1(day20Input)
        val part2Output = Day20.part2(day20Input)
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
    println("Processing time: ${timeToExecuteDay20}ms")
}

object Day20 {
    fun part1(input: List<String>): Int {
        val (image, algo) = parseInput(input)
        val expansionMult = 1000 - max(image.size, image[0].size)
        val exp = List(expansionMult) { List(image[0].size + (expansionMult * 2)) { "░" }.joinToString("") } +
                image.map { line ->
                    listOf(
                        (List(expansionMult) { "░" }).joinToString(""),
                        line.joinToString(""),
                        (List(expansionMult) { "░" }).joinToString(""),
                    ).joinToString("")
                } +
                List(expansionMult) { List(image[0].size + (expansionMult * 2)) { "░" }.joinToString("") }
        val base = exp.map { it.toCharArray().toTypedArray() }.toTypedArray()
        val a = GoLStep(GoLStep(base, algo), algo)
        return a.flatten().count { it == '▓' }
    }

    fun part2(input: List<String>): Int {
        val (image, algo) = parseInput(input)
        val expansionMult = 1000 - max(image.size, image[0].size)
        val exp = List(expansionMult) { List(image[0].size + (expansionMult * 2)) { "░" }.joinToString("") } +
                image.map { line ->
                    listOf(
                        (List(expansionMult) { "░" }).joinToString(""),
                        line.joinToString(""),
                        (List(expansionMult) { "░" }).joinToString(""),
                    ).joinToString("")
                } +
                List(expansionMult) { List(image[0].size + (expansionMult * 2)) { "░" }.joinToString("") }
        var base = exp.map { it.toCharArray().toTypedArray() }.toTypedArray()
        for (i in 0 until 50) {
            base = GoLStep(base, algo)
        }
        return base.flatten().count { it == '▓' }
    }

    private fun GoLStep(base: Array<Array<Char>>, algo: GoLAlgo): Array<Array<Char>> {

        val copy = base.map { it.toCharArray().toTypedArray() }.toTypedArray()
        for (rowNum in 0..base.lastIndex) {
            for (colNum in 0..base[0].lastIndex) {
                val window = listOf(
                    base.getOrBlank(rowNum - 1, colNum - 1),
                    base.getOrBlank(rowNum - 1, colNum),
                    base.getOrBlank(rowNum - 1, colNum + 1),
                    base.getOrBlank(rowNum, colNum - 1),
                    base.getOrBlank(rowNum, colNum),
                    base.getOrBlank(rowNum, colNum + 1),
                    base.getOrBlank(rowNum + 1, colNum - 1),
                    base.getOrBlank(rowNum + 1, colNum),
                    base.getOrBlank(rowNum + 1, colNum + 1),
                ).joinToString("") { if (it == '░') "0" else "1" }
                copy[rowNum][colNum] = algo.calculate(window)
            }
        }
        return copy
    }

    private fun Array<Array<Char>>.getOrBlank(rowNum: Int, colNum: Int): Char {
        return if (rowNum in this.indices && colNum in this.indices) this[rowNum][colNum] else this[0][0]
    }

    private fun parseInput(input: List<String>): Pair<Array<Array<Char>>, GoLAlgo> {
        val algo = GoLAlgo(input.first().map { if (it == '.') '░' else '▓' }.joinToString(""))
        val imageRaw = input.subList(2, input.size)
        val image: Array<Array<Char>> = imageRaw.map { line ->
            line.map {
                when (it) {
                    '#' -> '▓'
                    '.' -> '░'
                    else -> throw IllegalArgumentException()
                }
            }.toTypedArray()
        }.toTypedArray()
        return image to algo
    }

    data class GoLAlgo(var input: String) {
        fun calculate(binaryString: String): Char = input[binaryString.toInt(2)]
    }
}



