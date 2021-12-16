import kotlin.system.measureTimeMillis

fun main() {
    val day16ExampleInput = readInput("Day16_Test")
    Day16.examplesPart1.forEach {
        assertEquals(it.part1Expected, Day16.part1(it.input))
    }
//    assertEquals(40, Day16.part1(day16ExampleInput))
//    assertEquals(316, Day16.part2(day16ExampleInput))
    val day16Input = readInput("Day16_Input")

    val timeToExecuteDay16 = measureTimeMillis {
        val part1Output = Day16.part1(day16Input.first())
        val part2Output = Day16.part2(day16Input.first())
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
    println("Processing time: ${timeToExecuteDay16}ms")
}

object Day16 {
    fun part1(input: String): Int {
        return 0
    }

    fun part2(input: String): Int {
        return 0
    }

    val examplesPart1 = listOf(
        ExampleToExpected("8A004A801A8002F478", 16),
        ExampleToExpected("620080001611562C8802118E34", 12),
        ExampleToExpected("C0015000016115A2E0802F182340", 23),
        ExampleToExpected("A0016C880162017C3686B18A3D4780", 31)
    )

    open class Packet(var version: Int, var type: Int)
    class LiteralPacket(version: Int, type: Int, var value: Int) : Packet(version, type)
    class OperatorPacket(version: Int, type: Int, var indicator: Boolean) {
        var subPackets: MutableList<Packet> = mutableListOf()
    }

    data class ExampleToExpected(val input: String, val part1Expected: Int, val part2Expected: Int? = null)
}



