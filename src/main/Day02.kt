import kotlin.system.measureTimeMillis

fun main() {
    
    with(Day02()) {
        val day2ExampleInput = readInput("Day02_Test")
        val day2Input = readInput("Day02_Input")
        val timeToExecuteDay2 = measureTimeMillis {
            val part1ExampleOutput = part1(day2ExampleInput)
            assertEquals(150, part1ExampleOutput)
            
            val part1Output = part1(day2Input)
            
            val part2ExampleOutput = part2(day2ExampleInput)
            assertEquals(900, part2ExampleOutput)
            
            val part2Output = part2(day2Input)
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
        println("Processing time: ${timeToExecuteDay2}ms")
    }
}

class Day02 {
    fun part1(input: List<String>): Int {
        var horizontalMvmt = 0
        var verticalMovement = 0
        input.asSequence()
            .map {
                val spl = it.split(" ")
                spl[0] to spl[1].toInt()
            }
            .forEach { (direction, amt) ->
                when (direction) {
                    "forward" -> horizontalMvmt += amt
                    "down" -> verticalMovement += amt
                    "up" -> verticalMovement -= amt
                    else -> throw IllegalArgumentException()
                }
            }
        return horizontalMvmt * verticalMovement
    }
    
    fun part2(input: List<String>): Int {
        var horizontalMvmt = 0
        var verticalMovement = 0
        var aim = 0
        
        input.asSequence()
            .map {
                val spl = it.split(" ")
                spl[0] to spl[1].toInt()
            }
            .forEach { (direction, amt) ->
                when (direction) {
                    "forward" -> {
                        horizontalMvmt += amt
                        verticalMovement += (aim * amt)
                    }
                    "down" -> aim += amt
                    "up" -> aim -= amt
                    else -> throw IllegalArgumentException()
                }
            }
        return horizontalMvmt * verticalMovement
    }
    
    
}
