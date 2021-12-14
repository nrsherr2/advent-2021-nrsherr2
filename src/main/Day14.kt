import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val day14ExampleInput = readInput("Day14_Test")
    assertEquals(1588, Day14.part1(day14ExampleInput))
    assertEquals(1588, Day14.part2(day14ExampleInput))
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
        val list = input.first().map { CharNode(it.toString()) }.toMutableList()
        val rules = input.subList(2, input.size).associate {
            val (a, b) = it.split(" -> ")
            a to b
        }
        val counts = calculateBloom(list, rules, 10)
        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val list = input.first().map { CharNode(it.toString()) }.toMutableList()
        val rules = input.subList(2, input.size).associate {
            val (a, b) = it.split(" -> ")
            a to b
        }
        val results = mutableListOf<BloomResult>()
        list.windowed(2, 1).forEach {
            val updatedRules = rules.toMutableMap()
                .apply { putAll(results.associate { res -> res.condition to results.indexOf(res).toString() }) }
            val map = calculateBloom(it.toMutableList(), updatedRules, 10)
            results.add(BloomResult(it.joinToString("") { cn -> cn.value }, map))
        }
        println(results)
        TODO()
//        val counts = calculateBloom(list, rules, 40)
//        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    private fun calculateBloom(
        list: MutableList<CharNode>,
        rules: Map<String, String>,
        bloomSize: Int,
    ): Map<String, Int> {
        for (i in 0 until bloomSize) {
            println(i)
            val newOnesToAdd = mutableListOf<CharNode>()
            list.windowed(size = 2, step = 1).forEach { window ->
                val condition = window.joinToString("") { it.value }
                rules[condition]?.let { newOnesToAdd.add(CharNode(it, window.first())) }
            }
            newOnesToAdd.forEach {
                list.add(list.indexOf(it.prev) + 1, it)
            }
            println(list.joinToString("") { it.value })
        }
        return list.groupingBy { it.value }.eachCount()
    }

    class CharNode(val value: String, var prev: CharNode? = null) {
        val id = Random.nextInt()
    }

    data class BloomResult(val condition: String, val outputMap: Map<String, Int>)
}




