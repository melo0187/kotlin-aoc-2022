fun main() {
    fun <T> List<String>.chunkedByBlank(transform: ((String) -> T)) =
        fold(mutableListOf(mutableListOf<T>())) { acc, line ->
            when {
                line.isNotBlank() -> acc.last().add(transform(line))
                else -> acc.add(mutableListOf())
            }
            acc
        }.map {
            it.toList()
        }.toList()

    fun part1(input: List<String>): Int =
        input.chunkedByBlank(String::toInt)
            .maxOf { elvesCalories ->
                elvesCalories.sum()
            }

    fun part2(input: List<String>): Int =
        input.chunkedByBlank(String::toInt)
            .map { elvesCalories -> elvesCalories.sum() }
            .sortedDescending()
            .take(3)
            .sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
