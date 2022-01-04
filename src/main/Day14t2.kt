import kotlin.system.measureTimeMillis

fun main() {
    val day14ExampleInput = readInput("Day14_Test")
    assertEquals(1588L, Day14t2.part1(day14ExampleInput))
    assertEquals(5L, Day14t2.part2(day14ExampleInput, 2))
    assertEquals(1588L, Day14t2.part2(day14ExampleInput, 10))
    assertEquals(2188189693529L, Day14t2.part2(day14ExampleInput))
    val day14Input = readInput("Day14_Input")

    val timeToExecuteDay14 = measureTimeMillis {
        val part1Output = Day14t2.part1(day14Input)
        val part2Output = Day14t2.part2(day14Input)
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
    println("Processing time: ${timeToExecuteDay14}ms")
}

object Day14t2 {
    fun part1(input: List<String>) = execute(input)

    fun part2(input: List<String>, maxTime: Int = 40) = execute(input, maxTime)

    fun execute(input: List<String>, time: Int = 10): Long {
        val list = input.first()
        val rules = input.subList(2, input.size).associate {
            val (a, b) = it.split(" -> ")
            a to b
        }
        var buckets = rules.keys.associateWith { 0L }.toMutableMap()
        val letters = rules.values.associateWith { 0L }.toMutableMap()
        list.windowed(2).forEach { buckets[it] = buckets[it]!! + 1 }
        list.forEach { letters[it.toString()] = letters[it.toString()]!! + 1 }
        for (i in 1..time) {
            val newBucks = rules.keys.associateWith { 0L }.toMutableMap()
            rules.forEach { (i, o) ->
                val putNum = buckets[i]!!
                val l = "${i[0]}$o"
                val lb = newBucks[l]!!
                val r = "$o${i[1]}"
                val rb = newBucks[r]!!
                newBucks[l] = lb + putNum
                newBucks[r] = rb + putNum
                letters[o] = letters[o]!! + putNum
            }
            buckets = newBucks
        }
        return letters.maxOf { it.value } - letters.minOf { it.value }
    }

}




