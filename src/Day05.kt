import java.util.Stack

fun main() {
    fun part1(input: List<String>): String {
        val startingStacksDrawing = input.takeWhile(String::isNotBlank)

        val stackCount = startingStacksDrawing
            .takeLast(1)
            .single()
            .toList()
            .mapNotNull(Char::digitToIntOrNull)
            .max()

        val startingStacksSlices = startingStacksDrawing
            .dropLast(1)
            .map { drawingLine ->
                drawingLine
                    .windowed(3, 4)
                    .map { cargo ->
                        cargo
                            .removeSurrounding("[", "]")
                            .singleOrNull()
                    }
            }

        val startingStacks = startingStacksSlices
            .foldRight(Array<Stack<Char>>(stackCount) { Stack() }) { sliceAcrossStacks, stacks  ->
                sliceAcrossStacks.forEachIndexed { stackIndex, crate ->
                    crate?.let(stacks[stackIndex]::push)
                }
                stacks
            }

        return with(startingStacks) {
            input.takeLastWhile(String::isNotBlank)
                .let { rearrangementProcedures ->
                    rearrangementProcedures.forEach { procedure ->
                        val (crateCount, srcStackNumber, targetStackNumber) =
                            procedure
                                .split(' ')
                                .mapNotNull(String::toIntOrNull)
                        val srcStack = get(srcStackNumber - 1)
                        val targetStack = get(targetStackNumber - 1)

                        repeat(crateCount) {
                            srcStack.pop()
                                .let(targetStack::push)
                        }
                    }
                }

            mapNotNull(Stack<Char>::lastOrNull)
                .joinToString("")
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
