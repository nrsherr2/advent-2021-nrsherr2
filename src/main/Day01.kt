import kotlin.system.measureTimeMillis

fun main() {
    val timeToExecuteDay1 = measureTimeMillis {
        with(Day01()) {
            //given the example for part 1, ensure we get the same result they do
            val day1ExampleInput = readInput("Day01_Test")
            val day1Input = readInput("Day01_Input")
            
            val part1ExampleOutput = part1(day1ExampleInput)
            assertEquals(part1ExampleOutput, 7)
            
            val part1Output = part1(day1Input)
            
            val part2ExampleOutput = part2(day1ExampleInput)
            assertEquals(part2ExampleOutput, 5)
            
            val part2Output = part2(day1Input)
            
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
    }
    println("Processing time: ${timeToExecuteDay1}ms")
}

class Day01 {
    fun part1(input: List<String>): Int {
        val depthReadings = input.map { it.toInt() }
        return depthReadings.filterIndexed { index, depth ->
            if (index == 0) return@filterIndexed false
            else depthReadings[index - 1] < depth
        }.size
    }
    
    fun part2(input: List<String>): Int {
        val depthReadings = input.map { it.toInt() }
        val windows = depthReadings
            .filterIndexed { index, _ -> index + 2 < depthReadings.size }
            .mapIndexed { index, depth ->
                val partA = depth
                val partB = depthReadings[index + 1]
                val partC = depthReadings[index + 2]
                partA + partB + partC
            }
        return part1(windows.map { it.toString() })
    }
    
    fun assertEquals(condition: Any?, expected: Any?) {
        require(condition == expected) { "Test Failed! Expected $expected, Received $condition" }
    }
}

