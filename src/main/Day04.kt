import kotlin.system.measureTimeMillis

fun main() {
    val day4ExampleInput = readInput("Day04_Test")
    val day4Input = readInput("Day04_Input")
    
    val exampleGame = Day04.parseInput(day4ExampleInput)
    val day4Part1TestOutput = exampleGame.play().let { it.first * it.second }
    val day4Part2TestOutput = exampleGame.playToLose().let { it.first * it.second }
    assertEquals(4512, day4Part1TestOutput)
    assertEquals(1924, day4Part2TestOutput)
    
    val timeToExecuteDay3 = measureTimeMillis {
        val game = Day04.parseInput(day4Input)
        val part1Output = game.play().let { it.first * it.second }
        val part2Output = game.playToLose().let { it.first * it.second }
//        val part1Output = Day03.part1(day3Input)
//        val part2Output = Day03.part2(day3Input)
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
    println("Processing time: ${timeToExecuteDay3}ms")
    
}

object Day04 {
    fun parseInput(inputLines: List<String>): BingoGame {
        val callOrder = inputLines.first().split(",").map { it.toInt() }
        val justBoards = inputLines.drop(1).filter { it.isNotBlank() }
        val boards = justBoards.chunked(5).map { BingoBoard(it) }
        return BingoGame(callOrder, boards)
    }
}

class BingoGame(
    val callOrder: List<Int>,
    val boards: List<BingoBoard>
) {
    fun play(): Pair<Int, Int> {
        callOrder.forEach { number ->
            callNumber(number)
            boards.firstNotNullOfOrNull { it.sumOfUnmarkedIfWon() }?.let { return number to it }
        }
        throw IllegalArgumentException("Could not find winning board!")
    }
    
    fun playToLose(): Pair<Int, Int> {
        var squidGame = boards.toList()
        callOrder.forEach { number ->
            callNumber(number)
            if(squidGame.size > 1){
                squidGame = squidGame.filter { !it.isWon() }
            }else{
                squidGame.first().sumOfUnmarkedIfWon()?.let { return number to it }
            }
        }
        throw IllegalArgumentException("Could not find winning board!")
    }
    
    private fun callNumber(numero: Int) = boards.forEach { it.markCell(numero) }
}

class BingoBoard(inputLines: List<String>) {
    val board = inputLines.map { line ->
        line.trim()
            .replace("  ", " ")
            .split(" ")
            .map { BingoCell(it.toInt()) }
            .toTypedArray()
    }.toTypedArray()
    
    fun markCell(numero: Int) {
        board.flatten().firstOrNull { it.num == numero }?.let { it.marked = true }
    }
    
    fun sumOfUnmarkedIfWon(): Int? = board
        .takeIf { isWon() }
        ?.flatten()
        ?.filter { !it.marked }
        ?.sumOf { it.num }
    
    fun isWon(): Boolean =
        board.any { row -> row.all { it.marked } } ||
                board.indices.any { col -> board.map { it[col] }.all { it.marked } }
    
    override fun toString(): String {
        return board.map { row -> row.map { it.toString() } }.joinToString("\n")
    }
}

class BingoCell(val num: Int) {
    var marked: Boolean = false
    override fun toString(): String {
        return if (marked) "x" else num.toString()
    }
}
