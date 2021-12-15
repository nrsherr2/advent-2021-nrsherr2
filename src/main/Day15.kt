import kotlin.system.measureTimeMillis

fun main() {
    val day15ExampleInput = readInput("Day15_Test")
    assertEquals(40, Day15.part1(day15ExampleInput))
    val day15Input = readInput("Day15_Input")

    val timeToExecuteDay15 = measureTimeMillis {
        val part1Output = Day15.part1(day15Input)
        val part2Output = Day15.part2(day15Input)
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
    println("Processing time: ${timeToExecuteDay15}ms")
}

object Day15 {
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

}




