import kotlin.system.measureTimeMillis

fun main() {
//    val day16ExampleInput = readInput("Day16_Test")
    Day16.examplesPart1.forEach { assertEquals(it.part1Expected, Day16.part1(it.input)) }
    Day16.examplesPart2.forEach { assertEquals(it.part2Expected, Day16.part2(it.input)) }
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
        val packets = parsePackets(rawBits)
        return packets.sumOf { it.versionSum() }
    }

    fun part2(input: String): Long {
        val rawBits = input.toCharArray().joinToString("") { it.digitToInt(16).toString(2).padStart(4, '0') }
        val packets = parsePackets(rawBits)
        return packets.sumOf { it.evaluate() }
    }

    private fun parsePackets(input: String): List<Packet> {
        var pt = 0
        val parsedPackets = mutableListOf<Packet>()
        while (pt < input.length && input.substring(pt, input.length).any { it == '1' }) {
            val version = input.substring(pt, pt + 3).also { pt += 3 }.toInt(2)
            val type = input.substring(pt, pt + 3).also { pt += 3 }.toInt(2)
            if (type == 4) {
                val result = parseLiteralPacket(input, pt, version, type)
                pt = result.second
                parsedPackets.add(result.first)
            } else {
                val indicator = input[pt].also { pt++ }
                val (packet, newPtr) = if (indicator == '0') parseOperatorType0(input, pt, version, type)
                else parseOperatorType1(input, pt, version, type)
                parsedPackets.add(packet)
                pt = newPtr
            }
        }
        return parsedPackets
    }

    private fun parseOperatorType0(input: String, pointer: Int, version: Int, type: Int): Pair<Packet, Int> {
        var pt = pointer
        val lengthOfSubPackets = input.substring(pt, pt + 15).toInt(2).also { pt += 15 }
        val packetsWithin = input.substring(pt, pt + lengthOfSubPackets).also { pt += lengthOfSubPackets }
        val packets = parsePackets(packetsWithin)
        val packet = OperatorPacket(version, type, 0, packets.toMutableList())
        return packet to pt
    }

    private fun parseOperatorType1(input: String, pointer: Int, version: Int, type: Int): Pair<Packet, Int> {
        var pt = pointer
        val packets = mutableListOf<Packet>()
        val desiredNumPackets = input.substring(pt, pt + 11).toInt(2).also { pt += 11 }
        while (packets.size < desiredNumPackets) {
            val version = input.substring(pt, pt + 3).also { pt += 3 }.toInt(2)
            val type = input.substring(pt, pt + 3).also { pt += 3 }.toInt(2)
            if (type == 4) {
                val result = parseLiteralPacket(input, pt, version, type)
                pt = result.second
                packets.add(result.first)
            } else {
                val indicator = input[pt].also { pt++ }
                val (packet, newPtr) = if (indicator == '0') parseOperatorType0(input, pt, version, type)
                else parseOperatorType1(input, pt, version, type)
                packets.add(packet)
                pt = newPtr
            }
        }
        val packet = OperatorPacket(version, type, 1, packets)
        return packet to pt
    }

    private fun parseLiteralPacket(input: String, pointer: Int, version: Int, type: Int): Pair<Packet, Int> {
        var pt = pointer
        var rep = true
        var parsedDigits = ""
        while (rep) {
            input[pt].let { rep = it == '1' }.also { pt++ }
            val num = input.substring(pt, pt + 4).also { pt += 4 }
            parsedDigits += num
        }
        return LiteralPacket(version, type, parsedDigits.toLong(2)) to pt
    }


    abstract class Packet(var version: Int, var type: Int) {
        open fun versionSum() = version
        abstract fun evaluate(): Long
    }

    class LiteralPacket(version: Int, type: Int, var value: Long) : Packet(version, type) {
        override fun evaluate(): Long {
            return value
        }
    }

    class OperatorPacket(
        version: Int,
        type: Int,
        var indicator: Int,
        var subPackets: MutableList<Packet> = mutableListOf()
    ) : Packet(version, type) {
        override fun versionSum() = version + subPackets.sumOf { it.versionSum() }
        override fun evaluate(): Long {
            return when (type) {
                0 -> subPackets.sumOf { it.evaluate() }
                1 -> subPackets.fold(1) { acc, sub -> acc * sub.evaluate() }
                2 -> subPackets.minOf { it.evaluate() }
                3 -> subPackets.maxOf { it.evaluate() }
                5 -> {
                    assertEquals(2, subPackets.size)
                    if (subPackets[0].evaluate() > subPackets[1].evaluate()) 1L else 0L
                }
                6 -> {
                    assertEquals(2, subPackets.size)
                    if (subPackets[0].evaluate() < subPackets[1].evaluate()) 1L else 0L
                }
                7 -> {
                    assertEquals(2, subPackets.size)
                    if (subPackets[0].evaluate() == subPackets[1].evaluate()) 1L else 0L
                }
                else -> throw IllegalArgumentException("Operation not implemented!")
            }
        }
    }

    val examplesPart1 = listOf(
        ExampleToExpected("D2FE28", 6),
        ExampleToExpected("38006F45291200", 1 + 6 + 2),
        ExampleToExpected("EE00D40C823060", 7 + 2 + 4 + 1),
        ExampleToExpected("8A004A801A8002F478", 16),
        ExampleToExpected("620080001611562C8802118E34", 12),
        ExampleToExpected("C0015000016115A2E0802F182340", 23),
        ExampleToExpected("A0016C880162017C3686B18A3D4780", 31)
    )

    data class ExampleToExpected(val input: String, val part1Expected: Int? = null, val part2Expected: Long? = null)

    val examplesPart2 = listOf(
        ExampleToExpected("C200B40A82", null, 3),
        ExampleToExpected("04005AC33890", null, 54),
        ExampleToExpected("880086C3E88112", null, 7),
        ExampleToExpected("CE00C43D881120", null, 9),
        ExampleToExpected("D8005AC2A8F0", null, 1),
        ExampleToExpected("F600BC2D8F", null, 0),
        ExampleToExpected("9C005AC2F8F0", null, 0),
        ExampleToExpected("9C0141080250320F1802104A08", null, 1)
    )
}



