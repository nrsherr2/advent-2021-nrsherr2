import kotlin.system.measureTimeMillis

fun main() {

        val day3ExampleInput = readInput("Day03_Test")
        val day3Input = readInput("Day03_Input")
    val timeToExecuteDay3 = measureTimeMillis {
        val day3Part1ExampleOutput = Day03.part1(day3ExampleInput)
        Day03.assertEquals(198, day3Part1ExampleOutput)
        
        val day3Part2ExampleOutput = Day03.part2(day3ExampleInput)
        Day03.assertEquals(230, day3Part2ExampleOutput)
        
        val part1Output = Day03.part1(day3Input)
        val part2Output = Day03.part2(day3Input)
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
            return@map if (lines.significantBitAtIndexIsOne(i)) '1' to '0' else '0' to '1'
        }
        val gammaMaleGrindset = bits.map { it.first }.binaryListCharToBase10Number()
        val epsilonMaleGrindset = bits.map { it.second }.binaryListCharToBase10Number()
        return gammaMaleGrindset * epsilonMaleGrindset
    }
    
    fun part2(lines: List<String>): Int {
        val oxygen = part2Filter(lines.toMutableList(), BitCriteria.OXYGEN).toCharArray().toList()
        val co2 = part2Filter(lines.toMutableList(), BitCriteria.CO2).toCharArray().toList()
        return oxygen.binaryListCharToBase10Number() * co2.binaryListCharToBase10Number()
    }
    
    private fun part2Filter(lines: MutableList<String>, bitCriteria: BitCriteria): String {
        val lineLen = lines[0].length
        var lns = lines.toList()
        for (i in 0 until lineLen) {
            val significantBitAtIndexIsOne = lns.significantBitAtIndexIsOne(i)
            lns = lns.filter { satisfiesBitCondition(it[i], bitCriteria, significantBitAtIndexIsOne) }
            if (lns.size == 1) return lns[0]
        }
        return List(lineLen) { "0" }.joinToString("")
    }
    
    private fun satisfiesBitCondition(bit: Char, condition: BitCriteria, significantBitAtIndexIsOne: Boolean): Boolean {
        return bit == when (condition) {
            BitCriteria.OXYGEN -> if (significantBitAtIndexIsOne) '1' else '0'
            BitCriteria.CO2 -> if (significantBitAtIndexIsOne) '0' else '1'
        }
    }
    
    private fun List<String>.significantBitAtIndexIsOne(index: Int): Boolean =
        sumOf { it[index].digitToInt() }.toDouble() >= (size.toDouble() / 2.0)
    
    private enum class BitCriteria { OXYGEN, CO2 }
    
    private fun List<Char>.binaryListCharToBase10Number(): Int {
        return this.joinToString("").toInt(2)
    }
    
    fun assertEquals(expected: Any?, condition: Any?) {
        require(condition == expected) { "Test Failed! Expected $expected, Received $condition" }
    }
}
