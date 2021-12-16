import kotlin.system.measureTimeMillis

fun main() {
    val day16ExampleInput = readInput("Day16_Test")
    Day16.examplesPart1.forEach {
        assertEquals(it.part1Expected, Day16.part1(it.input))
    }
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
        val rawBits = input.toCharArray().joinToString("") { it.digitToInt(16).toString(2).padStart(4, '0') }
        println(rawBits)
        return 0
    }

    fun part2(input: String): Int {
        return 0
    }

    fun parsePackets(input: String, pointer: Int, desiredNumPackets: Int? = null): Pair<List<Packet>, Int> {
        var pt = pointer
        val parsedPackets = mutableListOf<Packet>()
        while (pt < input.length && (desiredNumPackets == null || parsedPackets.size < desiredNumPackets)) {
            val version = input.substring(pt, pt + 3).also { pt += 3 }.toInt(2)
            val type = input.substring(pt, pt + 3).also { pt += 3 }.toInt(2)
        }
        return parsedPackets to pt
    }
    fun parseLiteralPacket(input: String,pointer: Int,version: Int,type: Int):Pair<Packet, Int>{
        var pt = pointer
        var rep = true
        var parsedCharacterCount = 0
        TODO()
    }

    val examplesPart1 = listOf(
        ExampleToExpected("D2FE28", 6),
        ExampleToExpected("8A004A801A8002F478", 16),
        ExampleToExpected("620080001611562C8802118E34", 12),
        ExampleToExpected("C0015000016115A2E0802F182340", 23),
        ExampleToExpected("A0016C880162017C3686B18A3D4780", 31)
    )

    open class Packet(var version: Int, var type: Int)
    class LiteralPacket(version: Int, type: Int, var value: Int) : Packet(version, type)
    class OperatorPacket(version: Int, type: Int, var indicator: Boolean) : Packet(version, type) {
        var subPackets: MutableList<Packet> = mutableListOf()
    }

    data class ExampleToExpected(val input: String, val part1Expected: Int, val part2Expected: Int? = null)
}



