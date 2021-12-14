import kotlin.system.measureTimeMillis

fun main() {
    val day14ExampleInput = readInput("Day14_Test")
    assertEquals(1588, Day14.part1(day14ExampleInput))
    assertEquals(5, Day14.part2(day14ExampleInput, 2))
    assertEquals(1588, Day14.part2(day14ExampleInput, 10))
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

    fun part2(input: List<String>, maxTime: Int = 40): Int {
        val list = input.first()
        val rules = input.subList(2, input.size).associate {
            val (a, b) = it.split(" -> ")
            a to b
        }
        val results = mutableListOf<BloomResult>()
        val michaelsoft = list.windowed(2, 1)
        michaelsoft.forEachIndexed { i, window ->
//            val updatedRules = rules.toMutableMap()
//                .apply { putAll(results.associate { res -> res.condition to results.indexOf(res).toString() }) }

            val b20 = bloom(window, maxTime / 2, rules)
            val binbows = b20.windowed(2, 1)
//
//            val bin = binbows.let {
//                val last = it.last()
//                val sl = it.subList(0, it.size - 1)
//                sl.distinct().associateWith { a -> sl.count {s-> s == a } }.plus(last to 1)
//            }
//            val bin20 = bin.entries.mapIndexed { j, (subWind, mult) ->
//                calculateBloom(subWind, rules, maxTime / 2).toMutableMap().apply {
//                    if (j < binbows.size - 1) this[subWind.last().toString()] = this[subWind.last().toString()]!! - 1
//                    this.keys.forEach { this[it] = this[it]!! * mult }
//                }
//            }
//            val m20 = bin20.foldRight(mutableMapOf<String, Int>()) { src, dest ->
//                src.entries.forEach { ent ->
//                    if (dest.containsKey(ent.key)) {
//                        dest[ent.key] = dest[ent.key]!!.plus(ent.value)
//                    } else {
//                        dest[ent.key] = ent.value
//                    }
//                }
//                dest
//            }.apply {
//                if (i < michaelsoft.size - 1) this[window.last().toString()] = this[window.last().toString()]!! - 1
//            }

            val bloomsFrom20 = binbows.mapIndexed { j, subWind ->
                if (j % 25 == 0)
                    println("$i, $j")
                calculateBloom(subWind, rules, maxTime / 2).toMutableMap().apply {
                    if (j < binbows.size - 1) this[subWind.last().toString()] = this[subWind.last().toString()]!! - 1
                }
            }
            val map = bloomsFrom20.foldRight(mutableMapOf<String, Int>()) { src, dest ->
                src.entries.forEach { ent ->
                    if (dest.containsKey(ent.key)) {
                        dest[ent.key] = dest[ent.key]!!.plus(ent.value)
                    } else {
                        dest[ent.key] = ent.value
                    }
                }
                dest
            }.apply {
                if (i < michaelsoft.size - 1) this[window.last().toString()] = this[window.last().toString()]!! - 1
            }
//            println(m20)
            println(map)
            results.add(BloomResult(window, map))
        }
        val counts = results.map { it.outputMap }.foldRight(mutableMapOf<String, Int>()) { src, dest ->
            src.entries.forEach { ent ->
                if (dest.containsKey(ent.key)) {
                    dest[ent.key] = dest[ent.key]!!.plus(ent.value)
                } else {
                    dest[ent.key] = ent.value
                }
            }
            dest
        }
        return counts.maxOf { it.value } - counts.minOf { it.value }
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




