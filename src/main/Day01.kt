import kotlin.system.measureTimeMillis

fun main() {
    val a = measureTimeMillis {
        with(Day01()) {
            //given the example for part 1, ensure we get the same result they do
            val part1ExampleInput = readInput("Day01_Part1_Test")
            val part1ExampleOutput = part1(part1ExampleInput)
            println(part1ExampleOutput)
            assertEquals(part1ExampleOutput, 7)
            
            val part1Input = readInput("Day01_Part1_Input")
            val part1Output = part1(part1Input)
            println(part1Output)
        }
    }
    println("Processing time: ${a}ms")
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
        return input.size
    }
    
    fun assertEquals(condition: Any?, expected: Any?) {
        require(condition == expected) { "Test Failed! Expected $expected, Received $condition" }
    }
}

