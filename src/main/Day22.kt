import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val day22ExampleInput = readInput("Day22_Test")
    val day22ExampleInput2 = readInput("Day22_Test2")
    assertEquals(590784L, Day22.part1(day22ExampleInput))
    assertEquals(2758514936282235, Day22.part2(day22ExampleInput2))
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
    fun part1(input: List<String>): Long {
        val cubes = parseInput(input)
        val part1Cubes = cubes.filter {
            it.xr.first >= -50 && it.xr.last <= 50 &&
                    it.yr.first >= -50 && it.yr.last <= 50 &&
                    it.zr.first >= -50 && it.zr.last <= 50
        }
        return solve(part1Cubes)
    }

    fun part2(input: List<String>): Long {
        val cubes = parseInput(input)
        return solve(cubes)
    }

    private fun solve(cubes: Sequence<Cyoob>): Long {
        var litCubes = emptyList<Cyoob>()
        cubes.forEach { c ->
            litCubes = when (c.action) {
                true -> union(c, litCubes)
                false -> c.subtractFrom(litCubes)
            }
        }
        return litCubes.sumOf { it.size }
    }

    private fun parseInput(input: List<String>): Sequence<Cyoob> = input.asSequence().map { line ->
        val (x, y, z) = line.split(",").map { xyz ->
            val (a, b) = xyz.substringAfter("=").split("..").map { it.toInt() }
            IntRange(min(a, b), max(a, b))
        }
        Cyoob(line.split(" ").first() == "on", x, y, z)
    }

    private fun union(c: Cyoob, cubes: List<Cyoob>): List<Cyoob> {
        var slashes = listOf(c)
        cubes.filter { it.collides(c) }.forEach { slashes = it.subtractFrom(slashes) }
        return cubes + slashes
    }


    data class Cyoob(
        var action: Boolean,
        var xr: IntRange,
        var yr: IntRange,
        var zr: IntRange
    ) {
        val size: Long
            get() {
                fun size(i: IntRange) = i.last - i.first + 1
                return size(xr).toLong() * size(yr) * size(zr)
            }

        operator fun minus(other: Cyoob): List<Cyoob> {
            if (!collides(other)) return listOf(this)
            val returns = mutableListOf<Cyoob>()
            val xMin = if (xr.first < other.xr.first) {
                returns.add(Cyoob(true, xr.first until other.xr.first, yr, zr))
                other.xr.first
            } else this.xr.first
            val xMax = if (other.xr.last < xr.last) {
                returns.add(Cyoob(true, (other.xr.last + 1)..xr.last, yr, zr))
                other.xr.last
            } else xr.last
            val xr = xMin..xMax
            val yMin = if (yr.first < other.yr.first) {
                returns.add(Cyoob(true, xr, yr.first until other.yr.first, zr))
                other.yr.first
            } else this.yr.first
            val yMax = if (other.yr.last < yr.last) {
                returns.add(Cyoob(true, xr, (other.yr.last + 1)..yr.last, zr))
                other.yr.last
            } else yr.last
            val yr = yMin..yMax
            if (zr.first < other.zr.first)
                returns.add(Cyoob(true, xr, yr, zr.first until other.zr.first))
            if (other.zr.last < zr.last)
                returns.add(Cyoob(true, xr, yr, (other.zr.last + 1)..zr.last))
            return returns
        }

        fun collides(other: Cyoob): Boolean {
            fun IntRange.collides(other: IntRange) = first in other || other.first in this
            return xr.collides(other.xr) && yr.collides(other.yr) && zr.collides(other.zr)
        }

        fun subtractFrom(other: List<Cyoob>) = other.flatMap { it - this }
    }
}



