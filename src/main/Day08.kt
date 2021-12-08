import kotlin.system.measureTimeMillis

fun main() {
    val day8ExampleInput = readInput("Day08_Test")
    assertEquals(26, Day08.part1(day8ExampleInput))
    assertEquals(5353,Day08.part2(listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")))
    assertEquals(61229, Day08.part2(day8ExampleInput))
    val day8Input = readInput("Day08_Input")

    val timeToExecuteDay8 = measureTimeMillis {
        val part1Output = Day08.part1(day8Input)
        val part2Output = null
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

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val display = determineDisplayMapping(line.replace(" | ", " ").split(" "))
            line.split(" | ")[1].split(" ").map { display.determineNumber(it.toSortedString()) }.joinToString("").toInt()
        }
    }

    private fun determineDisplayMapping(input: List<String>): SevenDigitDisplay {
        val display = SevenDigitDisplay()
        val frConf = input.first { it.length == 4 }
        val svConf = input.first { it.length == 3 }
        val one = input.first { it.length == 2 }.toSortedString()
        display.top = svConf.first { it !in frConf }
        val (zeroOrNines, six) = input.filter { it.length == 6 }.partition { one.all { light -> it.contains(light) } }
            .let { it.first to it.second.first() }
        display.upRight = one.first { !six.toSortedString().contains(it) }
        val fiveLens = input.filter { it.length == 5 }
        val five = fiveLens.first { !it.contains(display.upRight!!) }
        display.lwLeft = six.first { !five.contains(it) }
        if (six.isEmpty()) throw IllegalArgumentException("no six for $input")
        val zero = zeroOrNines.first { it.contains(display.lwLeft!!) }
        display.mid = six.first { !zero.contains(it) }
        val two = fiveLens.first { fvl ->
            listOf(display.top, display.upRight, display.mid, display.lwLeft)
                .all { fvl.contains(it!!) }
        }
        display.bot = two.first { !listOf(display.top, display.upRight, display.mid, display.lwLeft).contains(it) }
        display.lwRight = five.first { one.contains(it) }
        display.upLeft = "abcdefg".first {
            !listOf(display.top, display.upRight, display.mid, display.lwLeft, display.lwRight, display.bot)
                .contains(it)
        }
        return display.also {
            it.lightEmUp()
            println("***")
        }
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
    fun lightEmUp() = listOf(one, two, three, four, five, six, seven, eight, nine, zero).forEach { it+1 }

    fun determineNumber(input: String) =
        (listOf(one, two, three, four, five, six, seven, eight, nine, zero).indexOfFirst { it == input } + 1)
            .also { if (it == -1) throw IllegalArgumentException("Could not find match for $input") }

    private val one
        get() = displayString(upRight, lwRight).toSortedString().also{println(it)}
    private val two
        get() = displayString(top, upRight, mid, lwLeft, bot).toSortedString().also{println(it)}
    private val three
        get() = displayString(top, upRight, mid, lwRight, bot).toSortedString().also{println(it)}
    private val four
        get() = displayString(upLeft, upRight, mid, lwRight).toSortedString().also{println(it)}
    private val five
        get() = displayString(top, upLeft, mid, lwRight, bot).toSortedString().also{println(it)}
    private val six
        get() = displayString(top, upLeft, mid, lwRight, lwLeft, bot).toSortedString().also{println(it)}
    private val seven
        get() = displayString(top, upRight, lwRight).toSortedString().also{println(it)}
    private val eight
        get() = displayString(top, upLeft, upRight, mid, lwRight, lwLeft, bot).toSortedString().also{println(it)}
    private val nine
        get() = displayString(top, upRight, upLeft, mid, lwRight).toSortedString().also{println(it)}
    private val zero
        get() = displayString(top, upLeft, upRight, lwRight, lwLeft, bot).toSortedString().also{println(it)}

    private fun displayString(vararg c: Char?) = c.joinToString("").toSortedString()
}

fun String.toSortedString() = toCharArray().sorted().joinToString("")


