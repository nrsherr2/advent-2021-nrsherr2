import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val day5ExampleInput = readInput("Day05_Test")
    val day5Input = readInput("Day05_Input")
    val day5Part1ExampleOutput = Day05.part1(Day05.processLines(day5ExampleInput))
    assertEquals(5, day5Part1ExampleOutput)
    val day5Part2ExampleOutput = Day05.part2(Day05.processLines(day5ExampleInput))
    assertEquals(12,day5Part2ExampleOutput)
    
    val timeToExecuteDay5 = measureTimeMillis {
        val processedLines = Day05.processLines(day5Input)
        val part1Output = Day05.part1(processedLines)
        val part2Output = Day05.part2(processedLines)
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
    println("Processing time: ${timeToExecuteDay5}ms")
}

object Day05 {
    fun processLines(lines: List<String>) = lines.map { line ->
        val (onePart, twoPart) = line.split("->").onEach { it.trim() }
        val (x1, y1) = extractXY(onePart)
        val (x2, y2) = extractXY(twoPart)
        return@map VentLine(x1, x2, y1, y2)
    }
    
    fun part1(susVents: List<VentLine>): Int {
        val oceanFloor = initOceanFloor(susVents)
        susVents.forEach { oceanFloor.drawVentLine(it) }
        return oceanFloor.flatten().count { it.isDangerous }
    }
    
    fun part2(susVents: List<VentLine>): Int {
        val oceanFloor = initOceanFloor(susVents)
        susVents.forEach { oceanFloor.drawVentLine(it, true) }
        return oceanFloor.flatten().count { it.isDangerous }
    }
    
    private fun initOceanFloor(susVents: List<VentLine>) = extractMaxDegree(susVents).let { maxSize ->
        Array(maxSize) { Array(maxSize) { OceanFloorTile() } }
    }
    
    private fun extractMaxDegree(susVents: List<VentLine>) = susVents.maxOf { it.maxDegree }
    
    private fun extractXY(string: String) = string.split(",").map { it.trim().toInt() }.let { it.first() to it[1] }
    
    private fun Array<Array<OceanFloorTile>>.drawVentLine(ventLine: VentLine, isPart2: Boolean = false) {
        when {
            ventLine.x1 != ventLine.x2 && ventLine.y1 == ventLine.y2 -> {
                val max = max(ventLine.x1, ventLine.x2)
                val min = min(ventLine.x1, ventLine.x2)
                (min..max).forEach { this[ventLine.y1][it].placeVent() }
            }
            ventLine.y1 != ventLine.y2 && ventLine.x1 == ventLine.x2 -> {
                val max = max(ventLine.y1, ventLine.y2)
                val min = min(ventLine.y1, ventLine.y2)
                (min..max).forEach { this[it][ventLine.x1].placeVent() }
            }
            else -> {
                if (isPart2) {
                    calculateDiagonalLine(ventLine).forEach { (x, y) ->
                        this[y][x].placeVent()
                    }
                }
            }
        }
    }
    
    private fun calculateDiagonalLine(line: VentLine): List<Pair<Int, Int>> {
        val directionXIsPositive = line.x1 < line.x2
        val directionYIsPositive = line.y1 < line.y2
        val point = Point(line.x1, line.y1)
        val points = mutableListOf(point.x to point.y)
        while (point.x != line.x2) {
            if (directionXIsPositive) point.x += 1 else point.x -= 1
            if (directionYIsPositive) point.y += 1 else point.y -= 1
            points.add(point.x to point.y)
        }
        return points
    }
    
    private data class Point(var x: Int, var y: Int)
    
    private fun Array<Array<OceanFloorTile>>.stringRepresentation() = this.joinToString("\n") {
        it.joinToString(" ") { oft -> if (oft.numVents == 0) "." else oft.numVents.toString() }
    }
}

data class VentLine(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
    val maxDegree: Int
        get() = maxOf(x1, x2, y1, y2) + 1
}

class OceanFloorTile {
    var numVents: Int = 0
    val isDangerous: Boolean
        get() = numVents >= 2
    
    fun placeVent() {
        numVents += 1
    }
}
