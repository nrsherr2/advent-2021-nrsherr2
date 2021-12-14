import kotlin.system.measureTimeMillis

fun main() {
    val day14ExampleInput = readInput("Day14_Test")
    assertEquals(1588, Day14.part1(day14ExampleInput))
//    assertEquals(1588, Day14.part2(day14ExampleInput))
    assertEquals(2188189693529, Day14.part2(day14ExampleInput))
    val day14Input = readInput("Day14_Input")

    val timeToExecuteDay14 = measureTimeMillis {
        val part1Output = Day14.part1(day14Input)
        val part2Output = Day14.part2(day14Input)
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

object Day14 {
    fun part1(input: List<String>): Int {
        val list = input.first()
        val rules = input.subList(2, input.size).associate {
            val (a, b) = it.split(" -> ")
            a to b
        }
        val counts = calculateBloom(list, rules, 10)
        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val list = input.first()
        val rules = input.subList(2, input.size).associate {
            val (a, b) = it.split(" -> ")
            a to b
        }
        val results = mutableListOf<BloomResult>()
        list.windowed(2, 1).forEach {window->
            val updatedRules = rules.toMutableMap()
                .apply { putAll(results.associate { res -> res.condition to results.indexOf(res).toString() }) }
            val bloomAfter20 = bloom(window,20,updatedRules)
            println(bloomAfter20)
            val map = calculateBloom(window, updatedRules, 40)
            results.add(BloomResult(window, map))
        }
        println(results)
        TODO()
//        val counts = calculateBloom(list, rules, 40)
//        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    private fun calculateBloom(
        list: String,
        rules: Map<String, String>,
        bloomSize: Int,
    ): Map<String, Int> = bloom(list, bloomSize, rules).groupingBy { it.toString() }.eachCount()


    private fun bloom(
        list: String,
        bloomSize: Int,
        rules: Map<String, String>
    ): String {
        var lt = list
        for (i in 0 until bloomSize) {
            println(i)
            lt = lt.asSequence().mapIndexed { index, thisChar ->
                if (index + 1 == lt.length) thisChar.toString()
                else {
                    rules["$thisChar${lt[index + 1]}"]?.let { "$thisChar$it" } ?: thisChar.toString()
                }
            }.joinToString("")
        }
        return lt
    }

    data class BloomResult(val condition: String, val outputMap: Map<String, Int>)
}




