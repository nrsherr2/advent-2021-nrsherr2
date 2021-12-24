import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    val day22ExampleInput = readInput("Day22_Test")
    assertEquals(590784, Day22.part1(day22ExampleInput))
//    assertEquals(3351, Day22.part2(day22ExampleInput))
    val day22Input = readInput("Day22_Input")

    val timeToExecuteDay22 = measureTimeMillis {
        val part1Output = Day22.part1(day22Input)
        val part2Output = Day22.part2(day22Input)
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
    println("Processing time: ${timeToExecuteDay22}ms")
}

object Day22 {
    fun part1(input: List<String>): Int {
        val grid = List(50) { List(50) { List(50) { false } } }
        TODO()
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    fun parseInput(input: List<String>): Sequence<Ranges> = input.asSequence().map { line ->
        val spl = line.split(" ")
        val action = spl.first()
        val coords = spl.last()
        val xyz = coords.split(",").map { it.filter { it.isDigit() || it == '.' || it == '-' } }
        TODO()
    }

    //off x=18..30,y=-20..-8,z=-3..13


    data class Ranges(
        var action: Boolean,
        var minX: Int,
        var minY: Int,
        var minZ: Int,
        var maxX: Int,
        var maxY: Int,
        var maxZ: Int
    )
}



