import kotlin.math.cos
import kotlin.math.sin
import kotlin.system.measureTimeMillis

fun main() {
    val day19ExampleInput = readInput("Day19_Test")
    assertEquals(79, Day19.part1(day19ExampleInput))
//    assertEquals(112, Day19.part2(day19ExampleInput))
    val day19Input = readInput("Day19_Input")

    val timeToExecuteDay19 = measureTimeMillis {
        val part1Output = Day19.part1(day19Input)
        val part2Output = Day19.part2(day19Input)
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
    println("Processing time: ${timeToExecuteDay19}ms")
}

object Day19 {
    fun part1(input: List<String>): Int {
        val probes = parseInput(input)
        println(probes.joinToString("\n") { it.toString() })
        linkProbes(probes)
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    fun linkProbes(probes: List<Probe>): List<Probe> {
        val poolSize = poolSize(probes)
        val curProbes = probes.toMutableList().subList(1, probes.size)
        val mutatedProbes = mutableListOf(probes.first())
        while (curProbes.isNotEmpty()) run {
            println("pop: ${curProbes.size} ${mutatedProbes.size}")
            val pop = curProbes.removeAt(0)
            for (xTrans in poolSize.minX..poolSize.maxX) {
                for (yTrans in poolSize.minY..poolSize.maxY) {
                    for (zTrans in poolSize.minZ..poolSize.maxZ) {
                        val shifted = pop.reposition(xTrans, yTrans, zTrans)
                        for (xRot in 0..3) {
                            for (yRot in 0..3) {
                                for (zRot in 0..3) {
                                    val rot = shifted.rotateX(xRot).rotateY(yRot).rotateZ(zRot)
                                    mutatedProbes.forEach { targetProbe ->
                                        if (rot.beacons.count { it in targetProbe.beacons } >= 12) {
                                            mutatedProbes.add(rot)
                                            return@run
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            curProbes.add(pop)
        }
        return mutatedProbes
    }

    fun parseInput(input: List<String>): List<Probe> {
        val starts = input.mapIndexedNotNull { index, s -> index.takeIf { s.startsWith("---") } }
        val blanks = input.mapIndexedNotNull { index, s -> index.takeIf { s.isEmpty() } }
        val collections = starts.mapIndexed { index, start -> input.subList(start + 1, blanks[index]) }
        return collections.map { coordStrings ->
            Probe(0, 0, 0, coordStrings.map { coordList ->
                val (x, y, z) = coordList.split(",").map { it.toInt() }
                Beacon(x, y, z)
            })
        }
    }

    data class Probe(val xCoord: Int = 0, val yCoord: Int = 0, val zCoord: Int = 0, var beacons: List<Beacon>) {
        fun rotateX(numRotations: Int): Probe {
            val rotationRadians = Math.toRadians(90.0 * numRotations)
            return Probe(xCoord, yCoord, zCoord, beacons = beacons.map { bcn ->
                Beacon(
                    xCoord = bcn.xCoord,
                    yCoord = bcn.yCoord * cos(rotationRadians).toInt() - bcn.zCoord * sin(rotationRadians).toInt(),
                    zCoord = bcn.yCoord * sin(rotationRadians).toInt() + bcn.zCoord * cos(rotationRadians).toInt()
                )
            })
        }

        fun rotateY(numRotations: Int): Probe {
            val rotationRadians = Math.toRadians(90.0 * numRotations)
            return Probe(xCoord, yCoord, zCoord, beacons = beacons.map { bcn ->
                Beacon(
                    xCoord = bcn.xCoord * cos(rotationRadians).toInt() + bcn.zCoord * sin(rotationRadians).toInt(),
                    yCoord = bcn.yCoord,
                    zCoord = -1 * bcn.xCoord * sin(rotationRadians).toInt() + bcn.zCoord * cos(rotationRadians).toInt()
                )
            })
        }

        fun rotateZ(numRotations: Int): Probe {
            val rotationRadians = Math.toRadians(90.0 * numRotations)
            return Probe(xCoord, yCoord, zCoord, beacons = beacons.map { bcn ->
                Beacon(
                    xCoord = bcn.xCoord * cos(rotationRadians).toInt() - bcn.yCoord * sin(rotationRadians).toInt(),
                    yCoord = bcn.xCoord * sin(rotationRadians).toInt() + bcn.yCoord * cos(rotationRadians).toInt(),
                    zCoord = bcn.zCoord
                )
            })
        }

        fun reposition(xCoord: Int = 0, yCoord: Int = 0, zCoord: Int = 0) =
            this.copy(xCoord, yCoord, zCoord, beacons.toList())
    }

    private fun poolSize(probes: List<Probe>) =
        probes.flatMap { it.beacons }.let { bcs ->
            PoolSize(
                bcs.minOf { it.xCoord },
                bcs.minOf { it.yCoord },
                bcs.minOf { it.zCoord },
                bcs.maxOf { it.xCoord },
                bcs.maxOf { it.yCoord },
                bcs.maxOf { it.zCoord }
            )
        }

    private data class PoolSize(
        val minX: Int,
        val minY: Int,
        val minZ: Int,
        val maxX: Int,
        val maxY: Int,
        val maxZ: Int
    )

    data class Beacon(var xCoord: Int = 0, var yCoord: Int = 0, var zCoord: Int = 0)
}



