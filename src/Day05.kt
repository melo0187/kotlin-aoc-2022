import java.util.Stack

object CrateMover9000 {
    operator fun invoke(crateCount: Int, srcStack: Stack<Char>, targetStack: Stack<Char>) {
        repeat(crateCount) {
            srcStack.pop()
                .let(targetStack::push)
        }
    }
}

object CrateMover9001 {
    operator fun invoke(crateCount: Int, srcStack: Stack<Char>, targetStack: Stack<Char>) {
        (1..crateCount)
            .fold(mutableListOf<Char>()) { buffer, _ ->
                srcStack.pop().let(buffer::add)
                buffer
            }
            .reversed()
            .let(targetStack::addAll)
    }
}

fun main() {
    fun List<String>.toStacks(): Array<Stack<Char>> {
        val stackCount =
            takeLast(1)
                .single()
                .toList()
                .mapNotNull(Char::digitToIntOrNull)
                .max()

        val startingStacksSlices =
            dropLast(1)
                .map { drawingLine ->
                    drawingLine
                        .windowed(3, 4)
                        .map { crate ->
                            crate
                                .removeSurrounding("[", "]")
                                .singleOrNull()
                        }
                }

        val startingStacks = startingStacksSlices
            .foldRight(Array<Stack<Char>>(stackCount) { Stack() }) { sliceAcrossStacks, stacks ->
                sliceAcrossStacks.forEachIndexed { stackIndex, crate ->
                    crate?.let(stacks[stackIndex]::push)
                }
                stacks
            }
        return startingStacks
    }

    fun Array<Stack<Char>>.rearrangeStacks(
        rearrangementProcedure: List<String>,
        pickupAndMove: (crateCount: Int, srcStack: Stack<Char>, targetStack: Stack<Char>) -> Unit
    ) {
        rearrangementProcedure
            .forEach { step ->
                val (crateCount, srcStackNumber, targetStackNumber) =
                    step
                        .split(' ')
                        .mapNotNull(String::toIntOrNull)
                val srcStack = get(srcStackNumber - 1)
                val targetStack = get(targetStackNumber - 1)

                pickupAndMove(crateCount, srcStack, targetStack)
            }
    }

    fun part1(input: List<String>): String {
        val startingStacksDrawing = input.takeWhile(String::isNotBlank)
        val rearrangementProcedure = input.takeLastWhile(String::isNotBlank)

        val startingStacks = startingStacksDrawing.toStacks()

        return with(startingStacks) {
            rearrangeStacks(rearrangementProcedure, CrateMover9000::invoke)

            mapNotNull(Stack<Char>::lastOrNull)
                .joinToString("")
        }
    }

    fun part2(input: List<String>): String {
        val startingStacksDrawing = input.takeWhile(String::isNotBlank)
        val rearrangementProcedure = input.takeLastWhile(String::isNotBlank)

        val startingStacks = startingStacksDrawing.toStacks()

        return with(startingStacks) {
            rearrangeStacks(rearrangementProcedure, CrateMover9001::invoke)

            mapNotNull(Stack<Char>::lastOrNull)
                .joinToString("")
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
