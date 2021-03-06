import kotlin.system.measureTimeMillis

fun main() {
    with(Day01()) {
        //given the example for part 1, ensure we get the same result they do
        val day1ExampleInput = readInput("Day01_Test")
        val day1Input = readInput("Day01_Input")

        val part1ExampleOutput = part1(day1ExampleInput)
        assertEquals(7, part1ExampleOutput)
        val part2ExampleOutput = part2(day1ExampleInput)
        assertEquals(5, part2ExampleOutput)

        val timeToExecuteDay1 = measureTimeMillis {
            val part1Output = part1(day1Input)
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
        println("Processing time: ${timeToExecuteDay1}ms")
    }
}

class Day01 {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }.windowed(2).count { it[0] < it[1] }
    }

    fun part2(input: List<String>): Int {
        val depthReadings = input.map { it.toInt() }
        return part1(depthReadings.windowed(3).map { (it[0] + it[1] + it[2]).toString() })
    }


}

