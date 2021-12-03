import kotlin.system.measureTimeMillis

fun main() {
    val timeToExecuteDay3 = measureTimeMillis {
        val day3ExampleInput = readInput("Day03_Test")
        val day3Input = readInput("Day03_Input")
        
        val day3Part1ExampleOutput = Day03.part1(day3ExampleInput)
        Day03.assertEquals(198, day3Part1ExampleOutput)
        
        val part1Output = Day03.part1(day3Input)
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
    println("Processing time: ${timeToExecuteDay3}ms")
    
}

object Day03 {
    fun part1(lines: List<String>): Int {
        if (lines.isEmpty()) return 0
        val bits = (0 until lines[0].length).map { i ->
            val significantDigitIsOne = lines.sumOf { it[i].digitToInt() } > (lines.size / 2)
            return@map if (significantDigitIsOne) '1' to '0' else '0' to '1'
        }
        val gammaMaleGrindset = bits.map { it.first }.binaryListCharToBase10Number()
        val epsilonMaleGrindset = bits.map { it.second }.binaryListCharToBase10Number()
        return gammaMaleGrindset * epsilonMaleGrindset
    }
    private fun List<Char>.binaryListCharToBase10Number():Int {
        return this.joinToString("").toInt(2)
    }
    fun assertEquals(expected: Any?, condition: Any?) {
        require(condition == expected) { "Test Failed! Expected $expected, Received $condition" }
    }
}
