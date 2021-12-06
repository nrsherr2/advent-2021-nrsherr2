import kotlin.system.measureTimeMillis

fun main() {
    val day6ExampleInput = readInput("Day06_Test")
    assertEquals(1, day6ExampleInput.size)
    assertEquals(26, Day06.part1(day6ExampleInput.first(), 18))
    assertEquals(5934, Day06.part1(day6ExampleInput.first(), 80))
//    assertEquals(26984457539, Day06.part1(day6ExampleInput.first(), 256))
    val day6Input = readInput("Day06_Input")
    assertEquals(1, day6Input.size)
    
    val timeToExecuteDay6 = measureTimeMillis {
        val part1Output = Day06.part1(day6Input.first(), 80)
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
    println("Processing time: ${timeToExecuteDay6}ms")
}

object Day06 {
    fun part2(initialInput: String, numDays: Int): Int {
        
        TODO()
    }
    
    fun part1(initialInput: String, numDays: Int): Int {
        val fish = initialInput.split(",").map { it.toInt() }.toMutableList()
        (0 until numDays).forEach { day ->
            val fishSize = fish.size
            (0 until fishSize).forEach { idx ->
                fish[idx] = fish[idx] - 1
                if (fish[idx] == -1) {
                    fish[idx] = 6
                    fish.add(8)
                }
            }
            println("Fish after $day days: ${fish.size}")
        }
        return fish.size
    }
    
}




