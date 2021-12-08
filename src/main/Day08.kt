import kotlin.system.measureTimeMillis

fun main() {
    val day8ExampleInput = readInput("Day08_Test")
    assertEquals(26, Day08.part1(day8ExampleInput))
//    assertEquals(61229, Day08.part2(day8ExampleInput))
    val day8Input = readInput("Day08_Input")

    val timeToExecuteDay8 = measureTimeMillis {
        val part1Output = Day08.part1(day8Input)
        val part2Output = Day08.part2(day8Input)
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
    fun part1(input: List<String>) = input.sumOf { line ->
        line.split(" | ")[1].split(" ").count { signal ->
            signal.length in listOf(2, 4, 3, 7)
        }
    }

    fun part2(input: List<String>) {

    }

}

class SevenDigitDisplay(
    var top: Char? = null,
    var upRight: Char? = null,
    var upLeft: Char? = null,
    var mid: Char? = null,
    var lwRight: Char? = null,
    var lwLeft: Char? = null,
    var bot: Char? = null,
) {
    val one
        get() = displayString(upRight, lwRight)
    val two
    get() = displayString(top,upRight,mid,lwLeft,bot)
    val three
    get() = displayString(top,upRight,mid,lwRight,bot)
    val four
    get() = displayString(upLeft,upRight,mid,lwRight)

    private fun displayString(vararg c: Char?) = c.joinToString("").toSortedString()
}

fun String.toSortedString() = toCharArray().sorted().joinToString("")


