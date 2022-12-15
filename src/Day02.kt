import Shape.Companion.elvenShapeCodes
import Shape.Companion.shapeByCode
import java.lang.IllegalArgumentException

sealed interface Shape {
    val score: Int
    val defeat: Shape

    companion object {
        val elvenShapeCodes = listOf('A', 'B', 'C')
        private val myShapeCodes = listOf('X', 'Y', 'Z')

        fun shapeByCode(code: Char): Shape {
            val shapeCodes = elvenShapeCodes.zip(myShapeCodes)
            return when (code) {
                in shapeCodes[0].toList() -> ROCK
                in shapeCodes[1].toList() -> PAPER
                in shapeCodes[2].toList() -> SCISSORS
                else -> throw IllegalArgumentException("Code is not a valid shape code!")
            }
        }
    }

    object ROCK : Shape {
        override val score: Int = 1
        override val defeat: Shape = SCISSORS
    }

    object PAPER : Shape {
        override val score: Int = 2
        override val defeat: Shape = ROCK
    }

    object SCISSORS : Shape {
        override val score: Int = 3
        override val defeat: Shape = PAPER
    }

    fun match(opponentShape: Shape): Outcome {
        return when (opponentShape) {
            this -> Outcome.DRAW
            defeat -> Outcome.WIN
            else -> Outcome.LOSS
        }
    }

    enum class Outcome(val score: Int) {
        LOSS(0),
        DRAW(3),
        WIN(6),
    }
}

fun main() {
    fun List<String>.toShapeMatchups() = map { line ->
        val (elfCode, myCode) = line.slice(listOf(0, 2))
            .partition { shapeCode -> shapeCode in elvenShapeCodes }

        elfCode.single() to myCode.single()
    }.map { (elfCode, myCode) ->
        shapeByCode(elfCode) to shapeByCode(myCode)
    }

    fun part1(input: List<String>): Int =
        input.toShapeMatchups()
            .sumOf { (elfShape, myShape) ->
                myShape.score + myShape.match(elfShape).score
            }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}