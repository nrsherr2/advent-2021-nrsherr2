import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    val day23ExampleInput = readInput("Day23_Test")
    assertEquals(12521L, Day23.part1(day23ExampleInput))
//    assertEquals(3351, Day23.part2(day23ExampleInput))
    val day23Input = readInput("Day23_Input")

    val timeToExecuteDay23 = measureTimeMillis {
        val part1Output = Day23.part1(day23Input)
        val part2Output = Day23.part2(day23Input)
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
    println("Processing time: ${timeToExecuteDay23}ms")
}

object Day23 {
    fun part1(input: List<String>): Long {
        val board = Board(parseInput(input))
        var maxMvmt = Long.MAX_VALUE
        val boardsInSpace: Queue<Board> = LinkedList(listOf(board))
        while (boardsInSpace.isNotEmpty()) {
            val currentBoard = boardsInSpace.poll()
            if (currentBoard.moves.size == 4 + 4 + 3 + 4 + 2 + 3 + 3 + 4 + 8) {
                println(boardsInSpace.size)
                if (currentBoard.moves.totalCost() == 12521L) {
                    println("WE FOUND IT")
                }
            }
            listOf(desiredState).forEach {
                if (currentBoard.stringRep() == it) {
                    println(currentBoard.stringRep())
                    println()
                }
            }
            if (currentBoard.allSatisfied()) {
                println(boardsInSpace.size)
                if (currentBoard.moves.totalCost() < maxMvmt) {
                    maxMvmt = currentBoard.moves.totalCost()
                }
            }
            val nextBoards = currentBoard.possibleMoveSequences()
                .map { currentBoard.newBoardWithMoves(it) }
                .filter { it.uniqueBoardState() && it.moves.totalCost() < maxMvmt }
            nextBoards.forEach { b ->
                if (boardsInSpace.none { it.moves.sameMoves(b.moves) }) {

//                val matchy = boardsInSpace.count { it.moves.all { it in b.moves } && it.moves.size == b.moves.size}
//                if (matchy < 1) {
                    boardsInSpace.offer(b)
                }
//                }
            }
        }
        return maxMvmt
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    class Board() {
        lateinit var moves: List<Move>
        lateinit var crabs: List<Crab>

        fun newBoardWithMoves(newMoves: List<Move>) = Board(moves = moves + newMoves, crabs = crabs)


        fun uniqueBoardState(): Boolean {
            val locations = getCrabLocations()
            moves.indices.forEach {
                val oldBoard = Board(moves.subList(0, it), crabs)
                if (oldBoard.getCrabLocations().matches(locations)) return false
            }
            return true
        }

        fun getCrabLocations(): Map<Crab, Point> {
            val crabsToPoints = crabs.associateWith {
                Point(2 + if (crabs.indexOf(it) >= 4) 1 else 0, (crabs.indexOf(it) % 4) * 2 + 3)
            }.toMutableMap()
            moves.forEach { move ->
                val myCrab = crabs.first { it.name == move.crabName }
                val point = crabsToPoints[myCrab]!!
                when (move.direction) {
                    Direction.UP -> point.rowNum -= 1
                    Direction.DOWN -> point.rowNum += 1
                    Direction.LEFT -> point.colNum -= 1
                    Direction.RIGHT -> point.colNum += 1
                }
                crabsToPoints[myCrab] = point
            }
            return crabsToPoints
        }

        fun possibleMoveSequences(): List<List<Move>> {
            val locations = getCrabLocations()
            return crabs.flatMap { crab ->
                val up = Move(crab.name, Direction.UP)
                val left = Move(crab.name, Direction.LEFT)
                val right = Move(crab.name, Direction.RIGHT)
                val down = Move(crab.name, Direction.DOWN)
                val crabLoc = locations[crab]!!
                val otherLocations = locations.filter { it.key != crab }
                return@flatMap when (crabLoc.rowNum) {
                    2 -> {
                        if (otherLocations.none { it.value.colNum == crabLoc.colNum }) {
                            listOf(listOf(down))
                        } else if (crabLoc.colNum == crab.desiredColumn() && otherLocations.any { it.value.rowNum == 3 && it.value.colNum == crabLoc.colNum && it.key.desiredColumn() == crab.desiredColumn() }) {
                            emptyList()
                        } else {
                            val possibleMoves = mutableListOf<List<Move>>()
                            for (dest in listOf(1, 2, 4, 6, 8, 10, 11)) run {
                                val muta = mutableListOf(up)
                                if (dest < crabLoc.colNum) for (i in (crabLoc.colNum - 1) downTo dest) {
                                    if (otherLocations.any { it.value.rowNum == 1 && it.value.colNum == i }) return@run
                                    muta.add(left)
                                } else for (i in (crabLoc.colNum + 1)..dest) {
                                    if (otherLocations.any { it.value.rowNum == 1 && it.value.colNum == i }) return@run
                                    muta.add(right)
                                }
                                possibleMoves.add(muta)
                            }
                            possibleMoves
                        }
                    }
                    3 -> {
                        if (otherLocations.any { it.value.colNum == crabLoc.colNum }) emptyList()
                        else {
                            val possibleMoves = mutableListOf<List<Move>>()
                            for (dest in listOf(1, 2, 4, 6, 8, 10, 11)) run {
                                val muta = mutableListOf(up, up)
                                if (dest < crabLoc.colNum) for (i in (crabLoc.colNum - 1) downTo dest) {
                                    if (otherLocations.any { it.value.rowNum == 1 && it.value.colNum == i }) return@run
                                    muta.add(left)
                                } else for (i in (crabLoc.colNum + 1)..dest) {
                                    if (otherLocations.any { it.value.rowNum == 1 && it.value.colNum == i }) return@run
                                    muta.add(right)
                                }
                                possibleMoves.add(muta)
                            }
                            possibleMoves
                        }
                    }
                    1 -> {
                        val muta = mutableListOf(down)
                        val dest = crab.desiredColumn()
                        if (otherLocations.any { it.value.rowNum == 2 && it.value.colNum == dest }) return@flatMap emptyList()
                        if (otherLocations.any {
                                it.value.rowNum == 3 && it.value.colNum == dest && !it.key.name.startsWith(crab.name.first())
                            }) {
                            return@flatMap emptyList()
                        }
                        if (dest < crabLoc.colNum) for (i in (crabLoc.colNum - 1) downTo dest) {
                            if (otherLocations.any { it.value.rowNum == 1 && it.value.colNum == i }) return@flatMap emptyList()
                            muta.add(left)
                        } else for (i in (crabLoc.colNum + 1)..dest) {
                            if (otherLocations.any { it.value.rowNum == 1 && it.value.colNum == i }) return@flatMap emptyList()
                            muta.add(right)
                        }
                        listOf(muta)
                    }
                    else -> {
                        emptyList()
                    }
                }
            }
        }

        fun stringRep(): String {
            val crabLocations = getCrabLocations()
            val initialRow = List(13) { "#" }.joinToString("")
            val secondRow = (0..12).map { colNum ->
                if (colNum == 0 || colNum == 12) "#" else {
                    crabLocations.entries.firstOrNull { it.value.colNum == colNum && it.value.rowNum == 1 }
                        ?.let { "${it.key.name[0]}" } ?: "."
                }
            }.joinToString("")
            val thirdRow = (0..12).map { colNum ->
                if (colNum in listOf(0, 1, 2, 4, 6, 8, 10, 11, 12)) "#" else {
                    crabLocations.entries.firstOrNull { it.value.colNum == colNum && it.value.rowNum == 2 }
                        ?.let { "${it.key.name[0]}" } ?: "."
                }
            }.joinToString("")
            val fourthRow = (0..12).map { colNum ->
                if (colNum in listOf(0, 1, 2, 4, 6, 8, 10, 11, 12)) "#" else {
                    crabLocations.entries.firstOrNull { it.value.colNum == colNum && it.value.rowNum == 3 }
                        ?.let { "${it.key.name[0]}" } ?: "."
                }
            }.joinToString("")
            return listOf(initialRow, secondRow, thirdRow, fourthRow, initialRow).joinToString("\n")
        }

        fun allSatisfied() = getCrabLocations().entries.all { (crab, loc) -> loc.colNum == crab.desiredColumn() }

        constructor(moves: List<Move>, crabs: List<Crab>) : this() {
            this.moves = moves
            this.crabs = crabs
        }

        constructor(startingOrder: String) : this() {
            val cr = mutableListOf<Crab>()
            startingOrder.forEachIndexed { i, l ->
                cr.add(Crab("$l${if (cr.any { it.name.startsWith(l) }) 1 else 0}", i))
            }
            crabs = cr
            moves = emptyList()
        }
    }

    data class Crab(val name: String, val startingLoc: Int) {
        fun desiredColumn() = when (name.first()) {
            'A' -> 3
            'B' -> 5
            'C' -> 7
            else -> 9
        }
    }

    data class Move(var crabName: String, var direction: Direction)

    fun List<Move>.totalCost() = sumOf {
        when (it.crabName.first()) {
            'A' -> 1L
            'B' -> 10
            'C' -> 100
            else -> 1000
        }
    }

    enum class Direction { UP, DOWN, LEFT, RIGHT }

    fun Map<Crab, Point>.matches(other: Map<Crab, Point>) = entries.all { entry ->
        if (other.containsKey(entry.key)) {
            val otherPoint = other[entry.key]!!
            otherPoint.colNum == entry.value.colNum && otherPoint.rowNum == entry.value.rowNum
        } else false
    }

    fun List<Move>.sameMoves(other: List<Move>): Boolean {
        val a = this.groupingBy { it.toString() }.eachCount()
        val b = other.groupingBy { it.toString() }.eachCount()
        return a.all { b[it.key] == it.value }
    }

    fun parseInput(lines: List<String>) = lines.joinToString("").filter { it.isLetter() }

    val desiredState = """
#############
#...........#
###A#B#C#D###
###A#B#C#D###
#############
    """.trimIndent()

    val step1 = """
#############
#.....D.....#
###B#.#C#D###
###A#B#C#A###
#############
    """.trimIndent()
    val step2 = """
#############
#...B.D.....#
###.#.#C#D###
###A#B#C#A###
#############
    """.trimIndent()
    val step3 = """
#############
#.....D.....#
###.#B#C#D###
###A#B#C#A###
#############
    """.trimIndent()
}



