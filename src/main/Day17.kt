import kotlin.system.measureTimeMillis

fun main() {
    val day17ExampleInput = readInput("Day17_Test")
    assertEquals(45, Day17.part1(day17ExampleInput))
    assertEquals(112, Day17.part2(day17ExampleInput))
    val day17Input = readInput("Day17_Input")

    val timeToExecuteDay17 = measureTimeMillis {
        val part1Output = Day17.part1(day17Input)
        val part2Output = Day17.part2(day17Input)
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
    println("Processing time: ${timeToExecuteDay17}ms")
}

object Day17 {
    fun part1(input: List<String>): Int {
        val coords = extractCoords(input.first())
        var maxHeightGlobal = 0
        for (dx0 in 0..25) {
            for (dy0 in 1..100) {
                val probe = Probe(dx0, dy0)
                val maxYLocal = launchProbe(probe, coords)
                if (maxYLocal > maxHeightGlobal) maxHeightGlobal = maxYLocal
            }
        }
        return maxHeightGlobal
    }

    fun part2(input: List<String>): Int {
        val target = extractCoords(input.first())
        val vels = mutableListOf<Pair<Int, Int>>()
        for (dx0 in 0..350) {
            for (dy0 in -100..5000) {
                val probe = Probe(dx0, dy0)
                if (launchProbe(probe, target) > Int.MIN_VALUE)
                    vels.add(dx0 to dy0)
            }
        }
        return vels.size
    }

    private fun launchProbe(probe: Probe, target: Target): Int {
        var maxYLocal = 0
        var count = 0
        while (!dud(probe, target)) {
            probe.step()
            if (probe.y > maxYLocal) maxYLocal = probe.y
            if (probe.x in target.minX..target.maxX && probe.y in target.minY..target.maxY)
                return maxYLocal
        }
        return Int.MIN_VALUE
    }

    private fun dud(probe: Probe, target: Target): Boolean = probe.y < target.minY && probe.y < 0

    private fun extractCoords(string: String): Target {
        return Regex("-?\\d+").findAll(string).map { it.value.toInt() }.toList().let {
            val x = it[0] to it[1]
            val y = it[2] to it[3]
            Target(
                minOf(x.first, x.second),
                maxOf(x.first, x.second),
                minOf(y.first, y.second),
                maxOf(y.first, y.second)
            )
        }
    }

    private data class Target(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int)
    private data class Probe(var dx: Int, var dy: Int, var x: Int = 0, var y: Int = 0) {
        fun step() {
            x += dx
            y += dy
            dy -= 1
            if (dx > 0) dx -= 1
            if (dx < 0) dx += 1
        }
    }
}



