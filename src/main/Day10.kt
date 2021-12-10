import kotlin.system.measureTimeMillis

fun main() {
    val day10ExampleInput = readInput("Day10_Test")
    assertEquals(26397, Day10.part1(day10ExampleInput))
    val day10Input = readInput("Day10_Input")

    val timeToExecuteDay10 = measureTimeMillis {
        val part1Output = Day10.part1(day10Input)
        val part2Output = Day10.part2(day10Input)
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
    println("Processing time: ${timeToExecuteDay10}ms")
}

object Day10 {
    fun part1(input: List<String>): Int {
        val badChars = input.mapNotNull { parseChunks(it) }
        return badChars.sumOf {
            when (it) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> "0".toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val incompleteLines = input.filter { parseChunks(it) == null }
        return 0
    }

    fun parseChunks(input: String): Char? {
        val chunks = mutableListOf<ChunkPair>()
        input.forEach { c ->
            if (c in listOf('(', '{', '[', '<')) {
                chunks.add(ChunkPair(c, null))
            } else {
                val lastOpenChunk = chunks.lastOrNull { it.close == null } ?: throw IllegalArgumentException()
                val desiredChar = when (lastOpenChunk.open) {
                    '(' -> ')'
                    '{' -> '}'
                    '<' -> '>'
                    '[' -> ']'
                    else -> throw IllegalArgumentException()
                }
                if (c != desiredChar) return c
                else lastOpenChunk.close = c
            }
        }
        return null
    }

    data class ChunkPair(var open: Char, var close: Char?)
}



