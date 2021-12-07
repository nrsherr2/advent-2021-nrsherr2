import kotlin.system.measureTimeMillis

fun main() {
    val day7ExampleInput = readInput("Day07_Test")
    assertEquals(1, day7ExampleInput.size)
    assertEquals(2, Day07.part1(day7ExampleInput.first()))
    val day7Input = readInput("Day07_Input")
    assertEquals(1, day7Input.size)
    
    val timeToExecuteDay7 = measureTimeMillis {
        val part1Output =  Day07.part1(day7Input.first())
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
    println("Processing time: ${timeToExecuteDay7}ms")
}

object Day07 {
    fun part1(input:String): Int {
        val listo = input.split(",").map { it.toInt()}
        //it's not median
        return listo.sorted()[listo.size / 2]
    }
}




