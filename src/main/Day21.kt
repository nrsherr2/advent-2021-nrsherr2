import java.util.*
import kotlin.math.max
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val day21ExampleInput = readInput("Day21_Test")
    assertEquals(739785, Day21.part1(day21ExampleInput))
    assertEquals(444356092776315L, Day21.part2(day21ExampleInput))
    val day21Input = readInput("Day21_Input")

    val timeToExecuteDay21 = measureTimeMillis {
        val part1Output = Day21.part1(day21Input)
        val part2Output = Day21.part2(day21Input)
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
    println("Processing time: ${timeToExecuteDay21}ms")
}

object Day21 {
    fun part1(input: List<String>): Int {
        val deterministicDie = DeterministicDie()
        val players = input.map { Player(it.split(" ").last().toInt(), deterministicDie) }
        while (players.none { it.score >= 1000 }) run {
            players.forEach { player ->
                player.roll3Die()
                if (player.score >= 1000) return@run
            }
        }
        val loser = players.minByOrNull { it.score }!!
        return loser.score * deterministicDie.numRolls
    }

    fun part2(input: List<String>): Long {
        val qd = FixedDie()
        val players = input.map { Player(it.split(" ").last().toInt(), qd) }
        val allGameStates = mutableListOf(GameState(players[0], players[1]))
        var player1Wins = 0L
        var player2Wins = 0L
        var counter = 0
        while (allGameStates.isNotEmpty()) run {
            val copy = allGameStates.toList()
//            println("round ${++counter}: ${copy.size} universes, player 1: $player1Wins player 2: $player2Wins")
            copy.forEach { gs ->
                allGameStates.remove(gs)
                for (i in 1..3) {
                    for (j in 1..3) {
                        for (k in 1..3) {
                            val newState = gs.dupe()
                            qd.resultQueue.addAll(listOf(i, j, k))
                            val currentPlayer = when (newState.currentPlayerOne) {
                                true -> newState.player1
                                false -> newState.player2
                            }
                            currentPlayer.roll3Die()
                            if (currentPlayer.score >= 21) {
                                when (newState.currentPlayerOne) {
                                    true -> player1Wins += newState.numCopies.toLong()
                                    false -> player2Wins += newState.numCopies.toLong()
                                }
                            } else {
                                newState.currentPlayerOne = !newState.currentPlayerOne
                                allGameStates.firstOrNull {
                                    it.currentPlayerOne == newState.currentPlayerOne &&
                                            it.player1.score == newState.player1.score &&
                                            it.player1.currentPosition == newState.player1.currentPosition &&
                                            it.player2.score == newState.player2.score &&
                                            it.player2.currentPosition == newState.player2.currentPosition
                                }?.let {
                                    it.numCopies += newState.numCopies
                                } ?: allGameStates.add(newState)
                            }
                        }
                    }
                }
            }
        }
        return max(player1Wins, player2Wins)
    }

    private class GameState(
        val player1: Player,
        val player2: Player,
        var currentPlayerOne: Boolean = true,
        var numCopies: Long = 1
    ) {

        fun dupe() = GameState(
            player1 = player1.dupe(),
            player2 = player2.dupe(),
            currentPlayerOne = currentPlayerOne,
            numCopies = numCopies
        )
    }


    private class Player(
        startingPosition: Int,
        var die: Die = DeterministicDie(),
        var score: Int = 0
    ) {
        var currentPosition: Int = startingPosition
        fun roll3Die() {
            val mvmt = die.roll() + die.roll() + die.roll()
            currentPosition = ((currentPosition - 1 + mvmt) % 10) + 1
            score += currentPosition
        }

        fun dupe() = Player(startingPosition = currentPosition, die = die, score = score)
    }

    sealed interface Die {
        var numRolls: Int
        fun roll(): Int
    }

    class FixedDie : Die {
        val resultQueue: Queue<Int> = LinkedList()
        override var numRolls: Int = 0

        override fun roll(): Int {
            numRolls++
            return resultQueue.poll()
        }

    }

    class DeterministicDie : Die {
        var currentValue: Int = 0
        override var numRolls: Int = 0

        override fun roll(): Int {
            numRolls++
            currentValue++
            if (currentValue > 100) currentValue -= 100
            return currentValue
        }
    }
}



