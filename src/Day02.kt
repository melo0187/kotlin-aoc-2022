import Codes.elvenShapeCodes
import Codes.outcomeByCode
import Codes.shapeByCode

sealed interface Shape {
    val score: Int
    val defeat: Shape

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

    fun matchWith(opponentShape: Shape): Outcome =
        when (opponentShape) {
            this -> Outcome.DRAW
            defeat -> Outcome.WIN
            else -> Outcome.LOSS
        }

    fun findMatchingShapeFor(outcome: Outcome): Shape = when (outcome) {
        Outcome.LOSS -> defeat
        Outcome.DRAW -> this
        Outcome.WIN -> findShapeThatDefeats(this)
    }

    private tailrec fun findShapeThatDefeats(shape: Shape): Shape =
        if (shape.defeat == this) {
            shape
        } else {
            findShapeThatDefeats(shape.defeat)
        }

    enum class Outcome(val score: Int) {
        LOSS(0),
        DRAW(3),
        WIN(6),
    }
}

object Codes {
    val elvenShapeCodes = listOf('A', 'B', 'C')
    private val myShapeCodes = listOf('X', 'Y', 'Z')

    val shapeByCode = (elvenShapeCodes + myShapeCodes)
        .associate {
            when (it) {
                'A', 'X' -> it to Shape.ROCK
                'B', 'Y' -> it to Shape.PAPER
                'C', 'Z' -> it to Shape.SCISSORS
                else -> error("Unknown shape code!")
            }
        }

    val outcomeByCode =
        mapOf(
            'X' to Shape.Outcome.LOSS,
            'Y' to Shape.Outcome.DRAW,
            'Z' to Shape.Outcome.WIN
        )
}

fun main() {
    fun List<String>.toCodePairs() = map { line ->
        val (elfCode, myCode) = line.slice(listOf(0, 2))
            .partition { shapeCode -> shapeCode in elvenShapeCodes }

        elfCode.single() to myCode.single()
    }

    fun part1(input: List<String>): Int =
        input.toCodePairs()
            .map { (elfCode, myCode) ->
                shapeByCode.getValue(elfCode) to shapeByCode.getValue(myCode)
            }
            .sumOf { (elfShape, myShape) ->
                myShape.score + myShape.matchWith(elfShape).score
            }

    fun part2(input: List<String>): Int =
        input.toCodePairs()
            .map { (elfCode, myCode) ->
                shapeByCode.getValue(elfCode) to outcomeByCode.getValue(myCode)
            }
            .sumOf { (elfShape, myDesiredOutcome) ->
                val myDesiredShape = elfShape.findMatchingShapeFor(myDesiredOutcome)
                myDesiredShape.score + myDesiredShape.matchWith(elfShape).score
            }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}