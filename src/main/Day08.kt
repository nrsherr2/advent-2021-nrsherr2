import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val day8ExampleInput = readInput("Day08_Test")
    assertEquals(26, Day08.part1(day8ExampleInput))
    val day8Input = readInput("Day08_Input")
    
    val timeToExecuteDay8 = measureTimeMillis {
        val part1Output = Day08.part1(day8Input)
        val part2Output = 0
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
    println("Processing time: ${timeToExecuteDay8}ms")
}

object Day08 {
    fun part1(input:List<String>) = input.sumOf { line ->
        line.split(" | ")[1].split(" ").count { signal ->
            signal.length in listOf(2,4,3,7)
        }
    }
}




