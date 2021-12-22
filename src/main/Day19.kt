import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
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
      val l =  linkProbes(probes)
        return l.flatMap { it.beacons }.distinct().count()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    fun linkProbes(probes: List<Probe>): List<Probe> {
        val curProbes = probes.toMutableList().subList(1, probes.size)
        val mutatedProbes = mutableListOf(probes.first())
        while (curProbes.isNotEmpty()) run {
            println("pop: ${curProbes.size} ${mutatedProbes.size}")
            val pop = curProbes.removeAt(0)
            for (xRot in 0..3) {
                for (yRot in 0..3) {
                    for (zRot in 0..3) {
                        val rot = pop.rotateX(xRot).rotateY(yRot).rotateZ(zRot)
                        mutatedProbes.forEach { targetProbe ->
                            val distanceTrips = targetProbe.beacons.flatMap { targetBeacon ->
                                rot.beacons.map {
                                    Triple(it, targetBeacon, distance(it, targetBeacon))
                                }
                            }
                            distanceTrips.firstOrNull { dt -> distanceTrips.count { it.third == dt.third } >= 12 }
                                ?.let {
                                    val tr = rot.reposition(
                                        it.first.xCoord - it.second.xCoord,
                                        it.first.yCoord - it.second.yCoord,
                                        it.first.zCoord - it.second.zCoord
                                    )
                                    mutatedProbes.add(tr)
                                    return@run
                                }
//                            val distances = targetProbe.beacons.flatMap { targetBeacon ->
//                                rot.beacons.map { distance(targetBeacon, it) }
//                            }.groupingBy { it }.eachCount().filter { it.value >= 12 }
//                            if (distances.isNotEmpty()) {
//                                val sourceProbe =
//                                    rot.beacons.first { distance(it, targetProbe) == distances.keys.first() }
//                            }
//                            println(distances.groupingBy { it }.eachCount().filter { it.value > 1 })
//                            println()
                        }
                    }
                }
            }

            curProbes.add(pop)
        }
        return mutatedProbes
    }

    fun distance(beak: Beacon, bacon: Beacon) = sqrt(
        (beak.xCoord - bacon.xCoord).toDouble().pow(2.0) +
                (beak.yCoord - bacon.yCoord).toDouble().pow(2.0) +
                (beak.zCoord - bacon.zCoord).toDouble().pow(2.0)
    )

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
            this.copy(
                xCoord = xCoord,
                yCoord = yCoord,
                zCoord = zCoord,
                beacons = beacons.map { Beacon(it.xCoord + xCoord, it.yCoord + yCoord, it.zCoord + zCoord) })
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



