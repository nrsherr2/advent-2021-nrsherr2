import kotlin.system.measureTimeMillis

fun main() {
    val day6ExampleInput = readInput("Day06_Test")
    assertEquals(1, day6ExampleInput.size)
    assertEquals(26L, Day06.part1(day6ExampleInput.first(), 18))
    assertEquals(Day06.part1(day6ExampleInput.first(), 18),Day06.part2(day6ExampleInput.first(), 18))
    assertEquals(5934L, Day06.part1(day6ExampleInput.first(), 80))
    assertEquals(Day06.part1(day6ExampleInput.first(), 80),Day06.part2(day6ExampleInput.first(), 80))
    assertEquals(26984457539, Day06.part2(day6ExampleInput.first(), 256))
    val day6Input = readInput("Day06_Input")
    assertEquals(1, day6Input.size)
    
    val timeToExecuteDay6 = measureTimeMillis {
        val part1Output = Day06.part1(day6Input.first(), 80)
        val part2Output = Day06.part2(day6Input.first(), 256)
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
    println("Processing time: ${timeToExecuteDay6}ms")
}

object Day06 {
    fun part2(initialInput: String, numDays: Int): Long {
        val map = initialInput.split(",")
            .groupingBy { -9L + it.toLong() }
            .eachCount().mapValues { it.value.toLong() }.toMutableMap()
        (-8 until numDays).forEach { dayNum ->
            map[dayNum.toLong()]?.let { numSpawnedThatDay ->
                val spawnSchedule = calculateSpawnSchedule(dayNum, numDays)
                spawnSchedule.forEach { spawnDay ->
                    val initialValue = map.getOrDefault(spawnDay.toLong(), 0L)
                    map[spawnDay.toLong()] = initialValue + numSpawnedThatDay
                }
            }
        }
        return map.values.sum()
    }
    
    private fun calculateSpawnSchedule(daySpawned: Int, maxDay: Int): List<Int> {
        val spawnDays = mutableListOf<Int>()
        var currentNumber = 8
        (daySpawned + 1 until maxDay).forEach { dayNum ->
            currentNumber--
            if (currentNumber == -1) {
                currentNumber = 6
                spawnDays.add(dayNum)
            }
        }
        return spawnDays
    }
    
    fun part1(initialInput: String, numDays: Int): Long {
        val fish = initialInput.split(",").map { it.toInt() }.toMutableList()
        (0 until numDays).forEach { day ->
            val fishSize = fish.size
            (0 until fishSize).forEach { idx ->
                fish[idx] = fish[idx] - 1
                if (fish[idx] == -1) {
                    fish[idx] = 6
                    fish.add(8)
                }
            }
        }
        return fish.size.toLong()
    }
    
}




