import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val day7ExampleInput = readInput("Day07_Test")
    assertEquals(1, day7ExampleInput.size)
    assertEquals(37, Day07.part1(day7ExampleInput.first()))
    assertEquals(168, Day07.part2(day7ExampleInput.first()))
    val day7Input = readInput("Day07_Input")
    assertEquals(1, day7Input.size)
    
    val timeToExecuteDay7 = measureTimeMillis {
        val part1Output = Day07.part1(day7Input.first())
        val part2Output = Day07.part2(day7Input.first())
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
    println("Processing time: ${timeToExecuteDay7}ms")
}

object Day07 {
    fun part1(input: String): Int {
        val listo = input.split(",").map { it.toInt() }
        var minDistance = Int.MAX_VALUE
        var destination = Int.MAX_VALUE
        (0..listo.maxByOrNull { it }!!).toList().forEach { dest ->
            listo.sumOf { distanceToInt(it, dest) }.let {
                if (it < minDistance) {
                    minDistance = it
                    destination = dest
                }
            }
        }
        return minDistance
    }
    
    fun part2(input: String): Int {
        val listo = input.split(",").map { it.toInt() }
        var minDistance = Int.MAX_VALUE
        var destination = Int.MAX_VALUE
        (0..listo.maxByOrNull { it }!!).toList().forEach { dest ->
            listo.sumOf { fuelCostToInt(it, dest) }.let {
                if (it < minDistance) {
                    minDistance = it
                    destination = dest
                }
            }
        }
        return minDistance
    }
    
    private fun distanceToInt(src: Int, dest: Int): Int = abs(src - dest)
    private fun fuelCostToInt(src: Int, dest: Int): Int {
        val distance = distanceToInt(src, dest)
        return (0..distance).sumOf { it }
    }
}




