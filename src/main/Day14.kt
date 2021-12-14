import kotlin.system.measureTimeMillis

fun main() {
    val day14ExampleInput = readInput("Day14_Test")
    assertEquals(1588, Day14.part1(day14ExampleInput))
    assertEquals(5L, Day14.part2(day14ExampleInput, 2))
    assertEquals(1588L, Day14.part2(day14ExampleInput, 10))
    assertEquals(2188189693529L, Day14.part2(day14ExampleInput))
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

    fun part2(input: List<String>, maxTime: Int = 40): Long {
        val list = input.first()
        val rules = input.subList(2, input.size).associate {
            val (a, b) = it.split(" -> ")
            a to b
        }
        println("calculating rule metas")
        val ruleMetas = rules.entries.map {
            RuleMeta(
                it.key,
                it.value,
                calculateBloom(it.key, rules, maxTime / 2, it.key.last().toString())
                    .entries.map { e -> e.key to e.value.toLong() }.toMap()
            )
        }
        val michaelsoft = list.windowed(2, 1)
        val results = michaelsoft.mapIndexed { index, window ->
            println("Window: $window")
            val bl = bloom(window, maxTime / 2, rules)
            val binbows = bl.windowed(2, 1)
            val bl2 = binbows.mapIndexed { idx, bin ->
                ruleMetas.first { it.condition == bin }.spreads.toMutableMap().apply {
                    if (idx == binbows.lastIndex) this[bin.last().toString()] = this[bin.last().toString()]!! + 1
                }
            }
            foldMapsTogether(bl2).apply {
                if (index < michaelsoft.lastIndex) this[window.last().toString()] = this[window.last().toString()]!! - 1
            }
        }
        val counts = foldMapsTogether(results)
        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    private fun foldMapsTogether(bl2: List<MutableMap<String, Long>>) =
        bl2.foldRight(mutableMapOf<String, Long>()) { src, dest ->
            src.entries.forEach { ent ->
                if (dest.containsKey(ent.key)) {
                    dest[ent.key] = dest[ent.key]!!.plus(ent.value)
                } else {
                    dest[ent.key] = ent.value
                }
            }
            dest
        }

    private data class RuleMeta(val condition: String, val output: String, val spreads: Map<String, Long>)

    private fun calculateBloom(
        list: String,
        rules: Map<String, String>,
        bloomSize: Int,
        minusChar: String? = null
    ): Map<String, Int> = bloom(list, bloomSize, rules).groupingBy { it.toString() }.eachCount().toMutableMap().apply {
        if (minusChar != null) this[minusChar] = this[minusChar]!! - 1
    }


    private fun bloom(
        list: String,
        bloomSize: Int,
        rules: Map<String, String>
    ): String {
        var lt = list
        for (i in 0 until bloomSize) {
            lt = lt.asSequence().mapIndexed { index, thisChar ->
                if (index + 1 == lt.length) thisChar.toString()
                else {
                    rules["$thisChar${lt[index + 1]}"]?.let { "$thisChar$it" } ?: thisChar.toString()
                }
            }.joinToString("")
        }
        return lt
    }

}




